Global / onChangedBuildSource := ReloadOnSourceChanges
ThisBuild / scalaVersion := "2.13.15"
ThisBuild / version := "0.1.0-SNAPSHOT"
Global / concurrentRestrictions := List(
  Tags.limit(Tags.Test, 1),
)

Compile / compile / scalacOptions ++= Seq(
  "-Werror",
  "-Wdead-code",
  "-Wextra-implicit",
  "-Wnumeric-widen",
  "-Wunused",
  "-Wvalue-discard",
  "-Xlint",
  "-Xlint:-byname-implicit",
  "-Xlint:-implicit-recursion",
  "-unchecked"
)

lazy val root = (project in file("."))
  .settings(
    name := "hw8",
    libraryDependencies ++= Dependencies.test.all ++ Dependencies.core.all
  )
