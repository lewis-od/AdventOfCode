package uk.co.lewisodriscoll.aoc

import uk.co.lewisodriscoll.aoc.computer.Computer
import uk.co.lewisodriscoll.aoc.computer.Memory
import uk.co.lewisodriscoll.aoc.computer.getMemory
import uk.co.lewisodriscoll.aoc.util.readProgramFromFile

fun main() {
    val program: Memory = readProgramFromFile("day2.txt")
    program[1] = 12
    program[2] = 2

    val result: Memory = Computer(program).runProgram().getMemory()
    println("Answer (part 1): ${result[0]}")

    for (noun in 1..99) {
        for (verb in 1..99) {
            program[1] = noun
            program[2] = verb

            val output = Computer(program).runProgram().getMemory()[0]
            if (output == 19690720) {
                val answer: Int = 100 * noun + verb
                println("Answer (part 2): $answer")
                return
            }
        }
    }

    println("Didn't find answer")
}
