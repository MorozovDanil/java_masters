# Учёт студентов

## Запуск
| Где запуск | Команда |
| --- | --- |
| PowerShell | `.\gradlew run` |
| cmd | `gradlew run` |
| Linux / macOS | `./gradlew run` |

## Если кириллица выглядит криво из-за Gradle'а
```
.\gradlew build
java -jar build\libs\student-app-1.0.jar
```

Запускал на Arch Linux, на Windows не смотрел - нету.

## Контент
```text
src/main/java/com/studentapp/
├── Main.java                    объединяет всё
├── model/
│   ├── Student.java             запись студента
│   ├── StudentValidator.java    проверка/валидация данных
│   └── ValidationException.java ошибка некорректных данных
├── collection/
│   └── DynamicArray.java        уровень 2
├── io/
│   ├── ConsoleInput.java        считывание в консоли (терминале) + проверка на плохой ввод
│   ├── StudentFileLoader.java   загрузка .csv
│   └── LoadResult.java
├── service/
│   └── StudentService.java      stream API/правила
└── ui/
    └── ConsoleMenu.java         меню
```

Кто кого использует:
```text
ui ──→ service ──→ collection, model
 │
 └───→ io ──────→ collection, model
```
