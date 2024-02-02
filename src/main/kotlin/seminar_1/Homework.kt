
fun main() {
    /**
    Написать программу, которая обрабатывает введённые пользователем в консоль команды:
    • exit
    • help
    • add <Имя> phone <Номер телефона>
    • add <Имя> email <Адрес электронной почты>
    После выполнения команды, кроме команды exit, программа ждёт следующую команду.
    Имя – любое слово.
    Если введена команда с номером телефона, нужно проверить, что указанный телефон может начинаться с +,
    затем идут только цифры. При соответствии введённого номера этому условию – выводим его на экран вместе с именем,
    используя строковый шаблон. В противном случае - выводим сообщение об ошибке.
    Для команды с электронной почтой делаем то же самое, но с другим шаблоном – для простоты, адрес должен содержать
    три последовательности букв, разделённых символами @ и точкой.
     */

    val contacts = mutableMapOf<String, String>()

    while (true) {
        print("Введите одну из команд:\n" +
                "    • exit\n" +
                "    • help\n" +
                "    • add <Имя> phone <Номер телефона>\n" +
                "    • add <Имя> email <Адрес электронной почты>\n")


        val input = readLine()

        when {
            input == "exit" -> {
                println("Программа завершена.")
                break
            }
            input == "help" -> {
                printHelp()
            }
            input?.startsWith("add") == true -> {
                val commandParts = input.split(" ")
                if (commandParts.size != 4) {
                    println("Неверный формат команды.")
                    continue
                }

                val name = commandParts[1]
                val type = commandParts[2]
                val value = commandParts[3]

                when (type) {
                    "phone" -> {
                        if (isValidPhone(value)) {
                            contacts[name] = value
                            println("Контакт $name с номером телефона $value добавлен.")
                        } else {
                            println("Неверный формат номера телефона.")
                        }
                    }
                    "email" -> {
                        if (isValidEmail(value)) {
                            contacts[name] = value
                            println("Контакт $name с адресом электронной почты $value добавлен.")
                        } else {
                            println("Неверный формат адреса электронной почты.")
                        }
                    }
                    else -> {
                        println("Неверный тип данных.")
                    }
                }
            }
            else -> {
                println("Неверная команда.")
            }
        }
    }
}

fun printHelp() {
    println("Доступные команды:")
    println("exit - завершение программы")
    println("help - вывод списка команд")
    println("add <Имя> phone <Номер телефона> - добавление контакта с номером телефона")
    println("add <Имя> email <Адрес электронной почты> - добавление контакта с адресом электронной почты")
}

fun isValidPhone(phone: String): Boolean {
    return phone.matches(Regex("\\+\\d+"))
}

fun isValidEmail(email: String): Boolean {
    return email.matches(Regex("[a-zA-Z]+@[a-zA-Z]+.[a-zA-Z]"))
}
