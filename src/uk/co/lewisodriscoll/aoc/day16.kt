package uk.co.lewisodriscoll.aoc

import uk.co.lewisodriscoll.aoc.computer.getDigit
import uk.co.lewisodriscoll.aoc.util.readFile
import kotlin.math.ceil
import kotlin.math.abs

fun getPattern(index: Int, length: Int): List<Int> {
    val elements = listOf(0, 1, 0, -1)
    val pattern = mutableListOf<Int>()
    elements.forEach { element: Int ->
        (0..index).forEach { _ -> pattern.add(element) }
    }

    val numReps: Int = ceil(length.toFloat() / pattern.size).toInt()

    return (0..numReps)
        .flatMap { _ -> pattern }
        .drop(1)
        .take(length)
}

fun phase(input: List<Int>): List<Int> = input.indices
    .map { index ->
        getPattern(index, input.size)
            .zip(input)
            .map { it.first * it.second }
            .sum()
    }
    .map { abs(it).getDigit(0) }

fun main() {
    val input = readFile("day16.txt").first().map(Char::toString).map(String::toInt)
    var output = input
    (1..100).forEach { _ ->
        output = phase(output)
    }
    println(output.take(8))
}
