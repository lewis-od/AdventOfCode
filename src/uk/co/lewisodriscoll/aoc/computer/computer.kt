package uk.co.lewisodriscoll.aoc.computer

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
