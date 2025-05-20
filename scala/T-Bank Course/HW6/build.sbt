import Dependencies.*

ThisBuild / scalaVersion     := "3.5.0"
ThisBuild / version          := "0.1.0"

lazy val root = (project in file("."))
  .settings(
    name := "hw6",
    libraryDependencies ++= List(
      catsCore,
      scalaTest % Test
    )
  )
