package uk.co.lewisodriscoll.aoc

import uk.co.lewisodriscoll.aoc.computer.Computer
import uk.co.lewisodriscoll.aoc.computer.Memory
import uk.co.lewisodriscoll.aoc.util.readProgramFromFile

fun main() {
    val program: Memory = readProgramFromFile("day5.txt")
    val computer = Computer(program, printOutput = true)

    println("Part 1:")
    computer.runProgram(listOf(1))
    println()
    println("Part 2:")
    computer.runProgram(listOf(5))
}
