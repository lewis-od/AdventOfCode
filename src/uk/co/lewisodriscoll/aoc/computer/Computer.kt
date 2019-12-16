package uk.co.lewisodriscoll.aoc.computer

class Computer(private var program: Memory, private val printOutput: Boolean = false) {
    private lateinit var memory: Memory
    private lateinit var outputs: MutableList<Int>
    private lateinit var inputs: MutableList<Int>
    private lateinit var curOp: Operation
    private var instructionPointer: Int = 0;
    private var initialised: Boolean = false

    init {
        resetMemory()
    }

    private fun resetMemory() {
        memory = mutableListOf<Int>().apply { addAll(program) }
        outputs = mutableListOf()
        inputs = mutableListOf()
        instructionPointer = 0
        curOp = createOperation(memory[instructionPointer])
        initialised = true
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

    private fun performUnaryOperation(op: UnaryOperation) {
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

    private fun step() {
        instructionPointer = when (curOp) {
            is TernaryOperation -> {
                performTernaryOperation(curOp as TernaryOperation)
                instructionPointer + 4
            }

            is JumpOperation -> performJumpOperation(curOp as JumpOperation)

            else -> {
                performUnaryOperation(curOp as UnaryOperation)
                instructionPointer + 2
            }
        }

        curOp = createOperation(memory[instructionPointer])
    }

    private fun shouldHalt(): Boolean =
        (curOp.opCode == OpCode.TERMINATE) || (curOp.opCode == OpCode.INPUT && inputs.size == 0)

    fun hasTerminated(): Boolean = curOp.opCode == OpCode.TERMINATE

    fun input(x: Int) = inputs.add(x)

    fun runUntilHalt(): Memory {
        if (!initialised) resetMemory()

        while (!shouldHalt()) {
            step()
        }

        if (printOutput) {
            @Suppress("NON_EXHAUSTIVE_WHEN")
            when (curOp.opCode) {
                OpCode.INPUT -> println("AWAIT INPUT")
                OpCode.TERMINATE -> println("TERMINATE")
            }
        }

        return outputs
    }

    fun runProgram(inputsArgs: List<Int>, forceReset: Boolean = true): List<Int> {
        if (!initialised || forceReset) resetMemory()
        inputs = inputsArgs.toMutableList()

        runUntilHalt()

        return outputs
    }

    fun runProgram(forceReset: Boolean = true): List<Int> = runProgram(listOf(), forceReset)

    fun getMemory(): Memory = memory

    fun setProgram(newProgram: Memory) {
        program = mutableListOf<Int>().apply { addAll(newProgram) }
        resetMemory()
    }

}
