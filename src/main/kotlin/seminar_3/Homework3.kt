import java.util.regex.Pattern

sealed class Command {
    abstract fun isValid(): Boolean
}

data class Exit(val arg: String = "") : Command() {
    override fun isValid() = true
}

data class Help(val arg: String = "") : Command() {
    override fun isValid() = true
}

data class AddName(val name: String) : Command() {
    override fun isValid() = true
}

data class AddPhone(val name: String, val phone: String) : Command() {
    override fun isValid(): Boolean {
        val phonePattern = Pattern.compile("\\+[0-9]+")
        return phonePattern.matcher(phone).matches()
    }
}

data class AddEmail(val name: String, val email: String) : Command() {
    override fun isValid(): Boolean {
        val emailPattern = Pattern.compile("[a-zA-Z]+@[a-zA-Z]+\\.[a-zA-Z]+")
        return emailPattern.matcher(email).matches()
    }
}

data class Show(val name: String) : Command() {
    override fun isValid() = true
}

data class Find(val value: String) : Command() {
    override fun isValid() = true
}

fun readCommand(): Command {
    print("Введите команду: ")
    val input = readLine()?.trim() ?: ""

    val commandParts = input.split(" ")
    val commandName = commandParts[0].toLowerCase()
    val arguments = commandParts.drop(1)

    return when (commandName) {
        "exit" -> Exit()
        "help" -> Help()
        "add_name" -> {
            if (arguments.isNotEmpty()) {
                AddName(arguments[0])
            } else {
                Help()
            }
        }
        "add_phone" -> {
            if (arguments.size == 2) {
                AddPhone(arguments[0], arguments[1])
            } else {
                Help()
            }
        }
        "add_email" -> {
            if (arguments.size == 2) {
                AddEmail(arguments[0], arguments[1])
            } else {
                Help()
            }
        }
        "show" -> {
            if (arguments.isNotEmpty()) {
                Show(arguments[0])
            } else {
                Help()
            }
        }
        "find" -> {
            if (arguments.isNotEmpty()) {
                Find(arguments[0])
            } else {
                Help()
            }
        }
        else -> Help()
    }
}

data class Person(val name: String, val phones: List<String>, val emails: List<String>)

fun main() {
    val phoneBook = mutableMapOf<String, Person>()

    while (true) {
        val command = readCommand()
        println("Вы ввели команду: $command")

        if (command.isValid()) {
            when (command) {
                is Exit -> break
                is Help -> printHelp()
                is AddName -> {
                    phoneBook[command.name] = Person(command.name, emptyList(), emptyList())
                    println("Запись добавлена: ${command.name}")
                }
                is AddPhone -> {
                    val person = phoneBook[command.name]
                    if (person != null) {
                        val updatedPerson = person.copy(phones = person.phones + command.phone)
                        phoneBook[command.name] = updatedPerson
                        println("Телефон добавлен для: ${command.name}")
                    } else {
                        println("Ошибка: имя ${command.name} не найдено")
                    }
                }
                is AddEmail -> {
                    val person = phoneBook[command.name]
                    if (person != null) {
                        val updatedPerson = person.copy(emails = person.emails + command.email)
                        phoneBook[command.name] = updatedPerson
                        println("Email добавлен для: ${command.name}")
                    } else {
                        println("Ошибка: имя ${command.name} не найдено")
                    }
                }
                is Show -> {
                    val person = phoneBook[command.name]
                    if (person != null) {
                        println("Телефоны для ${command.name}: ${person.phones.joinToString()}")
                        println("Emails для ${command.name}: ${person.emails.joinToString()}")
                    } else {
                        println("Ошибка: имя ${command.name} не найдено")
                    }
                }
                is Find -> {
                    val foundPeople = phoneBook.filterValues { person ->
                        person.phones.contains(command.value) || person.emails.contains(command.value)
                    }.keys
                    println("Найденные люди для значения ${command.value}: ${foundPeople.joinToString()}")
                }
            }
        } else {
            printHelp()
        }
    }
}

fun printHelp() {
    println("""
        Список команд:
        exit - закрыть программу
        help - показать список команд
        add_name <имя> - создать новую запись
        add_phone <имя> <телефон> - добавить номер телефона к существующему имени
        add_email <имя> <email> - добавить email к существующему имени
        show <имя> - показать связанные с именем телефоны и email
        find <значение> - найти список людей с указанным значением (телефон или email)
    """.trimIndent())
}