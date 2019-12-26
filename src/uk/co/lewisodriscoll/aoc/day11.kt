package uk.co.lewisodriscoll.aoc

import uk.co.lewisodriscoll.aoc.computer.Computer
import uk.co.lewisodriscoll.aoc.computer.Memory
import uk.co.lewisodriscoll.aoc.util.readProgramFromFile

typealias Vector<T> = Pair<T, T>

enum class Colour {
    WHITE, BLACK
}

class Robot(private val program: Memory) {
    private val computer = Computer(program)
    private var position = Vector(0, 0)
    private var direction = 0
    val paintedTiles = hashMapOf<Point, Colour>()

    fun run() {
        while (!computer.hasTerminated()) {
            val input = when (readColour()) {
                Colour.BLACK -> 0
                Colour.WHITE -> 1
            }
            computer.input(input)
            val instructions: List<Int> = computer.runUntilHalt().map(Long::toInt).takeLast(2)
            paint(instructions[0])
            turn(instructions[1])
            move()
        }
    }

    private fun turn(code: Int) {
        direction = when (code) {
            0 -> direction - 90
            1 -> direction + 90
            else -> throw IllegalArgumentException("turn expected 0 or 1, got $code")
        }.rem(360)
    }

    private fun move() {
        position += when (direction) {
            0 -> Vector(0, 1)
            90, -270 -> Vector(1, 0)
            180, -180 -> Vector(0, -1)
            270, -90 -> Vector(-1, 0)
            else -> throw IllegalArgumentException("Invalid direction $direction")
        }
    }

    private fun paint(code: Int) {
        paintedTiles[position] = when (code) {
            0 -> Colour.BLACK
            1 -> Colour.WHITE
            else -> throw IllegalArgumentException("Invalid colour code $code")
        }
    }

    private fun readColour(): Colour = paintedTiles[position] ?: Colour.BLACK

}

fun main() {
    val robot = Robot(readProgramFromFile("day11.txt"))
    robot.run()
    println(robot.paintedTiles.size)
}
