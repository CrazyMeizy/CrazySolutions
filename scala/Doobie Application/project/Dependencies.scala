import sbt.*

object Dependencies {

  val kindProjector = ("org.typelevel" %% "kind-projector"     % "0.13.3").cross(CrossVersion.full)
  val bmFor         = "com.olegpy"     %% "better-monadic-for" % "0.3.1"

  val catsEffect = "org.typelevel" %% "cats-effect" % "3.6.1"

  val h2        = "com.h2database" % "h2"              % "2.3.232" // actual version for h2
  val liquibase = "org.liquibase"  % "liquibase-core"  % "4.31.1"  // migrations
  val logback   = "ch.qos.logback" % "logback-classic" % "1.5.18"  // logs

  object doobie {
    val version = "1.0.0-RC9"

    val core     = "org.tpolecat" %% "doobie-core"     % version
    val hikari   = "org.tpolecat" %% "doobie-hikari"   % version
    val postgres = "org.tpolecat" %% "doobie-postgres" % version
    val h2       = "org.tpolecat" %% "doobie-h2"       % version
  }

  object pureconfig {
    val version = "0.17.9"

    val core    = "com.github.pureconfig" %% "pureconfig-core"    % version
    val generic = "com.github.pureconfig" %% "pureconfig-generic" % version
  }

}
