package io

import scala.annotation.tailrec
import scala.language.implicitConversions
import scala.util.Try

/** Класс типов, позволяющий комбинировать описания вычислений, которые могут либо успешно
  * завершиться с некоторым значением, либо завершиться неуспешно, выбросив исключение Throwable.
  * @tparam F
  *   тип вычисления
  */
trait Computation[F[_]] {

  def map[A, B](fa: F[A])(f: A => B): F[B]
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
  def tailRecM[A, B](a: A)(f: A => F[Either[A, B]]): F[B]
  def pure[A](a: A): F[A]
  def *>[A, B](fa: F[A])(another: F[B]): F[B]
  def as[A, B](fa: F[A])(newValue: => B): F[B]
  def void[A](fa: F[A]): F[Unit]
  def attempt[A](fa: F[A]): F[Either[Throwable, A]]
  def option[A](fa: F[A]): F[Option[A]]

  /** Если вычисление fa выбрасывает ошибку, то обрабатывает ее функцией f, без изменения типа
    * выходного значения.
    * @return
    *   результат вычисления fa или результат функции f
    */
  def handleErrorWith[A, AA >: A](fa: F[A])(f: Throwable => F[AA]): F[AA]

  /** Обрабатывает ошибку вычисления чистой функцией recover или преобразует результат вычисления
    * чистой функцией.
    * @return
    *   результат вычисления преобразованный функцией map или результат функции recover
    */
  def redeem[A, B](fa: F[A])(recover: Throwable => B, map: A => B): F[B]
  def redeemWith[A, B](fa: F[A])(recover: Throwable => F[B], bind: A => F[B]): F[B]

  /** Выполняет вычисление. "unsafe", потому что при неуспешном завершении может выбросить
    * исключение.
    * @param fa
    *   еще не начавшееся вычисление
    * @tparam A
    *   тип результата вычисления
    * @return
    *   результат вычисления, если оно завершится успешно.
    */
  def unsafeRunSync[A](fa: F[A]): A

  /** Оборачивает ошибку в контекст вычисления.
    * @param error
    *   ошибка
    * @tparam A
    *   тип результата вычисления. Т.к. вычисление сразу завершится ошибкой при выполнении, то может
    *   быть любым.
    * @return
    *   создает описание вычисления, которое сразу же завершается с поданной ошибкой.
    */
  def raiseError[A](error: Throwable): F[A]

}

object Computation {
  def apply[F[_]: Computation]: Computation[F] = implicitly[Computation[F]]
}

final class MyIO[A] private (private[io] val step: MyIO.Step[A]) {
  self =>

  def map[B](f: A => B)(implicit
    comp: Computation[MyIO]
  ): MyIO[B] = comp.map(self)(f)

  def flatMap[B](f: A => MyIO[B])(implicit
    comp: Computation[MyIO]
  ): MyIO[B] = comp.flatMap(self)(f)

  def tailRecM[B](f: A => MyIO[Either[A, B]])(implicit
    comp: Computation[MyIO]
  ): MyIO[B] =
    self.flatMap(a => comp.tailRecM(a)(f))

  def *>[B](another: MyIO[B])(implicit
    comp: Computation[MyIO]
  ): MyIO[B] = comp.*>(self)(another)

  def as[B](newValue: => B)(implicit
    comp: Computation[MyIO]
  ): MyIO[B] = comp.as(self)(newValue)

  def void(implicit
    comp: Computation[MyIO]
  ): MyIO[Unit] = comp.void(self)

  def attempt(implicit
    comp: Computation[MyIO]
  ): MyIO[Either[Throwable, A]] = comp.attempt(self)

  def option(implicit
    comp: Computation[MyIO]
  ): MyIO[Option[A]] = comp.option(self)

  def handleErrorWith[AA >: A](f: Throwable => MyIO[AA])(implicit
    comp: Computation[MyIO]
  ): MyIO[AA] = comp.handleErrorWith[A, AA](self)(f)

