package uk.co.lewisodriscoll.aoc.computer

class Computer(private var program: Memory, private val printOutput: Boolean = false) {
    private lateinit var memory: Memory
    private lateinit var outputs: Memory
    private lateinit var inputs: Memory
    private lateinit var curOp: Operation
    private var instructionPointer: Int = 0
    private var relativeBase: Int = 0

    init {
        resetMemory()
    }

    private fun resetMemory() {
        memory = mutableListOf<Long>().apply { addAll(program) }
        val zeros = generateSequence(0L) { _ -> 0L }.take(1000)
        memory.addAll(zeros)

        outputs = mutableListOf()
        inputs = mutableListOf()
        instructionPointer = 0
        relativeBase = 0
        curOp = createOperation(memory[instructionPointer].toInt())
    }

    private fun getValue(pointer: Int, mode: ParamMode): Long = when (mode) {
        ParamMode.POSITION -> memory[memory[pointer].toInt()]
        ParamMode.RELATIVE -> memory[memory[pointer].toInt() + relativeBase]
        ParamMode.IMMEDIATE -> memory[pointer]
    }

    private fun performTernaryOperation(op: TernaryOperation) {
        val x: Long = getValue(instructionPointer + 1, op.paramModes[0])
        val y: Long = getValue(instructionPointer + 2, op.paramModes[1])
        val out: Int = when (val outputMode = op.paramModes[2]) {
            ParamMode.RELATIVE -> memory[instructionPointer + 3].toInt() + relativeBase
            ParamMode.POSITION -> memory[instructionPointer + 3].toInt()
            else -> throw IllegalStateException("Invalid param mode $outputMode for op ${op.opCode}")
        }

        memory[out] = when (val opCode = op.opCode) {
            OpCode.PLUS -> x + y
            OpCode.TIMES -> x * y
            OpCode.LESS_THAN -> (x < y).toLong()
            OpCode.EQUALS -> (x == y).toLong()
            else -> throw Exception("$opCode is not a binary operation")
        }
    }

    private fun performJumpOperation(op: JumpOperation): Int {
        val arg: Long = getValue(instructionPointer + 1, op.paramModes[0])
        val target: Long = getValue(instructionPointer + 2, op.paramModes[1])

        val condition: Boolean = when (val opCode = op.opCode) {
            OpCode.JUMP_TRUE -> arg > 0L
            OpCode.JUMP_FALSE -> arg == 0L
            else -> throw Exception("$opCode is not a binary operation")
        }

        return if (condition) target.toInt() else instructionPointer + 3
    }

    private fun performUnaryOperation(op: UnaryOperation) {
        when (op.opCode) {
            OpCode.INPUT -> {
                val input: Long = inputs.removeAt(0)
                when (op.paramModes.first()) {
                    ParamMode.RELATIVE -> memory[memory[instructionPointer + 1].toInt() + relativeBase] = input
                    else -> memory[memory[instructionPointer + 1].toInt()] = input
                }

            }
            OpCode.OUTPUT -> {
                val output: Long = getValue(instructionPointer + 1, op.paramModes[0])
                outputs.add(output)
                if (printOutput) println("OUTPUT: $output")
            }
            OpCode.SET_RELATIVE_BASE -> {
                relativeBase += getValue(instructionPointer + 1, op.paramModes[0]).toInt()
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

        curOp = createOperation(memory[instructionPointer].toInt())
    }

    private fun shouldHalt(): Boolean =
        (curOp.opCode == OpCode.TERMINATE) || (curOp.opCode == OpCode.INPUT && inputs.size == 0)

    fun hasTerminated(): Boolean = curOp.opCode == OpCode.TERMINATE

    fun input(x: Long) = inputs.add(x)

    fun input(x: Int) = input(x.toLong())

    fun runUntilHalt(): Memory {
        outputs = mutableListOf()

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

    fun runProgram(inputArgs: List<Int>, forceReset: Boolean = true): Memory {
        if (forceReset) resetMemory()
        inputs = mutableListOf<Long>().apply { addAll(inputArgs.map(Int::toLong)) }

        runUntilHalt()

        return outputs
    }

    fun runProgram(forceReset: Boolean = true): Memory = runProgram(listOf(), forceReset)

    fun getMemory(): Memory = memory

    fun setProgram(newProgram: Memory) {
        program = mutableListOf<Long>().apply { addAll(newProgram) }
        resetMemory()
    }

}
