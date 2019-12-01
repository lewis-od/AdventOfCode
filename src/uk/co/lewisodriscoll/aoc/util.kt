package uk.co.lewisodriscoll.aoc

import java.io.File

fun readFile(fileName: String): List<String> {
    val filePath: String = "resources/${fileName}"
    val file: File = File(filePath)
    return file.readLines()
}
