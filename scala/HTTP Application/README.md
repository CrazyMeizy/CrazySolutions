# HTTP Application

Пример web server приложения на scala

## Структура проекта

```
├── project
│   ├── build.properties            - версия sbt
│   ├── Dependencies.scala          - зависимости проекта
│   └── plugins.sbt                 - подключаемые плагины для проекта
├── src
│   └── main
│       ├── resources
│       │   └── application.conf    - деволтный кофниг для приложения
│       └── scala                   - код приложения
├── .scalafmt.conf                  - конфигурация форматирования кода
├── build.sbt                       - конфигурация sbt
└── README.md
```

## Запуск

Для запуска выполните команду `sbt run` из корня проекта, или запустите по кнопке `Run` в IntelliJ
IDEA [HttpApplication](src/main/scala/HttpApplication.scala)

## Куда тыкать после запуска?

После запуска будут доступны следующие зоны:

* http://localhost:8080/docs - public зона нашего сервиса
* http://localhost:9345/docs - monitoring зона нашего сервиса
