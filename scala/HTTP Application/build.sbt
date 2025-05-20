ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.16"
ThisBuild / organization := "ru.itmo.scala"

lazy val root: Project = project
  .in(file("."))
  .settings(
    name := "http-application",
    addCompilerPlugin(Dependencies.kindProjector),
    addCompilerPlugin(Dependencies.bmFor),
    libraryDependencies ++= List(
      Dependencies.catsEffect,
      Dependencies.xml,
      // server deps
      Dependencies.tapir.verxServer,
      Dependencies.tapir.cats,
      Dependencies.tapir.metrics,
      Dependencies.tapir.swagger,
      Dependencies.tapir.json,
      Dependencies.tethys.jackson,
      Dependencies.tethys.derivation,
      // config deps
      Dependencies.pureconfig.core,
      Dependencies.pureconfig.generic,
      // client deps
      Dependencies.client.tethys,
      Dependencies.client.cats,
    ),
  )
