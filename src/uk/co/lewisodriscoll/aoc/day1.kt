package uk.co.lewisodriscoll.aoc

import kotlin.math.floor

fun calcFuelForMass(mass: Int): Int {
    return floor(mass.toDouble() / 3.0f).toInt() - 2;
}

fun calcRequiredFuel(mass: Int): Int {
    val massOfFuel: Int = calcFuelForMass(mass);

    if (massOfFuel <= 0) return 0

    return massOfFuel + calcRequiredFuel(massOfFuel);
}

fun main() {
    val inputs: List<Int> = readFile("day1.txt").map(String::toInt)

    val answerPart1: Int = inputs
        .map { mass: Int -> calcFuelForMass(mass) }
        .sum()
    println("Total fuel required (part 1): $answerPart1")

    val answerPart2: Int = inputs
        .map { mass: Int -> calcRequiredFuel(mass) }
        .sum()
    println("Total fuel required (part 2): $answerPart2")
}
