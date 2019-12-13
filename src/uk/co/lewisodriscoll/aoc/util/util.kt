package uk.co.lewisodriscoll.aoc.util

import uk.co.lewisodriscoll.aoc.computer.Memory
import java.io.File

fun readFile(fileName: String): List<String> = File("resources/${fileName}").readLines()

fun readProgramFromFile(filename: String): Memory = readFile(filename)
    .first()
    .split(",")
    .map(String::toInt)
    .toMutableList()
