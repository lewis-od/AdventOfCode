package uk.co.lewisodriscoll.aoc.day13

import uk.co.lewisodriscoll.aoc.computer.Computer
import uk.co.lewisodriscoll.aoc.util.readProgramFromFile

fun part1() {
    val computer = Computer(readProgramFromFile("day13.txt"))
    val outputs = computer.runProgram()
    val numBlocks = outputs
        .map(Long::toInt)
        .filterIndexed { index, _ -> (index + 1).rem(3) == 0 }
        .filter { it == 2 }
        .size
    println(numBlocks)
}

fun main() {
    part1()
}
