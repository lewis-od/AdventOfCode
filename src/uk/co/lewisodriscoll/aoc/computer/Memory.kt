package uk.co.lewisodriscoll.aoc.computer

typealias Memory = MutableList<Int>
typealias ProgramOutput = Pair<Memory, List<Int>>
fun ProgramOutput.getMemory() = this.first
fun ProgramOutput.getOutputs() = this.second
