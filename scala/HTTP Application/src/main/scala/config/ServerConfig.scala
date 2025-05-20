package config

import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader

final case class ServerConfig(
    monitoring: ZoneConfig,
    internal: ZoneConfig,
)

object ServerConfig {
  implicit val configReaderServer: ConfigReader[ServerConfig] = deriveReader[ServerConfig]
}
