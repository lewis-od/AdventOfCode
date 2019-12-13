package uk.co.lewisodriscoll.aoc.computer

typealias Memory = MutableList<Int>
typealias ProgramOutput = Pair<Memory, List<Int>>
fun ProgramOutput.getMemory() = this.first
fun ProgramOutput.getOutputs() = this.second

fun Int.digits(): List<Int> = this.toString().toList().map(Char::toString).map(String::toInt)
fun Int.getDigit(i: Int): Int = this.digits()
    .reversed()
    .getOrElse(i) { 0 }
fun Int.getTens() = 10 * this.getDigit(1) + this.getDigit(0)

fun Boolean.toInt(): Int = when (this) {
    true -> 1
    false -> 0
}

enum class OpCode {
    PLUS,
    TIMES,
    INPUT,
    OUTPUT,
    JUMP_TRUE,
    JUMP_FALSE,
    LESS_THAN,
    EQUALS,
    TERMINATE
}
enum class ParamMode { POSITION, IMMEDIATE }

abstract class Operation(protected val code: Int) {
    val opCode: OpCode = when (code.getTens()) {
        1 -> OpCode.PLUS
        2 -> OpCode.TIMES
        3 -> OpCode.INPUT
        4 -> OpCode.OUTPUT
        5 -> OpCode.JUMP_TRUE
        6 -> OpCode.JUMP_FALSE
        7 -> OpCode.LESS_THAN
        8 -> OpCode.EQUALS
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
        OpCode.INPUT -> listOf(ParamMode.IMMEDIATE)
        OpCode.OUTPUT -> listOf(intToParamMode(code.getDigit(2)))
        OpCode.TERMINATE -> listOf()
        else -> throw Exception("$opCode is not a unary operation")
    }
}

class JumpOperation(code: Int) : Operation(code) {
    override fun parseParamModes(): List<ParamMode> = (2..4)
        .map { intToParamMode(code.getDigit(it)) }
}

class TernaryOperation(code: Int) : Operation(code) {
    override fun parseParamModes(): List<ParamMode> = (2..3)
            .map { code.getDigit(it) }
            .map { intToParamMode(it) }
}

fun createOperation(code: Int): Operation = when(code.getTens()) {
    1, 2, 7, 8 -> TernaryOperation(code)
    5, 6 -> JumpOperation(code)
    3, 4, 99 -> UnaryOperation(code)
    else -> throw Exception("Op code not implemented: ${code.getTens()}")
}

class Computer(private val program: Memory, private val printOutput: Boolean = false) {
    private lateinit var memory: Memory
    private lateinit var outputs: MutableList<Int>
    private var instructionPointer: Int = 0;

    private fun resetMemory() {
        memory = mutableListOf<Int>().apply { addAll(program) }
        outputs = mutableListOf()
        instructionPointer = 0
    }

    private fun getValue(pointer: Int, mode: ParamMode): Int = when (mode) {
        ParamMode.POSITION -> memory[memory[pointer]]
        ParamMode.IMMEDIATE -> memory[pointer]
    }

    private fun performTernaryOperation(op: TernaryOperation) {
        val x: Int = getValue(instructionPointer + 1, op.paramModes[0])
        val y: Int = getValue(instructionPointer + 2, op.paramModes[1])
        val out: Int = memory[instructionPointer + 3]

        memory[out] = when (val opCode = op.opCode) {
            OpCode.PLUS -> x + y
            OpCode.TIMES -> x * y
            OpCode.LESS_THAN -> (x < y).toInt()
            OpCode.EQUALS -> (x == y).toInt()
            else -> throw Exception("$opCode is not a binary operation")
        }
    }

    private fun performJumpOperation(op: JumpOperation): Int {
        val arg: Int = getValue(instructionPointer + 1, op.paramModes[0])
        val target: Int = getValue(instructionPointer + 2, op.paramModes[1])

        val condition: Boolean = when (val opCode = op.opCode) {
            OpCode.JUMP_TRUE -> arg > 0
            OpCode.JUMP_FALSE -> arg == 0
            else -> throw Exception("$opCode is not a binary operation")
        }

        return if (condition) target else instructionPointer + 3
    }

    private fun performUnaryOperation(op: UnaryOperation, inputs: MutableList<Int>) {
        when (op.opCode) {
            OpCode.INPUT -> {
                val input: Int = inputs.removeAt(0)
                memory[memory[instructionPointer + 1]] = input
            }
            OpCode.OUTPUT -> {
                val output: Int = getValue(instructionPointer + 1, op.paramModes[0])
                outputs.add(output)
                if (printOutput) println("OUTPUT: $output")
            }
            else -> throw Exception("${op.opCode} not implemented")
        }
    }

    fun runProgram(inputsArgs: List<Int>): ProgramOutput {
        resetMemory()
        val inputs: MutableList<Int> = inputsArgs.toMutableList()

        var curOp: Operation = createOperation(memory[instructionPointer])
        while (curOp.opCode != OpCode.TERMINATE) {
            instructionPointer = when (curOp) {
                is TernaryOperation -> {
                    performTernaryOperation(curOp as TernaryOperation)
                    instructionPointer + 4
                }

                is JumpOperation -> performJumpOperation(curOp as JumpOperation)

                else -> {
                    performUnaryOperation(curOp as UnaryOperation, inputs)
                    instructionPointer + 2
                }
            }

            curOp = createOperation(memory[instructionPointer])
        }

        if (printOutput) println("TERMINATE")

        return ProgramOutput(memory, outputs)
    }

    fun runProgram(): ProgramOutput = runProgram(listOf())

}