  def redeem[B](recover: Throwable => B, map: A => B)(implicit
    comp: Computation[MyIO]
  ): MyIO[B] = comp.redeem(self)(recover, map)

  def redeemWith[B](recover: Throwable => MyIO[B], bind: A => MyIO[B])(implicit
    comp: Computation[MyIO]
  ): MyIO[B] = comp.redeemWith(self)(recover, bind)

  def unsafeRunSync(implicit
    comp: Computation[MyIO]
  ): A = comp.unsafeRunSync(self)

}

/*----------------------------------------------------------------------------------------------*/

object MyIO {

  /** Внутренняя структура для представления различных шагов вычисления */
  sealed trait Step[+A]
  private case class Pure[A](a: A)                               extends Step[A]
  private case class Suspend[A](thunk: () => MyIO[A])            extends Step[A]
  private case class FlatMap[A, B](fa: MyIO[A], f: A => MyIO[B]) extends Step[B]
  private case class RaiseError[A](error: Throwable)             extends Step[A]

  private case class HandleErrorWith[A, B >: A](fa: MyIO[A], f: Throwable => MyIO[B])
      extends Step[B]

  /** Неявное преобразование между MyIO и Step */
  implicit def stepToMyIO[A](step: Step[A]): MyIO[A] = new MyIO(step)
  implicit def myIOToStep[A](io: MyIO[A]): Step[A]   = io.step

  implicit val computationInstanceForIO: Computation[MyIO] = new Computation[MyIO] {
    override def map[A, B](fa: MyIO[A])(f: A => B): MyIO[B] = new MyIO(
      FlatMap(fa, (a: A) => pure(f(a)))
    )
    override def flatMap[A, B](fa: MyIO[A])(f: A => MyIO[B]): MyIO[B] = new MyIO(
      FlatMap(fa, f)
    )
    override def tailRecM[A, B](a: A)(f: A => MyIO[Either[A, B]]): MyIO[B] = new MyIO(
      FlatMap(
        pure(a),
        (a1: A) =>
          flatMap(f(a1)) {
            case Left(a)  => tailRecM(a)(f)
            case Right(b) => pure(b)
          }
      )
    )

    override def pure[A](a: A): MyIO[A] = new MyIO(Pure(a))

    override def *>[A, B](fa: MyIO[A])(another: MyIO[B]): MyIO[B] = flatMap(fa)(_ => another)

    override def as[A, B](fa: MyIO[A])(newValue: => B): MyIO[B] = map(fa)(_ => newValue)

    override def void[A](fa: MyIO[A]): MyIO[Unit] = pure(())

    override def attempt[A](fa: MyIO[A]): MyIO[Either[Throwable, A]] =
      handleErrorWith(
        new MyIO(
          FlatMap[A, Either[Throwable, A]](fa, (a: A) => pure(Right(a)))
        )
      )((e: Throwable) => pure(Left(e)))

    override def option[A](fa: MyIO[A]): MyIO[Option[A]] =
      handleErrorWith(
        new MyIO(
          FlatMap[A, Option[A]](fa, (a: A) => pure(Some(a)))
        )
      )((_: Throwable) => pure(None))

    override def handleErrorWith[A, AA >: A](fa: MyIO[A])(f: Throwable => MyIO[AA]): MyIO[AA] =
      new MyIO(HandleErrorWith(fa, f))

    override def redeem[A, B](fa: MyIO[A])(recover: Throwable => B, map: A => B): MyIO[B] =
      computationInstanceForIO.map(attempt(fa)) {
        case Left(e)  => recover(e)
        case Right(a) => map(a)
      }

    override def redeemWith[A, B](
      fa: MyIO[A]
    )(recover: Throwable => MyIO[B], bind: A => MyIO[B]): MyIO[B] =
      flatMap(attempt(fa)) {
        case Left(e)  => recover(e)
        case Right(a) => bind(a)
      }

    def unsafeRunSync[A](fa: MyIO[A]): A = {
      sealed trait Frame
      case class SuccessCont(f: Any => MyIO[Any])        extends Frame
      case class ErrorHandler(f: Throwable => MyIO[Any]) extends Frame

      sealed trait Result
      case class Success(value: Any)       extends Result
      case class Failure(error: Throwable) extends Result

      @tailrec
      def loop(currentIO: MyIO[Any], stack: List[Frame]): Any = currentIO.step match {
        case Pure(a) =>
          eval(a) match {
            case Success(value) =>
              stack match {
                case Nil => value
                case frame :: rest =>
                  frame match {
                    case SuccessCont(f)  => loop(f(value), rest)
                    case ErrorHandler(_) => loop(currentIO, rest)
                  }
              }
            case Failure(e) =>
              handleError(e, stack)
          }

        case Suspend(thunk) =>
          eval(thunk()) match {
            case Success(nextIO) =>
              loop(nextIO.asInstanceOf[MyIO[Any]], stack)
            case Failure(e) =>
              handleError(e, stack)
          }

        case FlatMap(fa, f) =>
          loop(fa, SuccessCont(f) :: stack)

        case RaiseError(e) =>
          handleError(e, stack)

        case HandleErrorWith(fa, f) =>
          loop(fa, ErrorHandler(f) :: stack)
      }

      @tailrec
      def handleError(e: Throwable, stack: List[Frame]): Any = stack match {
        case Nil => throw e
        case frame :: rest =>
          frame match {
            case SuccessCont(_)  => handleError(e, rest)
            case ErrorHandler(f) => loop(f(e), rest)
          }
      }

      def eval(thunk: => Any): Result =
        try
          Success(thunk)
        catch {
          case e: Throwable => Failure(e)
        }

      loop(fa.asInstanceOf[MyIO[Any]], Nil).asInstanceOf[A]
    }

    override def raiseError[A](error: Throwable): MyIO[A] = new MyIO(RaiseError(error))
  }

