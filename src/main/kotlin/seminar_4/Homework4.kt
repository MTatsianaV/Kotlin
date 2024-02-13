
sealed class Command {
    object Exit : Command()
    object Help : Command()
    data class AddName(val name: String) : Command()
    data class AddPhone(val name: String, val phone: String) : Command()
    data class AddEmail(val name: String, val email: String) : Command()
    data class Show(val name: String?) : Command()
    data class Find(val value: String) : Command()
    data class Export(val filePath: String) : Command()
}

interface Validatable {
    fun isValid(): Boolean
}

data class Person(val phones: List<String>, val emails: List<String>)

fun main() {
    val phoneBook = mutableMapOf<String, Person>()
    var command: Command

    do {
        command = readCommand()
        println(command)

        when (command) {
            is Command.Exit -> {
                println("Exiting...")
                return
            }
            is Command.Help -> println("Commands:\nexit\nhelp\nadd_name NAME\nadd_phone NAME PHONE\nadd_email NAME EMAIL\nshow [NAME]\nfind VALUE\nexport FILE_PATH")
            is Command.AddName -> {
                val person = phoneBook.getOrPut(command.name) { Person(emptyList(), emptyList()) }
                phoneBook[command.name] = person
            }
            is Command.AddPhone -> {
                if (phoneBook.containsKey(command.name)) {
                    val person = phoneBook[command.name]
                    val phones = person?.phones?.toMutableList() ?: mutableListOf()
                    phones.add(command.phone)
                    phoneBook[command.name] = person?.copy(phones = phones) ?: Person(listOf(command.phone), emptyList())
                } else {
                    println("Error: Name ${command.name} doesn't exist.")
                }
            }
            is Command.AddEmail -> {
                if (phoneBook.containsKey(command.name)) {
                    val person = phoneBook[command.name]
                    val emails = person?.emails?.toMutableList() ?: mutableListOf()
                    emails.add(command.email)
                    phoneBook[command.name] = person?.copy(emails = emails) ?: Person(emptyList(), listOf(command.email))
                } else {
                    println("Error: Name ${command.name} doesn't exist.")
                }
            }
            is Command.Show -> {
                if (phoneBook.isEmpty()) {
                    println("Not initialized")
                } else {
                    if (command.name != null && phoneBook.containsKey(command.name)) {
                        val person = phoneBook[command.name]!!
                        println("Name: ${command.name}\nPhones: ${person.phones.joinToString(", ")}\nEmails: ${person.emails.joinToString(", ")}")
                    } else {
                        println("Invalid name.")
                    }
                }
            }
            is Command.Find -> {
                val foundNames = phoneBook.filter { it.value.phones.contains(command.value) || it.value.emails.contains(command.value) }.keys
                if (foundNames.isNotEmpty()) {
                    println(foundNames.joinToString(", "))
                } else {
                    println("No matching names found.")
                }
            }
            is Command.Export -> {
                val json = buildJson {
                    array(phoneBook.values) {
                        obj {
                            "name" to it.key
                            "phones" to array(it.value.phones)
                            "emails" to array(it.value.emails)
                        }
                    }
                }
                File(command.filePath).writeText(json.toString())
                println("Data exported to file: ${command.filePath}")
            }
        }
    } while (true)
}

fun readCommand(): Command {
    print("> ")
    val input = readLine() ?: ""
    val parts = input.split(" ", limit = 2)
    val command = when (parts[0]) {
        "exit" -> Command.Exit
        "help" -> Command.Help
        "add_name" -> {
            val name = if (parts.size > 1) parts[1] else ""
            Command.AddName(name)
        }
        "add_phone" -> {
            val arguments = parts.getOrNull(1)?.split(" ") ?: emptyList()
            val name = arguments.getOrNull(0) ?: ""
            val phone = arguments.getOrNull(1) ?: ""
            Command.AddPhone(name, phone)
        }
        "add_email" -> {
            val arguments = parts.getOrNull(1)?.split(" ") ?: emptyList()
            val name = arguments.getOrNull(0) ?: ""
            val email = arguments.getOrNull(1) ?: ""
            Command.AddEmail(name, email)
        }
        "show" -> {
            val name = parts.getOrNull(1)
            Command.Show(name)
        }
        "find" -> {
            val value = parts.getOrNull(1) ?: ""
            Command.Find(value)
        }
        "export" -> {
            val filePath = parts.getOrNull(1) ?: ""
            Command.Export(filePath)
        }
        else -> Command.Help
    }
    return command
}

@DslMarker
annotation class JsonDslMarker

@JsonDslMarker
class JsonObjectBuilder {
    private val map = LinkedHashMap<String, Any?>()

    infix fun String.to(value: Any?) {
        map[this] = value
    }

    override fun toString(): String {
        val result = StringBuilder()
        result.append("{")
        for ((key, value) in map.entries) {
            result.append("\"$key\": ")
            when (value) {
                is String -> result.append("\"$value\"")
                is Int, is Long, is Float, is Double, is Boolean -> result.append(value)
                else -> result.append(value.toString())
            }
            result.append(", ")
        }
        if (map.isNotEmpty()) {
            result.delete(result.length - 2, result.length)
        }
        result.append("}")
        return result.toString()
    }
}

@JsonDslMarker
class JsonArrayBuilder {
    private val list = mutableListOf<Any?>()

    fun obj(init: JsonObjectBuilder.() -> Unit) {
        val builder = JsonObjectBuilder()
        builder.init()
        list.add(builder.toString())
    }

    infix fun array(values: Collection<Any?>) {
        list.add(values)
    }

    override fun toString(): String {
        val result = StringBuilder()
        result.append("[")
        for (item in list) {
            when (item) {
                is String -> result.append("\"$item\"")
                is List<*> -> result.append(item.joinToString(", "))
                else -> result.append(item.toString())
            }
            result.append(", ")
        }
        if (list.isNotEmpty()) {
            result.delete(result.length - 2, result.length)
        }
        result.append("]")
        return result.toString()
    }
}

@JsonDslMarker
fun buildJson(init: JsonArrayBuilder.() -> Unit): JsonArrayBuilder {
    val builder = JsonArrayBuilder()
    builder.init()
    return builder
}