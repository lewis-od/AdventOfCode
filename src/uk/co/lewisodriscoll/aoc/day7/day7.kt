package uk.co.lewisodriscoll.aoc.day7

import uk.co.lewisodriscoll.aoc.computer.Computer
import uk.co.lewisodriscoll.aoc.computer.Memory
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

fun runAmplifier(phase: Long, input: Long, computer: Computer): Long = computer
    .runProgram(listOf(phase.toInt(), input.toInt()))
    .first()

fun runChain(phases: List<Long>, computer: Computer): Long = phases.reversed().foldRight(0L) { phase, input ->
    runAmplifier(phase, input, computer)
}

fun runLoop(phases: List<Long>, computers: List<Computer>): Long {
    computers.forEachIndexed { i, computer -> computer.input(phases[i]) }

    var output: Long = 0
    while (!computers.last().hasTerminated()) {
        computers.forEach { computer ->
            computer.input(output)
            output = computer.runUntilHalt().last()
        }
    }

    return output
}

fun part1(program: Memory): Long  = (0..4).toList()
    .map(Int::toLong)
    .permutations()
    .map { runChain(it, Computer(program)) }
    .max()!!

fun part2(program: Memory) = (5..9).toList()
    .map(Int::toLong)
    .permutations()
    .map { runLoop(it, (5..9).map { Computer(program) }) }
    .max()!!

fun main() {
    val program: Memory = readProgramFromFile("day7.txt")

    val output1: Long = part1(program)
    println("Output signal (part 1): $output1")

    val output2: Long = part2(program)
    println("Output signal (part 2): $output2")
}
