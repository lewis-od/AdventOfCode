package uk.co.lewisodriscoll.aoc

typealias Memory = MutableList<Int>

fun performInstructionAt(pointer: Int, memory: Memory) {
    val opCode: Int = memory[pointer];

    val operation: (Int, Int) -> Int = when (opCode) {
        1 -> Int::plus
        2 -> Int::times
        else -> throw Exception("Invalid opcode: $opCode")
    }
    val i: Int = memory[pointer + 1]
    val j: Int = memory[pointer + 2]
    val k: Int = memory[pointer + 3]

    memory[k] = operation(memory[i], memory[j])
}

fun runProgram(program: Memory): Memory {
    val register: Memory = program.toMutableList()

    var instructionPointer: Int = 0;
    while (register[instructionPointer] != 99) {
        performInstructionAt(instructionPointer, register)
        instructionPointer += 4
    }

    return register
}

fun main() {
    val program: Memory = readFile("day2.txt")[0].split(",")
            .map(String::toInt)
            .toMutableList()
    program[1] = 12
    program[2] = 2

    val result: Memory = runProgram(program)
    println("Answer (part 1): ${result[0]}")

    for (noun in 1..99) {
        for (verb in 1..99) {
            program[1] = noun
            program[2] = verb

            val output = runProgram(program)[0]
            if (output == 19690720) {
                val answer: Int = 100 * noun + verb
                println("Answer (part 2): $answer")
                return
            }
        }
    }

    println("Didn't find answer")
}
