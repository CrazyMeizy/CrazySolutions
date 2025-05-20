package db_app.repository

import cats.effect.Concurrent
import cats.syntax.traverse._
import doobie.Transactor
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import fs2.Stream
import PeopleRepositoryDoobie.{getByIdQuery, updateNameQuery}
import db_app.model.People

private class PeopleRepositoryDoobie[F[_]: Concurrent](
    xa: Transactor[F]
) extends PeopleRepository[F] {

  override def getAll: Stream[F, People] =
    sql"select id, age, name from people"
      .query[People]
      .stream // get all rows as stream (that means in memory we have only part of all people)
      .transact[F](xa)

  override def getById(id: Long): F[Option[People]] =
    getByIdQuery(id)
      .transact[F](xa)

  override def getLikeName(pattern: String): F[List[People]] =
    sql"select id, age, name from people where name like $pattern"
      .query[People]
      .to[List] // convert all rows to List
      .transact[F](xa)

  override def addOne(age: Int, name: String): F[Long] =
    sql"insert into people (age,name) values ($age, $name)"
      .update
      .withUniqueGeneratedKeys[Long]("id") // get id of inserted people
      .transact[F](xa)

  override def updateName(id: Long, name: String): F[Int] =
    updateNameQuery(id, name)
      .transact[F](xa)

  /** transactional swap of names of people with id1 and id2
    */
  override def swap(leftId: Long, rightId: Long): F[Int] = (
    for {
      left  <- getByIdQuery(leftId)
      right <- getByIdQuery(rightId)
      rowsUpdated <- left.zip(right).traverse { case (l, r) =>
        for {
          fstUpdate <- updateNameQuery(l.id, r.name)
          sndUpdate <- updateNameQuery(r.id, l.name)
        } yield fstUpdate + sndUpdate
      }
    } yield rowsUpdated.getOrElse(0)
  ).transact(xa)
}

object PeopleRepositoryDoobie {

  def make[F[_]: Concurrent](xa: Transactor[F]): PeopleRepository[F] =
    new PeopleRepositoryDoobie(xa)

  private def getByIdQuery(id: Long): ConnectionIO[Option[People]] =
    sql"select id, age, name from people where id = $id"
      .query[People]
      .option // get one or zero rows and return Option, else throw exception

  private def updateNameQuery(id: Long, name: String): ConnectionIO[Int] =
    sql"update people set name = $name where id = $id"
      .update
      .run
}
