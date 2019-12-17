package uk.co.lewisodriscoll.aoc

import uk.co.lewisodriscoll.aoc.computer.Computer
import uk.co.lewisodriscoll.aoc.computer.Memory
import uk.co.lewisodriscoll.aoc.util.readProgramFromFile

fun main() {
    val program: Memory = readProgramFromFile("day9.txt")

    val computer: Computer = Computer(program)
    val keycode: Memory = computer.runProgram(listOf(1))
    println("BOOST keycode: ${keycode.first()}")

    val coordinates: Memory = computer.runProgram(listOf(2), forceReset = true)
    println("Coordinates: ${coordinates.first()}")
}
