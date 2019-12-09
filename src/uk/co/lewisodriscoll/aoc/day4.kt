package uk.co.lewisodriscoll.aoc

import kotlin.math.pow

fun Int.pow(n: Int): Int = this.toDouble().pow(n).toInt()
fun Int.digits(): List<Int> = this.toString().toList().map(Char::toString).map(String::toInt)

fun countDigits(digits: List<Int>): HashMap<Int, Int> {
    val frequencies: HashMap<Int, Int> = hashMapOf()
    for (digit: Int in digits) {
        if (frequencies[digit] != null) {
            frequencies[digit] = frequencies[digit]!! + 1
        } else {
            frequencies[digit] = 1
        }
    }

    return frequencies
}

fun part1(searchSpace: Iterable<Int>): Int = searchSpace
    .map(Int::digits)
    .filter { it.sorted() == it }
    .map { countDigits(it).values }
    .count { it.fold(false) { acc: Boolean, freq: Int -> acc || freq >= 2 } }

fun part2(searchSpace: Iterable<Int>): Int = searchSpace
    .map(Int::digits)
    .filter { it.sorted() == it }
    .map { countDigits(it).values }
    .count { it.fold(false) { acc: Boolean, freq: Int -> acc || freq == 2 } }

fun main() {
    val lowerBound: Int = 172930
    val upperBound: Int = 683082

    val searchSpace = lowerBound..upperBound

    val answer1: Int = part1(searchSpace)
    println("Answer 1: $answer1")

    val answer2: Int = part2(searchSpace)
    println("Answer 2: $answer2")
}
