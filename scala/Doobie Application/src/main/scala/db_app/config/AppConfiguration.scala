package db_app.config

import cats.effect.kernel.Sync
import cats.syntax.functor._
import cats.syntax.monadError._
import pureconfig.error.{ConfigReaderException, ConfigReaderFailures}
import pureconfig.generic.semiauto.deriveReader
import pureconfig.{ConfigReader, ConfigSource}

// Aggregate all configs for app (to have consistent config path)
final case class AppConfiguration(
    db: DbConfig,
)

object AppConfiguration {
  implicit val configReaderApp: ConfigReader[AppConfiguration] = deriveReader[AppConfiguration]

  def load[I[_]: Sync](
      conf: ConfigSource
  ): I[Either[ConfigReaderFailures, AppConfiguration]] =
    Sync[I].delay(conf.at("ru.itmo.scala").load[AppConfiguration])

  def unsafeLoad[I[_]: Sync](
      conf: ConfigSource
  ): I[AppConfiguration] =
    load[I](conf)
      .map(_.left.map(new ConfigReaderException[AppConfiguration](_)))
      .rethrow
}
