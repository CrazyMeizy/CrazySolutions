package task1.hierarchy

import cats.Eval
import task1.{Branch, Leaf, Tree}
//import scala.annotation.tailrec
import scala.util.control.TailCalls.{TailRec, done, tailcall}

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

trait Apply[F[_]] extends Functor[F] {
  def ap[A, B](ff: F[A => B])(fa: F[A]): F[B]
}

trait Applicative[F[_]] extends Apply[F] {
  def pure[A](a: A): F[A]
}

trait FlatMap[F[_]] extends Apply[F] {
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

  // Нужно реализовать с @tailrec.
  // https://typelevel.org/cats/typeclasses/monad.html#tailrecm
  def tailRecM[A, B](a: A)(f: A => F[Either[A, B]]): F[B]
}

trait Monad[F[_]] extends FlatMap[F] with Applicative[F] {
  def pure[A](a: A): F[A]

  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
}

object TypeClasses {
  // Реализовать инстансы нужно в этом объекте.
  implicit val TreeMonad: Monad[Tree] = new Monad[Tree] {

    override def pure[A](a: A): Tree[A] = Leaf(a)

    override def flatMap[A, B](fa: Tree[A])(f: A => Tree[B]): Tree[B] = {
      def makeTree(fa: Tree[A]): Eval[Tree[B]] = {
        fa match {
          case Branch(left, right) =>
            for {
              l <- Eval.defer(makeTree(left))
              r <- Eval.defer(makeTree(right))
            } yield Branch(l, r)
          case Leaf(value) => Eval.now(f(value))
        }
      }

      makeTree(fa).value
    }

    override def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] = {
      def makeTree(tree: Tree[Either[A, B]]): TailRec[Tree[B]] = {
        tree match {
          case Leaf(Left(value)) =>
            tailcall(makeTree(f(value)))
          case Leaf(Right(value)) =>
            done(Leaf(value))
          case Branch(left, right) =>
            for {
              l <- tailcall(makeTree(left))
              r <- tailcall(makeTree(right))
            } yield Branch(l, r)
        }
      }

      makeTree(f(a)).result
    }

    override def ap[A, B](ff: Tree[A => B])(fa: Tree[A]): Tree[B] = {
      def makeTree(ff: Tree[A => B])(fa: Tree[A]): TailRec[Tree[B]] = {
        (ff, fa) match {
          case (Branch(leftF, rightF), Branch(leftA, rightA)) =>
            for {
              l <- tailcall(makeTree(leftF)(leftA))
              r <- tailcall(makeTree(rightF)(rightA))
            } yield Branch(l, r)
          case (leaf, Branch(leftA, rightA)) =>
            for {
              l <- tailcall(makeTree(leaf)(leftA))
              r <- tailcall(makeTree(leaf)(rightA))
            } yield Branch(l, r)
          case (Branch(leftF, rightF), leaf) =>
            for {
              l <- tailcall(makeTree(leftF)(leaf))
              r <- tailcall(makeTree(rightF)(leaf))
            } yield Branch(l, r)
          case (Leaf(f), Leaf(a)) => done(Leaf(f(a)))
        }
      }

      makeTree(ff)(fa).result
    }

    override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = {
      def makeTree(fa: Tree[A]): TailRec[Tree[B]] = {
        fa match {
          case Branch(left, right) =>
            for {
              l <- tailcall(makeTree(left))
              r <- tailcall(makeTree(right))
            } yield Branch(l, r)
          case Leaf(value) => done(Leaf(f(value)))
        }
      }

      makeTree(fa).result
    }
  }
}
