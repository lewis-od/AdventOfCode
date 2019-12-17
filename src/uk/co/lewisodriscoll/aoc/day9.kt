package uk.co.lewisodriscoll.aoc

import uk.co.lewisodriscoll.aoc.computer.Computer
import uk.co.lewisodriscoll.aoc.computer.Memory
import uk.co.lewisodriscoll.aoc.util.readProgramFromFile

fun main() {
    val program: Memory = readProgramFromFile("day9.txt")

    val computer: Computer = Computer(program, printOutput = true)
    val output: Memory = computer.runProgram(listOf(1))
    println(output)
}
