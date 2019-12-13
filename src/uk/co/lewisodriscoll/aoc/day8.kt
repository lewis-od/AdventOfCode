package uk.co.lewisodriscoll.aoc

import uk.co.lewisodriscoll.aoc.util.readFile

typealias Layer = List<List<Int>>
fun Layer.print() = forEach {
    it.forEach { pixel ->
        if (pixel == 1) print("#")
        else print(" ")
    }
    println()
}

typealias Image = List<Layer>

fun toImage(input: List<Int>, width: Int, height: Int): Image = input
    .chunked(width * height)
    .map { it.chunked(width) }

fun countValues(layer: Layer, n: Int): Int = layer.map { row -> row.count { it == n } }.sum()

fun part1(image: Image): Int {
    val zeroCounts: List<Int> = image.map { countValues(it, 0) }
    val layerIndex: Int = zeroCounts.indexOf(zeroCounts.min())
    val winningLayer: Layer = image[layerIndex]
    return countValues(winningLayer, 1) * countValues(winningLayer, 2)
}

fun part2(input: List<Int>, width: Int, height: Int): Layer {
    val layers = input.chunked(width * height)
    val output = MutableList(width * height) { 2 }
    layers.forEach { layer ->
        layer.forEachIndexed { index, pixel ->
            val current: Int = output[index]
            if (current == 2) output[index] = pixel
        }
    }

    return output.chunked(width)
}

fun main() {
    val width: Int = 25
    val height: Int = 6

    val input: List<Int> = readFile("day8.txt")
        .first()
        .map(Char::toString)
        .map(String::toInt)

    val image: Image = toImage(input, width, height)
    val part1Answer: Int = part1(image)
    println("Answer (part 1): $part1Answer")

    val outputImage = part2(input, width, height)
    outputImage.print()
}
