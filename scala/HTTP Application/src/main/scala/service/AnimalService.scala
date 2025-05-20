package service

import cats.effect.{Ref, Sync}
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.{FlatMap, Monad}
import domain.AnimalInfo

trait AnimalService[F[_]] {
  def getAnimals: F[List[String]]
  def getAnimalByName(name: String): F[Option[AnimalInfo]]
  def addAnimal(animalInfo: AnimalInfo): F[Unit]
}

object AnimalService {

  def make[I[_]: Sync, F[_]: Sync]: I[AnimalService[F]] =
    Ref.in[I, F, Set[AnimalInfo]](Set.empty[AnimalInfo])
      .map(new AnimalServiceImpl[F](_))

  def populate[F[_]: FlatMap](service: AnimalService[F]): F[Unit] =
    service.addAnimal(AnimalInfo("Cat", "Cats are cute", 25)) >>
      service.addAnimal(AnimalInfo("Dog", "Dogs can run fast", 13)) >>
      service.addAnimal(AnimalInfo("Bird", "Birds are small and can fly", 42))

  private class AnimalServiceImpl[F[_]: Monad](store: Ref[F, Set[AnimalInfo]]) extends AnimalService[F] {

    override def getAnimals: F[List[String]] =
      store.get.map(_.toList.map(_.name))

    override def getAnimalByName(name: String): F[Option[AnimalInfo]] =
      store.get.map(_.find(_.name == name))

    override def addAnimal(animalInfo: AnimalInfo): F[Unit] =
      store.update(_ + animalInfo)
  }
}
