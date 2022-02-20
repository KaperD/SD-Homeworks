# CLI

## Usage

* To use our application in ubuntu or macOS or Windows, run in this directory:
```bash
./gradlew -q --console=plain run
```

* To build, test and check code style, run in this directory:
```bash
./gradlew build
```

* If you want to run tests, you should have a python3

## CLI library for Grep
Для разбора параметров была выбрана библиотека CliKt. Выбирали из неё, Apache Commons Cli и kotlinx-cli. Выбрали её, так как она наиболее Kotlin-like (используются делегирующие свойства и т.д.) и позволяют совместить в один класс разбор параметров и логику команды.

