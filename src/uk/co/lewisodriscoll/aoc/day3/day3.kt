package uk.co.lewisodriscoll.aoc.day3

import uk.co.lewisodriscoll.aoc.util.readFile
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

typealias Point = Pair<Int, Int>
operator fun Point.plus(x: Point): Point = Point(this.first + x.first, this.second + x.second)
fun Point.distance(): Int = abs(this.first) + abs(this.second)
fun Point.distance(p: Point): Int = abs(this.x - p.x) + abs(this.y - p.y)
val Point.x: Int
    get() = this.first
val Point.y: Int
    get() = this.second

typealias Line = Pair<Point, Point>
fun Line.getXRange(): Pair<Int, Int> = Pair(min(this.first.x, this.second.x), max(this.first.x, this.second.x))
fun Line.getYRange(): Pair<Int, Int> = Pair(min(this.first.y, this.second.y), max(this.first.y, this.second.y))

enum class Direction { L, R, U, D }
typealias Displacement = Pair<Direction, Int>
fun Displacement.toPoint(): Point = when(this.first) {
    Direction.L -> Point(-this.second, 0)
    Direction.R -> Point(this.second, 0)
    Direction.U -> Point(0, this.second)
    Direction.D -> Point(0, -this.second)
}
fun Displacement.applyTo(x: Point): Point = x + this.toPoint()


fun toDisplacement(input: String): Displacement {
    val direction: Direction = when(val firstChar = input.substring(0, 1)) {
        "L" -> Direction.L
        "R" -> Direction.R
        "U" -> Direction.U
        "D" -> Direction.D
        else -> throw IllegalArgumentException("Invalid direction: $firstChar")
    }

    val distance: Int = input.substring(1).toInt()

    return Displacement(direction, distance)
}

fun createWire(displacements: List<Displacement>): List<Point> {
    val wire: MutableList<Point> = mutableListOf(Point(0, 0))
    for (i in displacements.indices) {
        wire.add(displacements[i].applyTo(wire[i]))
    }

    return wire.toList()
}

fun isHorizontal(line: Line): Boolean = line.first.y == line.second.y
fun isVertical(line: Line): Boolean = !isHorizontal(line)

fun findIntersection(horizontal: Line, vertical: Line): Point? {
    val xRange: Pair<Int, Int> = horizontal.getXRange()
    if (vertical.first.x < xRange.first || vertical.first.x > xRange.second) {
        return null
    }
    val yRange: Pair<Int, Int> = vertical.getYRange()
    if (horizontal.first.y < yRange.first || horizontal.first.y > yRange.second) {
        return null
    }

    return Point(vertical.first.x, horizontal.first.y)
}

fun stepsToPoint(point: Point, displacements: List<Displacement>): Int {
    val stepsToEndOfWire: Int = displacements
        .map(Displacement::toPoint)
        .map(Point::distance)
        .sum()
    val endOfWire: Point = displacements.fold(Point(0, 0)) { x: Point, d: Displacement ->
        d.applyTo(x)
    }
    val dx: Int = endOfWire.distance(point)

    return stepsToEndOfWire + dx
}

fun part1(wire1Displacements: List<Displacement>, wire2Displacements: List<Displacement>): Point? {
    val wire1: List<Point> = createWire(wire1Displacements)
    val wire2: List<Point> = createWire(wire2Displacements)

    var closestIntersection: Point? = null
    for (i in 0 until wire1.size - 1) {
        for (j in 0 until wire2.size - 1) {
            val line1: Line = Line(wire1[i], wire1[i + 1])
            val line2: Line = Line(wire2[j], wire2[j + 1])

            if ((isHorizontal(line1) && isHorizontal(line2)) || (isVertical(line1) && isVertical(line2))) {
                continue
            }

            val intersection: Point? = if (isHorizontal(line1)) findIntersection(line1, line2) else findIntersection(line2, line1)

            if (intersection == null || (intersection.x == 0 && intersection.y == 0)) continue

            if (closestIntersection == null || intersection.distance() < closestIntersection.distance()) {
                closestIntersection = intersection
            }
        }
    }

    return closestIntersection
}

fun part2(wire1Displacements: List<Displacement>, wire2Displacements: List<Displacement>): Pair<Point, Int>? {
    val wire1: List<Point> = createWire(wire1Displacements)
    val wire2: List<Point> = createWire(wire2Displacements)

    var leastStepsIntersection: Point? = null
    var leastNumberSteps: Int? = null
    for (i in 0 until wire1.size - 1) {
        for (j in 0 until wire2.size - 1) {
            val line1: Line = Line(wire1[i], wire1[i + 1])
            val line2: Line = Line(wire2[j], wire2[j + 1])

            if ((isHorizontal(line1) && isHorizontal(line2)) || (isVertical(line1) && isVertical(line2))) {
                continue
            }

            val intersection: Point? = if (isHorizontal(line1)) findIntersection(line1, line2) else findIntersection(line2, line1)

            if (intersection == null || (intersection.x == 0 && intersection.y == 0)) continue

            val wire1Distance: Int = stepsToPoint(intersection, wire1Displacements.subList(0, i))
            val wire2Distance: Int = stepsToPoint(intersection, wire2Displacements.subList(0, j))

            val totalSteps: Int = wire1Distance + wire2Distance

            if (leastNumberSteps == null || totalSteps < leastNumberSteps) {
                leastNumberSteps = totalSteps
                leastStepsIntersection = intersection
            }
        }
    }

    if (leastStepsIntersection != null && leastNumberSteps != null) {
        return Pair(leastStepsIntersection, leastNumberSteps)
    }

    return null
}

fun main() {
    val input: List<List<String>> = readFile("day3.txt").map { line: String -> line.split(",") }

    val wire1Displacements: List<Displacement> = input[0].map { s: String -> toDisplacement(s) }
    val wire2Displacements: List<Displacement> = input[1].map { s: String -> toDisplacement(s) }

    val wire1: List<Point> = createWire(wire1Displacements)
    val wire2: List<Point> = createWire(wire2Displacements)

    val closestIntersection: Point? = part1(wire1Displacements, wire2Displacements)
    if (closestIntersection != null) {
        println("Intersection at: $closestIntersection")
        println("Distance: ${closestIntersection.distance()}")
    } else {
        println("No intersection found")
    }

    val leastStepsAnswer: Pair<Point, Int>? = part2(wire1Displacements, wire2Displacements)
    if (leastStepsAnswer != null) {
        println("Least steps intersection at ${leastStepsAnswer.first} in ${leastStepsAnswer.second} steps")
    } else {
        println("No least steps intersection found")
    }
}
