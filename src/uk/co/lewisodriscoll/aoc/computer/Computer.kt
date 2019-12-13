package uk.co.lewisodriscoll.aoc.computer

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
