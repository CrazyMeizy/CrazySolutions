package db_app.repository

import db_app.model.People
import fs2.Stream

trait PeopleRepository[F[_]] {
  def getAll: Stream[F, People]
  def getById(id: Long): F[Option[People]]
  def getLikeName(pattern: String): F[List[People]]
  def addOne(age: Int, name: String): F[Long]
  def updateName(id: Long, name: String): F[Int]
  def swap(idLeft: Long, idRight: Long): F[Int]
}
