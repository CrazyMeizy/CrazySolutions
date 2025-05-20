package db_app.config

import com.zaxxer.hikari.HikariConfig
import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader

case class DbConfig(
    driver: String,
    url: String,
    user: String,
    password: String
)

object DbConfig {
  implicit val configReaderDb: ConfigReader[DbConfig] = deriveReader[DbConfig]

  def toHikariConf(config: DbConfig): HikariConfig = {
    val hikariConfig = new HikariConfig()
    hikariConfig.setDriverClassName(config.driver)
    hikariConfig.setJdbcUrl(config.url)
    hikariConfig.setUsername(config.user)
    hikariConfig.setPassword(config.password)
    hikariConfig
  }

}
