package uk.co.lewisodriscoll.aoc

import uk.co.lewisodriscoll.aoc.computer.Computer
import uk.co.lewisodriscoll.aoc.computer.Memory
import uk.co.lewisodriscoll.aoc.util.readProgramFromFile

fun main() {
    val program: Memory = readProgramFromFile("day2.txt")
    program[1] = 12L
    program[2] = 2L

    val computer: Computer = Computer(program)
    computer.runProgram()

    val result: Memory = computer.getMemory()
    println("Answer (part 1): ${result[0]}")

    for (noun in 1..99) {
        for (verb in 1..99) {
            program[1] = noun.toLong()
            program[2] = verb.toLong()

            computer.setProgram(program)
            computer.runProgram()
            val output = computer.getMemory()[0]
            if (output == 19690720L) {
                val answer: Int = 100 * noun + verb
                println("Answer (part 2): $answer")
                return
            }
        }
    }

    println("Didn't find answer")
}
