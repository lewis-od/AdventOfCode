package uk.co.lewisodriscoll.aoc.day11

import uk.co.lewisodriscoll.aoc.computer.Computer
import uk.co.lewisodriscoll.aoc.computer.Memory
import uk.co.lewisodriscoll.aoc.day3.Point
import uk.co.lewisodriscoll.aoc.day3.plus
import uk.co.lewisodriscoll.aoc.util.readProgramFromFile

typealias Vector = Point

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
            val instructions: List<Int> = computer.runUntilHalt().map(Long::toInt)
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

fun part1() {
    val robot = Robot(readProgramFromFile("day11.txt"))
    robot.run()
    println("Tiles painted: ${robot.paintedTiles.size}")
}

fun part2() {
    val robot = Robot(readProgramFromFile("day11.txt"))
    robot.paintedTiles[Point(0, 0)] = Colour.WHITE
    robot.run()
    val paintedTiles = robot.paintedTiles

    val whiteTiles = paintedTiles.filterValues { it == Colour.WHITE }.keys
    val minX = whiteTiles.map { it.first }.min()!!
    val minY = whiteTiles.map { it.second }.min()!!

    val transformed = whiteTiles.map { it + Point(-minX, -minY) }
    val maxX = transformed.map { it.first }.max()!!
    val maxY = transformed.map { it.second }.max()!!

    (0..maxY).reversed().forEach { y ->
        (0..maxX).forEach { x ->
            if (transformed.contains(Point(x, y))) print('#')
            else print(' ')
        }
        println()
    }
}

fun main() {
    part1()
    part2()
}
