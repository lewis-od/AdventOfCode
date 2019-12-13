package uk.co.lewisodriscoll.aoc

import uk.co.lewisodriscoll.aoc.util.readFile

typealias Layer = List<List<Int>>
typealias Image = List<Layer>

fun toImage(input: String, width: Int, height: Int): Image = input
    .map(Char::toString)
    .map(String::toInt)
    .chunked(width * height)
    .map { it.chunked(width) }

fun countValues(layer: Layer, n: Int): Int = layer.map { row -> row.count { it == n } }.sum()

fun part1(image: Image): Int {
    val zeroCounts: List<Int> = image.map { countValues(it, 0) }
    val layerIndex: Int = zeroCounts.indexOf(zeroCounts.min())
    val winningLayer: Layer = image[layerIndex]
    return countValues(winningLayer, 1) * countValues(winningLayer, 2)
}

fun main() {
    val width: Int = 25
    val height: Int = 6

    val input: String = readFile("day8.txt").first()
    val image: Image = toImage(input, width, height)
    val part1Answer: Int = part1(image)
    println("Answer (part 1): $part1Answer")
}