  def apply[A](body: => A): MyIO[A] = delay(body)

  def suspend[A](thunk: => MyIO[A]) /*(implicit
    comp: Computation[MyIO]
  )*/: MyIO[A] = Suspend(() => thunk)

  def delay[A](body: => A): MyIO[A] = suspend(pure(body))
  def pure[A](a: A): MyIO[A]        = computationInstanceForIO.pure(a)

  def fromEither[A](e: Either[Throwable, A])(implicit
    comp: Computation[MyIO]
  ): MyIO[A] = e match {
    case Right(a) => pure(a)
    case Left(e)  => comp.raiseError(e)
  }

  def fromOption[A](option: Option[A])(orElse: => Throwable)(implicit
    comp: Computation[MyIO]
  ): MyIO[A] = fromEither(option.toRight(orElse))(comp)

  def fromTry[A](t: Try[A])(implicit
    comp: Computation[MyIO]
  ): MyIO[A] = fromEither(t.toEither)(comp)

  def none[A]: MyIO[Option[A]] = pure(None)

  def raiseUnless(cond: Boolean)(e: => Throwable)(implicit
    comp: Computation[MyIO]
  ): MyIO[Unit] =
    if (!cond) comp.raiseError(e)
    else unit

  def raiseWhen(cond: Boolean)(e: => Throwable)(implicit
    comp: Computation[MyIO]
  ): MyIO[Unit] =
    if (cond) comp.raiseError(e)
    else unit

  def raiseError[A](error: Throwable)(implicit
    comp: Computation[MyIO]
  ): MyIO[A] = comp.raiseError(error)

  def unlessA(cond: Boolean)(action: => MyIO[Unit]): MyIO[Unit] =
    if (!cond) action
    else unit

  def whenA(cond: Boolean)(action: => MyIO[Unit]): MyIO[Unit] =
    if (cond) action
    else unit

  val unit: MyIO[Unit] = pure(())

}
