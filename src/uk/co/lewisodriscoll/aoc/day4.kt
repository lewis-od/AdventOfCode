package uk.co.lewisodriscoll.aoc

import kotlin.math.pow

fun Int.pow(n: Int): Int = this.toDouble().pow(n).toInt()
fun Int.digits(): List<Int> = this.toString().toList().map(Char::toString).map(String::toInt)

fun digitsToNumber(digits: List<Int>): Int = digits.reduceRightIndexed { n, x, acc -> acc + 10.pow(digits.size - 1 - n) * x }

fun contains2ConsecutiveDigits(digits: List<Int>): Boolean {
    var containsConsecutive: Boolean = false
    for (i in 1 until digits.size) {
        if (digits[i] == digits[i - 1]) {
            containsConsecutive = true
        }
    }
    return containsConsecutive
}

fun contains3ConsecuiveDigits(digits: List<Int>): Boolean {
//    var containsConsecutive: Boolean = false
//    for (i in 2 until digits.size) {
//        if ((digits[i] == digits[i - 1]) && (digits[i - 1] == digits[i - 2])) {
//            containsConsecutive = true
//        }
//    }
//    return containsConsecutive
    var containsConsecutive: Boolean = false
    for (i in 0 until digits.size - 4) {
        if (digits[i] == digits[i + 1]) {
            if (digits[i] != digits[i + 2]) continue
            if (digits[i] != digits[i + 3]) {
                containsConsecutive = true
            }
        }
    }
    return containsConsecutive
}

fun part1(searchSpace: List<Int>): List<Int> = searchSpace
    .map(Int::digits)
    .filter { digits: List<Int> -> digits.sorted() == digits }
    .filter { digits: List<Int> -> contains2ConsecutiveDigits(digits) }
    .map { digits: List<Int> -> digitsToNumber(digits) }

// Doesn't work - will filter out 111122 even though it's valid
// Answer is between 758 and 1262
fun part2(searchSpace: List<Int>): List<Int> = searchSpace
    .map(Int::digits)
    .filter { digits: List<Int> -> !contains3ConsecuiveDigits(digits) }
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
