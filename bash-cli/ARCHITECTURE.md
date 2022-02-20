[Ссылка на диаграмму](https://drive.google.com/file/d/1ugLOWAhoQyYoIes2i5ooCkWwWV5iZhIs/view?usp=sharing)

## Допущения:

* В команде нет переноса строк
* Многопоточность не используется

## Описание функции main: 
*   В цикле считываем строку `line`, например, с помощью `readLine`
    * Разбиваем на токены, вызывая метод `Parser::splitIntoTokens`
    * Затем обходим каждый токен и если он не одинарных кавычках, то делаем подстановку с помощью `Substitutor::substitute`
    * Если токен был без кавычек, то после подстановки снова разбиваем на токены и мёржим с общим списком токенов.
    * Далее бьём список токенов на группы, относящиеся к одной и той же команде, вызывая `Parser::splitIntoCommands`.
    * Затем для каждой группы вызываем `CommandFactory::getCommand` и получаем `List<Command`.
    * Создаем Pipe и вызываем execute.
    * Если ReturnCode::EXIT, то завершаем работу интерпретатора.
## Pipe
Класс, отвечающий за пайп комманд
```kotlin
class Pipe(val commands: List<Command>) {
    execute(input: Stream, out: Stream, error: Stream, environment: Environment): ReturnCode {}
}
```
План реализации:
* Первой команде передаём `input`, если это не `System.in`, как `input` и `ByteArrayOutputStream`, как `output` в `Command::execute`.
* Далее для всех команд, кроме первой, создаём `ByteArrayInputStream` от массива, полученного из `ByteArrayOutputStream` прошлой команды, и передаём его как `input`, затем вызываем на `ByteArrayOutputStream` `reset` и передаём его, как `output` в `Command::execute`.
* Если код возврата у какой-то команды не `ReturnCode::SUCCESS`, то завершаем выполнение пайпа.
* `Output` последней команды пишем в out.
## Command 
Интерфейс, от которого будут наследоваться другие команды 
```kotlin    
enum StatusCode {
    EXIT,
    ERROR,
    SUCCESS
}

data class ReturnCode(val status: StatusCode, val code: Int)

interface Command(private args: List<Token>) {
    execute(in: Stream, out: Stream, error: Stream, environment: Environment): ReturnCode
};
```

## CommandFactory 
```kotlin
class CommandFactory {
    Map<String, (List<Token>) -> Command> supportedCommands;
    getCommand(args: List<Token>): Command {...}
}
```
Имя команды - args[0]:
1. Если имя команды есть в supportedCommands, то мы вызываем соответствующий конструктор, иначе
2. Если имя команды содержит `=` и имя команды не было в кавычках, то это команда присваивания и мы вызываем метод `Parser::parseAssignmentCommand`, получая аргументы команды присваивания и создаём `AssignmentCommand`, иначе
3. Это внешняя команда и мы объединяем аргументы с названием команды и передаем их в конструктор `ExternalCommand`.

## State 
Хранит состояние системы, реализовано через глобальный Singleton.

```kotlin
class Environment {
    Map<String, String> enviromentVariables
}
```


## Substitution
Класс, который отвечает за подстановку глобальных перменных
```kotlin
object Substitutor {
    substitute(input: Token, environment: Environment): Token {...}
}
```

**План реализации**: 
Так как подстановка в токены, то кавычки уже не обрабатываем:
1. Проверяем quottingType, чтобы понять нужна ли подстановка.
2. Последовательно находим места подстановки: если увидели не экранированный доллар, жадно собираем имя переменной окружения до символа, которого не может быть в имени переменной. Одновременно с эти наращиваем текущий префикс готовой строки `result`.
3. Собрав имя, достаём значение переменной из `State`, если его там не было, то пустую строку.
4. Потом делаем `result += value`
5. Возвращаем `result`

## Token
Класс для токена. Имеет одно из трех состояний: 
* одинарные кавычки
* двойные 
* не в кавычках

## Parser
Класс, отвечает за парсинг строки, которая пришла от пользователя.
* `fun splitIntoCommands(input: List<Token>): List<List<Token>>`: сплиттим input по `|`, которые не в кавычках.
* `fun splitIntoTokens(input: String) : List<Token>`: сплиттим строку по пробелам, которые не в кавычках.
* `fun parseAssignmentCommand(input: String) : List<Token>` разбивает подстроку на имя переменной и ее значение

## Команды
Детали внутренней реализации команд:
### Общее

* В поток ошибок пишем исключение, если оно вылетело и возвращаем `ReturnCode::ERROR`.
* В случае успеха возвращаем `ReturnCode::SUCCESS`.
### cat
* Открываем файлы из аргументов и пишем их последовательно в поток вывода.
* Поток ввода игнорируем, если есть аргументы.

### echo
* Пишем аргументы в вывода последовательно через пробел.

### wc
* Выводит кол-во строк, слов, байт с помощью forEach.
* Если нет аргументов, то поток ввода считаем как файл.

### pwd
* Выводит текущую директорию
* Поток ввода игнорируем. 

### exit
* Возвращаем `ReturnCode::EXIT`.
* Поток ввода игнорируем. 
* В поток вывода ничего не пишем.

### AssignmentCommand
* Если кол-во аргументов не равно 2, то ошибка, иначе первый аргумент - ключ, второй - значение.
* Далее добавляем значение в мапу `State` по данному ключу.

### ExternalCommand
* Используем ProcessBuilder.
* Выставляем процессу окружение, совпадающее с нашим environment.
* Если внешняя команда вернула ненулевой код возврата, то возвращаем `ReturnCode::ERROR`.

### GrepCommand
* делаем разбор аргументов с помощью Clikt.
* если передали имя файла, то открываем и делаем его входным потоком
* из входного потока читаем строки и выводим строку, если
* * в ней есть match 
* * она является контекстом предыдущего match
* Игнорирование регистра
* * С помощью стандартного флага `IGNORE_CASE`
* Поиск слова целиком
* * С помощью добавления в регулярное выражение символов `\b`
