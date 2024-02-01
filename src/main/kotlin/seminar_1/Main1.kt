fun main(vararg args: String) {
    /**Написать простую программу, которая принимает в качестве аргументов два целых числа и выводит на экран их
    сумму. Всю программу можно написать внутри функции main.*/
    if (args.size >= 2){
        val a = args[0].toIntOrNull()
        val b = args[1].toIntOrNull()
        if (a!=null && b!=null){
            val sum = a + b
            println("sum = $sum")
            return
        }
    }
    println("Error")
}