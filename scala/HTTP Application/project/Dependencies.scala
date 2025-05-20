import sbt.*

object Dependencies {

  val kindProjector = ("org.typelevel" %% "kind-projector"     % "0.13.3").cross(CrossVersion.full)
  val bmFor         = "com.olegpy"     %% "better-monadic-for" % "0.3.1"

  val catsEffect = "org.typelevel"          %% "cats-effect" % "3.6.1"
  val xml        = "org.scala-lang.modules" %% "scala-xml"   % "2.3.0"

  object tapir {
    val version = "1.11.25"

    val verxServer = "com.softwaremill.sttp.tapir" %% "tapir-vertx-server-cats"  % version
    val cats       = "com.softwaremill.sttp.tapir" %% "tapir-cats"               % version
    val metrics    = "com.softwaremill.sttp.tapir" %% "tapir-prometheus-metrics" % version
    val swagger    = "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle"  % version
    val json       = "com.softwaremill.sttp.tapir" %% "tapir-json-tethys"        % version
  }

  object tethys {
    val version = "0.29.4"

    val jackson    = "com.tethys-json" %% "tethys-jackson213" % version
    val derivation = "com.tethys-json" %% "tethys-derivation" % version
  }

  object pureconfig {
    val version = "0.17.9"

    val core    = "com.github.pureconfig" %% "pureconfig-core"    % version
    val generic = "com.github.pureconfig" %% "pureconfig-generic" % version
  }

  object client {
    val version = "4.0.3"

    val tethys = "com.softwaremill.sttp.client4" %% "tethys-json" % version
    val cats   = "com.softwaremill.sttp.client4" %% "cats"        % version

  }

}
