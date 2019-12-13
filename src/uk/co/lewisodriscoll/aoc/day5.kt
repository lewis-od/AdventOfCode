package uk.co.lewisodriscoll.aoc

import uk.co.lewisodriscoll.aoc.computer.Memory
import uk.co.lewisodriscoll.aoc.computer.runProgram

fun main() {
    val program: Memory = readFile("day5.txt")[0].split(",")
        .map(String::toInt)
        .toMutableList()

    println("Part 1:")
    runProgram(program, listOf(1))
    println()
    println("Part 2:")
    runProgram(program, listOf(5))
}
