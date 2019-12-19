package uk.co.lewisodriscoll.aoc

import uk.co.lewisodriscoll.aoc.computer.getDigit
import uk.co.lewisodriscoll.aoc.util.readFile
import kotlin.math.absoluteValue

typealias Pattern = List<Pair<Int, Int>>

fun getPattern(index: Int): Pattern {
    val elements = listOf(0, 1, 0, -1)
    val pattern = mutableListOf<Pair<Int, Int>>()
    elements.forEach { element: Int ->
        pattern.add(Pair(element, index + 1))
    }

    return pattern
}

fun flattenPattern(pattern: Pattern): Sequence<Int> {
    val repetitions = pattern.first().second
    var out = -1
    var i = 0
    return generateSequence {
        i %= repetitions
        if (i == 0) {
            out *= -1
        }
        i++

        out
    }
}

fun isZeroIndex(index: Int, j: Int) = (j + 1).rem(2 * (index + 1)) <= index

fun phase(input: List<Int>): List<Int> = input.indices.map { index ->
    val pattern = getPattern(index)
    input.asSequence()
        .filterIndexed { j, _ -> !isZeroIndex(index, j) }
        .zip(flattenPattern(pattern))
        .map { it.first * it.second }
        .sum()
        .absoluteValue
        .getDigit(0)
}

fun part1(input: List<Int>): List<Int> {
    var output = input
    (1..100).forEach { _ ->
        output = phase(output)
    }
    return output.take(8)
}

// WARNING: Very very slow
fun part2(input: List<Int>): List<Int> {
    var output = (0..10000).flatMap { input }
    (1..100).forEach { _->
        output = phase(output)
    }
    val offset = input.take(8).mapIndexed { n, x -> 10.pow(n) * x }.sum()
    return output.subList(offset, offset + 8)
}

fun main() {
    val input = readFile("day16.txt").first().map(Char::toString).map(String::toInt)
    val solution1 = part1(input)
    println("Solution (part 1): $solution1")

    val solution2 = part2(input)
    println("Solution (part 1): $solution2")
}
