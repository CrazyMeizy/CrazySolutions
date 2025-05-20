import Dependencies.*

ThisBuild / scalaVersion := "2.13.16"
ThisBuild / version      := "0.1.0-SNAPSHOT"

lazy val root = project
  .in(file("."))
  .settings(
    name := "db-application",
    addCompilerPlugin(Dependencies.kindProjector),
    addCompilerPlugin(Dependencies.bmFor),
    libraryDependencies ++= List(
      Dependencies.catsEffect,
      // logging deps
      Dependencies.logback,
      // db deps
      Dependencies.doobie.h2,
      Dependencies.doobie.hikari,
      Dependencies.doobie.core,
      // db jdbc driver
      Dependencies.h2,
      // db migration
      Dependencies.liquibase,
      // config deps
      Dependencies.pureconfig.core,
      Dependencies.pureconfig.generic,
    ),
  )
  .settings(Compile / unmanagedResourceDirectories += baseDirectory.value / "src" / "main" / "migrations")
