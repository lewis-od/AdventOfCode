package uk.co.lewisodriscoll.aoc

typealias Memory = MutableList<Int>

fun runProgram(program: Memory): Memory {
    val register: Memory = program.toMutableList()

    var instructionPointer: Int = 0;
    while (true) {
        val opCode: Int = register[instructionPointer]

        if (opCode == 99) {
            break
        }

        val i: Int = register[instructionPointer + 1]
        val j: Int = register[instructionPointer + 2]
        val k: Int = register[instructionPointer + 3]

        if (opCode == 1) {
            register[k] = register[i] + register[j]
        } else if (opCode == 2) {
            register[k] = register[i] * register[j]
        }

        instructionPointer += 4
    }

    return register
}

fun main() {
    val program: Memory = readFile("day2.txt")[0].split(",").map(String::toInt).toMutableList()
    program[1] = 12
    program[2] = 2

    val result: Memory = runProgram(program)

    println("Answer (part 1): ${result[0]}")

    var answer: Int? = null
    for (noun in 1..99) {
        for (verb in 1..99) {
            program[1] = noun
            program[2] = verb

            val output = runProgram(program)[0]
            if (output == 19690720) {
                answer = 100 * noun + verb
            }
        }
    }

    if (answer != null) {
        println("Answer (part 2): $answer")
    } else {
        println("Didn't find answer")
    }

}
