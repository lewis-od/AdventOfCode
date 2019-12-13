package uk.co.lewisodriscoll.aoc.computer

fun Int.digits(): List<Int> = this.toString().toList().map(Char::toString).map(String::toInt)
fun Int.getDigit(i: Int): Int = this.digits()
    .reversed()
    .getOrElse(i) { 0 }
fun Int.getTens() = 10 * this.getDigit(1) + this.getDigit(0)

fun Boolean.toInt(): Int = when (this) {
    true -> 1
    false -> 0
}
