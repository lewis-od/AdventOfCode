package uk.co.lewisodriscoll.aoc

import kotlin.math.abs

class Vec3(var x: Int, var y: Int, var z: Int) {

    fun absolute(): Int = abs(x) + abs(y) + abs(z)

    operator fun plus(v: Vec3) = Vec3(x + v.x, y + v.y, z + v.z)

    operator fun get(i: Int): Int = when (i) {
        0 -> x
        1 -> y
        2 -> z
        else -> throw IndexOutOfBoundsException("Expected $i <= 2")
    }

    operator fun set(i: Int, v: Int) = when (i) {
        0 -> x = v
        1 -> y = v
        2 -> z = v
        else -> throw IndexOutOfBoundsException("Expected $i <= 2")
    }

    override fun toString(): String {
        return "<x=$x, y=$y, z=$z>"
    }
}

class Moon(var position: Vec3) {
    constructor(x: Int, y: Int, z: Int) : this(Vec3(x, y, z))
    var velocity: Vec3 = Vec3(0, 0 ,0)

    fun getPotentialEnergy(): Int = position.absolute()

    fun getKineticEnergy(): Int = velocity.absolute()

    fun getTotalEnergy(): Int = getPotentialEnergy() * getKineticEnergy()

    override fun toString(): String = "position: $position, velocity: $velocity"
}

fun updateVelocities(moons: List<Moon>) {
    val n: Int = moons.size
    for (i in 0 until n) {
        for (j in (i + 1) until n) {
            val moonA = moons[i]
            val moonB = moons[j]
            for (k in 0..2) {
                val xa = moonA.position[k]
                val xb = moonB.position[k]

                if (xa < xb) {
                    moonA.velocity[k] += 1
                    moonB.velocity[k] -= 1
                } else if (xb < xa) {
                    moonA.velocity[k] -= 1
                    moonB.velocity[k] += 1
                }
            }
        }
    }
}

fun updatePositions(moons: List<Moon>) = moons.forEach { moon -> moon.position += moon.velocity }

fun main() {
    val io = Moon(13, -13, -2)
    val europa = Moon(16, 2, -15)
    val ganymede = Moon(7, -18, -12)
    val callisto = Moon(-3, -8, -8)
    val moons = listOf(io, europa, ganymede, callisto)

    (0 until 1000).forEach { _ ->
        updateVelocities(moons)
        updatePositions(moons)
    }

    val totalEnergy = moons.map(Moon::getTotalEnergy).sum()
    println("Total energy after 1000 steps: $totalEnergy")
}

