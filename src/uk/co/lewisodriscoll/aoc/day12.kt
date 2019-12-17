package uk.co.lewisodriscoll.aoc

import uk.co.lewisodriscoll.aoc.util.readFile
import kotlin.math.abs

class Moon(val x: Int, val y: Int, val z: Int, val vx: Int, val vy: Int, val vz: Int) {
    constructor(x: Int, y: Int, z: Int) : this(x, y, z, 0, 0, 0)

    private fun getPotentialEnergy(): Int = abs(x) + abs(y) + abs(z)

    private fun getKineticEnergy(): Int = abs(vx) + abs(vy) + abs(vz)

    fun getTotalEnergy(): Int = getPotentialEnergy() * getKineticEnergy()

    override fun toString(): String = "position: ($x, $y, $z), velocity: ($vx, $vy, $vz)"
}

fun calculateVelocity(moon: Moon, other: Moon): Moon {
    var dvx = 0
    var dvy = 0
    var dvz = 0

    if (moon.x > other.x) {
        dvx -= 1
    } else if (moon.x < other.x) {
        dvx += 1
    }

    if (moon.y > other.y) {
        dvy -= 1
    } else if (moon.y < other.y) {
        dvy += 1
    }

    if (moon.z > other.z) {
        dvz -= 1
    } else if (moon.z < other.z) {
        dvz += 1
    }

    return Moon(moon.x, moon.y, moon.z, moon.vx + dvx, moon.vy + dvy, moon.vz + dvz)
}

fun updateVelocity(moon: Moon, others: List<Moon>): Moon {
    var output = moon
    others.forEach { output = calculateVelocity(output, it) }
    return output
}

fun updatePosition(moon: Moon): Moon =
    Moon(moon.x + moon.vx, moon.y + moon.vy, moon.z + moon.vz, moon.vx, moon.vy, moon.vz)

fun step(moons: List<Moon>): List<Moon> = moons
    .map { updateVelocity(it, moons) }
    .map { updatePosition(it) }

fun part1(moons: List<Moon>): Int = generateSequence(moons) { step(it) }
    .drop(1)
    .take(1000)
    .map{ it.map(Moon::getTotalEnergy).sum() }
    .last()

fun hashFromMoons(moons: List<Moon>, axis: Char): String = when (axis) {
    'x' -> moons.joinToString("||") { it.x.toString() + "|" + it.vx.toString() }
    'y' -> moons.joinToString("||") { it.z.toString() + "|" + it.vy.toString() }
    'z' -> moons.joinToString("||") { it.x.toString() + "|" + it.vz.toString() }
    else -> throw IllegalArgumentException("Invalid axis: $axis")
}

fun cycleLengthForAxis(moons: List<Moon>, axis: Char): Long {
    val initialState = hashFromMoons(moons, axis)

    return generateSequence(moons) { step(it) }
        .map { hashFromMoons(it, axis) }
        .drop(1)
        .takeWhile { it != initialState }
        .count() + 1
        .toLong()
}

fun cycleLengthForAxis2(moons: List<Moon>, axis: Char): Long {
    val prevStates = mutableListOf<String>()
    var prevMoons = moons
    var stateHash = hashFromMoons(moons, axis)
    while (!prevStates.contains(stateHash)) {
        prevStates.add(stateHash)
        prevMoons = step(prevMoons)
        stateHash = hashFromMoons(prevMoons, axis)
    }

    val prevIndex = prevStates.indexOf(stateHash)
    println("prev state: ${prevStates[prevIndex]}")
    println("current state: $stateHash")
    println(prevIndex + prevStates.size)

    return prevStates.size.toLong()
}

fun lcm(a: Long, b: Long): Long = a * b / gcd(a, b)

fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

fun part2(moons: List<Moon>): Long = listOf('x', 'y', 'z')
    .map { cycleLengthForAxis(moons, it) }
    .reduce { a, b -> lcm(a, b) }

fun moonFromString(str: String): Moon {
    val matches = Regex("\\<x=(-?\\d+),\\sy=(-?\\d+),\\sz=(-?\\d+)\\>").matchEntire(str)
    return Moon(matches!!.groupValues[1].toInt(), matches.groupValues[2].toInt(), matches.groupValues[3].toInt())
}

fun main() {
//    val moons = readFile("day12.txt").map { moonFromString(it) }
//
//    val totalEnergy = part1(moons)
//    println("Total energy after 1000 steps: $totalEnergy")

//    val testInput =
//        ("<x=-8, y=-10, z=0>\n" +
//                "<x=5, y=5, z=10>\n" +
//                "<x=2, y=-7, z=3>\n" +
//                "<x=9, y=-8, z=-3>").split("\n").map { moonFromString(it) }

    val testInput = ("<x=-1, y=0, z=2>\n" +
            "<x=2, y=-10, z=-7>\n" +
            "<x=4, y=-8, z=8>\n" +
            "<x=3, y=5, z=-1>").split("\n").map { moonFromString(it) }

    listOf('x', 'y', 'z').forEach { axis ->
        println("For $axis (old): ${cycleLengthForAxis(testInput, axis)}")
        println("For $axis (new): ${cycleLengthForAxis2(testInput, axis)}")
    }
    println(part2(testInput))
}

