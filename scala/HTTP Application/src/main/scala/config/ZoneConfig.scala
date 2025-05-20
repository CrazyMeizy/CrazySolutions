package config

import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader

final case class ZoneConfig(
    name: String,
    host: String,
    port: Int,
    swaggerEnabled: Boolean,
)

object ZoneConfig {
  implicit val configReaderZone: ConfigReader[ZoneConfig] = deriveReader[ZoneConfig]
}
