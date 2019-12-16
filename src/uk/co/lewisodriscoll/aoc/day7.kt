package uk.co.lewisodriscoll.aoc

import uk.co.lewisodriscoll.aoc.computer.Computer
import uk.co.lewisodriscoll.aoc.computer.Memory
import uk.co.lewisodriscoll.aoc.computer.getOutputs
import uk.co.lewisodriscoll.aoc.util.readProgramFromFile

fun <T> List<T>.plusAt(index: Int, element: T): List<T> = when {
    index !in 0..size -> throw Error("Cannot put at index $index because size is $size")
    index == 0 -> listOf(element) + this
    index == size -> this + element
    else -> dropLast(size - index) + element + drop(index)
}

fun <T> List<T>.permutations(): Set<List<T>> = when {
    isEmpty() -> setOf()
    size == 1 -> setOf(listOf(get(0)))
    else -> {
        val element: T = get(0)
        drop(1).permutations()
            .flatMap { sublist -> (0..sublist.size).map { i -> sublist.plusAt(i, element) } }
            .toSet()
    }
}

fun runAmplifier(phase: Int, input: Int, computer: Computer): Int = computer
    .runProgram(listOf(phase, input))
    .first()

fun runChain(phases: List<Int>, computer: Computer) = phases.reversed().foldRight(0) { phase, input ->
    runAmplifier(phase, input, computer)
}

fun part1(computer: Computer): Int  = (0..4).toList()
    .permutations()
    .map { runChain(it, computer) }
    .max()!!

fun main() {
    val program: Memory = readProgramFromFile("day7.txt")
    val computer: Computer = Computer(program, printOutput = false)

    val output1: Int = part1(computer)
    println("Output signal (part 1): $output1")
}
