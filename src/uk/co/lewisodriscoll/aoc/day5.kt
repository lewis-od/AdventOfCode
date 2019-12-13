package uk.co.lewisodriscoll.aoc

import uk.co.lewisodriscoll.aoc.computer.Memory
import uk.co.lewisodriscoll.aoc.computer.runProgram
import uk.co.lewisodriscoll.aoc.util.readProgramFromFile

fun main() {
    val program: Memory = readProgramFromFile("day5.txt")

    println("Part 1:")
    runProgram(program, listOf(1))
    println()
    println("Part 2:")
    runProgram(program, listOf(5))
}
