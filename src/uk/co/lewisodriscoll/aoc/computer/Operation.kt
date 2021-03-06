package uk.co.lewisodriscoll.aoc.computer

enum class OpCode {
    PLUS,
    TIMES,
    INPUT,
    OUTPUT,
    JUMP_TRUE,
    JUMP_FALSE,
    LESS_THAN,
    EQUALS,
    SET_RELATIVE_BASE,
    TERMINATE
}
enum class ParamMode { POSITION, IMMEDIATE, RELATIVE }

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
        9 -> OpCode.SET_RELATIVE_BASE
        99 -> OpCode.TERMINATE
        else -> throw Exception("Invalid opcode: $code")
    }

    val paramModes: List<ParamMode> = parseParamModes()

    abstract fun parseParamModes(): List<ParamMode>

    protected fun intToParamMode(value: Int): ParamMode = when (value) {
        0 -> ParamMode.POSITION
        1 -> ParamMode.IMMEDIATE
        2 -> ParamMode.RELATIVE
        else -> throw Exception("Invalid param mode: $value")
    }

    public override fun toString(): String = "$opCode:$paramModes"
}

class UnaryOperation(code: Int) : Operation(code) {
    override fun parseParamModes(): List<ParamMode> = when (opCode) {
        OpCode.INPUT, OpCode.OUTPUT, OpCode.SET_RELATIVE_BASE -> listOf(intToParamMode(code.getDigit(2)))
        OpCode.TERMINATE -> listOf()
        else -> throw Exception("$opCode is not a unary operation")
    }
}

class JumpOperation(code: Int) : Operation(code) {
    override fun parseParamModes(): List<ParamMode> = (2..4)
        .map { intToParamMode(code.getDigit(it)) }
}

class TernaryOperation(code: Int) : Operation(code) {
    override fun parseParamModes(): List<ParamMode> = (2..4)
        .map { code.getDigit(it) }
        .map { intToParamMode(it) }
}

fun createOperation(code: Int): Operation = when(code.getTens()) {
    1, 2, 7, 8 -> TernaryOperation(code)
    5, 6 -> JumpOperation(code)
    3, 4, 9, 99 -> UnaryOperation(code)
    else -> throw Exception("Op code not implemented: ${code.getTens()}")
}