package uk.co.lewisodriscoll.aoc

import kotlin.math.pow

fun Int.pow(n: Int): Int = this.toDouble().pow(n).toInt()
fun Int.digits(): List<Int> = this.toString().toList().map(Char::toString).map(String::toInt)

fun digitsToNumber(digits: List<Int>): Int = digits.reduceRightIndexed { n, x, acc -> acc + 10.pow(digits.size - 1 - n) * x }

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

fun contains2ConsecutiveDigits(digits: List<Int>): Boolean = countDigits(digits)
    .values
    .fold(false) { acc: Boolean, freq: Int ->
        acc || (freq >= 2)
    }

fun containsPairOfDigits(digits: List<Int>): Boolean {
    val frequencies: HashMap<Int, Int> = countDigits(digits)

    return frequencies.values
        .fold(false) { acc: Boolean, freq: Int ->
            acc || (freq == 2)
        }
}

fun part1(searchSpace: List<Int>): List<Int> = searchSpace
    .map(Int::digits)
    .filter { digits: List<Int> -> digits.sorted() == digits }
    .filter { digits: List<Int> -> contains2ConsecutiveDigits(digits) }
    .map { digits: List<Int> -> digitsToNumber(digits) }

fun part2(searchSpace: List<Int>): List<Int> = searchSpace
    .map(Int::digits)
    .filter { digits: List<Int> -> containsPairOfDigits(digits) }
    .map { digits: List<Int> -> digitsToNumber(digits) }

fun main() {
    val lowerBound: Int = 172930
    val upperBound: Int = 683082

    val searchSpace = (lowerBound..upperBound).toList()

    val validPasswords1: List<Int> = part1(searchSpace)
    println("Answer 1: ${validPasswords1.size}")

    val validPasswords2: List<Int> = part2(validPasswords1)
    println("Answer 2: ${validPasswords2.size}")
}
