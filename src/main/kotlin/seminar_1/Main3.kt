fun main(vararg args: String) {
    /**
     Написать программу, выводящую на экран фигуру из звёздочек.
     a – количество звёздочек на первой строчке;
     b – количество строк от первой до центральной и от центральной до последней;
     c – количество звёздочек, на которое увеличивается последовательность с каждой строкой.
     */

    val a = 1
    val b = 2
    val c = 4
    printStars(a, b, c)
}
private fun printStars (a: Int, b: Int, c: Int){
    var stars = ""
    val sym = "*"
    var starCount = a
    for (i in 0..2*b) {
        stars += "${sym.repeat(starCount)}\n"
        starCount = if (i<b) (starCount+c) else(starCount-c)
    }
    println(stars)
}