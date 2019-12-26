package uk.co.lewisodriscoll.aoc.day13

import uk.co.lewisodriscoll.aoc.computer.Computer
import uk.co.lewisodriscoll.aoc.util.readProgramFromFile

fun part1() {
    val computer = Computer(readProgramFromFile("day13.txt"))
    val outputs = computer.runProgram()
    val numBlocks = outputs
        .map(Long::toInt)
        .filterIndexed { index, _ -> (index + 1).rem(3) == 0 }
        .filter { it == 2 }
        .size
    println("Number of blocks drawn: $numBlocks")
}

fun printScreen(instructions: List<Int>) {
    val xs = instructions.filterIndexed { index, _ -> (index + 1).rem(3) == 1 }
    val ys = instructions.filterIndexed { index, _ -> (index + 1).rem(3) == 2 }
    val ids = instructions.filterIndexed { index, _ -> (index + 1).rem(3) == 0 }

    val maxX = xs.max()!!
    val maxY = ys.max()!!
    val screen = List<MutableList<Char>>(maxY + 1) {
        MutableList<Char>(maxX + 1) { ' ' }
    }

    xs.indices.forEach { i ->
        val x = xs[i]
        if (x != -1) {
            val y = ys[i]
            screen[y][x] = when(ids[i]) {
                0 -> ' '
                1 -> '|'
                2 -> '#'
                3 -> '_'
                4 -> 'O'
                else -> throw IllegalArgumentException("Invalid ID ${ids[i]}")
            }
        }
    }

    screen.forEach { row ->
        row.forEach {
            print(it)
        }
        println()
    }
}

fun getScore(instructions: List<Int>): Int? {
    val xs = instructions.filterIndexed { index, _ -> (index + 1).rem(3) == 1 }
    val scoreInstructionIndex = xs.indexOf(-1)
    if (scoreInstructionIndex == -1) return null
    return instructions.filterIndexed { index, _ -> (index + 1).rem(3) == 0 }[scoreInstructionIndex]
}

fun getX(instructions: List<Int>, id: Int): Int? {
    val ids = instructions.filterIndexed { index, _ -> (index + 1).rem(3) == 0 }
    val targetIndex = ids.indexOf(id)
    if (targetIndex == -1) return null
    return instructions.filterIndexed { index, _ -> (index + 1).rem(3) == 1 }[targetIndex]
}

fun getBallX(instructions: List<Int>): Int? = getX(instructions, 4)

fun getPaddleX(instructions: List<Int>): Int? = getX(instructions, 3)

fun part2() {
    val game = readProgramFromFile("day13.txt")
    game[0] = 2
    val computer = Computer(game)

    var score = 0
    var paddleX = 0
    var ballX = 0
    while (!computer.hasTerminated()) {
        val output = computer.runUntilHalt().map(Long::toInt)

        score = getScore(output) ?: score
        paddleX = getPaddleX(output) ?: paddleX
        ballX = getBallX(output) ?: ballX

        var input = 0
        if (ballX < paddleX) input = -1
        else if (ballX > paddleX) input = 1

        computer.input(input)
    }

    println("Score on completion: $score")
}

fun main() {
    part1()
    part2()
}
