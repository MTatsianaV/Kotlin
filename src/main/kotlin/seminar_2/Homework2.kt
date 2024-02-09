/**
sealed class Command {
    object Exit : Command()
    object Help : Command()
    object Show : Command()
    class AddName(val name: String) : Command()
    class AddPhone(val name: String, val phone: String) : Command()
    class AddEmail(val name: String, val email: String) : Command()

}

fun readCommand(): Command {
    val input = readLine() ?: return Command.Exit
    return when (input) {
        "exit" -> Command.Exit
        "help" -> Command.Help
        "show" -> Command.Show
        else -> {
            val args = input.split(" ").map { it.trim() }
            when {
                args.size == 1 -> Command.AddName(args[0])
                args.size == 2 -> Command.AddPhone(args[0], args[1])
                args.size == 2 -> Command.AddEmail(args[0], args[2])
                else -> Command.Help
            }
        }
    }
}

fun isValid(command: Command): Boolean {
    return when (command) {
        is Command.AddPhone -> {
            val phone = command.phone
            phone.matches("\\+?\\d+".toRegex())
        }
        is Command.AddEmail -> {
            val email = command.email
            email.matches("[a-zA-Z]+@[a-zA-Z]+\\.[a-zA-Z.]+".toRegex())
        }
        else -> true
    }
}

data class Person(val name: String, var phone: String, var email: String)

fun main() {
    var person: Person? = null
    while (true) {
        val command = readCommand()
        when {
            command is Command.Exit -> break
            command is Command.Help -> {
                println("Доступные команды:")
                println("exit - завершение программы")
                println("help - вывод списка команд")
                println("введите имя, чтобы добавить новую строку")
                println("введите добавленное имя и номер телефона, чтобы добавить номер телефона")
                println("введите добавленное имя и email, чтобы добавить email")
                println("show - вывод последнего введенного значения")
            }
            command is Command.Show -> {
                person?.let {
                    println("Последнее значение: $it")
                } ?: println("Not initialized")
            }
            command is Command.AddName -> {
                person = Person(command.name, "", "")
                println("Добавлено имя: $command")
            }
            command is Command.AddPhone -> {
                if (isValid(command)) {
                    person?.let {
                        if (it.name == command.name) {
                            it.phone = command.phone
                            println("Добавлен номер телефона: $command")
                        } else {
                            println("Ошибка: такого имени не существует")
                        }
                    }
                } else {
                    println("Ошибка: номер телефона некорректен")
                }
            }
            command is Command.AddEmail -> {
                if (isValid(command)) {
                    person?.let {
                        if (it.name == command.name) {
                            it.email = command.email
                            println("Добавлен адрес электронной почты: $command")
                        } else {
                            println("Ошибка: такого имени не существует")
                        }
                    }
                } else {
                    println("Ошибка: адрес электронной почты некорректен")
                }
            }

        }
    }
}
*/