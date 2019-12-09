package uk.co.lewisodriscoll.aoc.computer

typealias Memory = MutableList<Int>

fun Int.digits(): List<Int> = this.toString().toList().map(Char::toString).map(String::toInt)
fun Int.getDigit(i: Int): Int = this.digits()
    .reversed()
    .getOrElse(i) { 0 }
fun Int.getTens() = 10 * this.getDigit(1) + this.getDigit(0)

enum class OpCode {
    PLUS,
    TIMES,
    STORE,
    OUTPUT,
    TERMINATE
}
enum class ParamMode { POSITION, IMMEDIATE }

abstract class Operation(protected val code: Int) {
    val opCode: OpCode = when (code.getTens()) {
        1 -> OpCode.PLUS
        2 -> OpCode.TIMES
        3 -> OpCode.STORE
        4 -> OpCode.OUTPUT
        99 -> OpCode.TERMINATE
        else -> throw Exception("Invalid opcode: $code")
    }

    val paramModes: List<ParamMode> = parseParamModes()

    abstract fun parseParamModes(): List<ParamMode>

    protected fun intToParamMode(value: Int): ParamMode = when (value) {
        0 -> ParamMode.POSITION
        1 -> ParamMode.IMMEDIATE
        else -> throw Exception("Invalid param mode: $value")
    }
}

class UnaryOperation(code: Int) : Operation(code) {
    override fun parseParamModes(): List<ParamMode> = when (opCode) {
        OpCode.STORE -> listOf(ParamMode.IMMEDIATE)
        OpCode.OUTPUT -> listOf(intToParamMode(code.getDigit(2)))
        else -> throw Exception("$opCode is not a unary operation")
    }
}

class BinaryOperation(code: Int) : Operation(code) {
    override fun parseParamModes(): List<ParamMode> = (2..3)
            .map { code.getDigit(it) }
            .map { intToParamMode(it) }
}

fun createOperation(code: Int): Operation = when(code.getTens()) {
    1, 2 -> BinaryOperation(code)
    3, 4, 99 -> UnaryOperation(code)
    else -> throw Exception("Invalid opcode: $code")
}

fun getValue(memory: Memory, pointer: Int, mode: ParamMode): Int = when (mode) {
    ParamMode.POSITION -> memory[memory[pointer]]
    ParamMode.IMMEDIATE -> memory[pointer]
}

fun performBinaryOperation(memory: Memory, instructionPointer: Int, op: BinaryOperation) {
    val x: Int = getValue(memory, instructionPointer + 1, op.paramModes[0])
    val y: Int = getValue(memory, instructionPointer + 2, op.paramModes[1])
    val out: Int = memory[instructionPointer + 3]

    memory[out] = when (val opCode = op.opCode) {
        OpCode.PLUS -> x + y
        OpCode.TIMES -> x * y
        else -> throw Exception("$opCode is not a binary operation")
    }
}

fun performUnaryOperation(memory: Memory, instructionPointer: Int, op: UnaryOperation, input: Int) {
    if (op.opCode == OpCode.STORE) {
        memory[memory[instructionPointer + 1]] = input
    } else {
        val output: Int = getValue(memory, instructionPointer + 1, op.paramModes[0])
        println("OUTPUT: $output")
    }
}

fun runProgram(program: Memory, inputs: List<Int>): Memory {
    val register: Memory = program.toMutableList()

    var instructionPointer: Int = 0;
    var inputCounter: Int = 0
    var curOp: Operation = createOperation(register[instructionPointer])
    while (curOp.opCode != OpCode.TERMINATE) {
        if (curOp is BinaryOperation) {
            performBinaryOperation(register, instructionPointer, curOp as BinaryOperation)
            instructionPointer += 4
        } else {
            performUnaryOperation(register, instructionPointer, curOp as UnaryOperation, inputs[inputCounter])
            if (curOp.opCode == OpCode.STORE) {
                inputCounter += 1
            }
            instructionPointer += 2
        }

        curOp = createOperation(register[instructionPointer])
    }

    return register
}

fun runProgram(program: Memory): Memory = runProgram(program, listOf())
