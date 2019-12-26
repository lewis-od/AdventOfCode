package uk.co.lewisodriscoll.aoc.day6

import uk.co.lewisodriscoll.aoc.util.readFile

class Satellite(val label: String, var parent: Satellite?) {

    fun countOrbits(): Int {
        if (this.parent == null) return 0

        return 1 + this.parent!!.countOrbits()
    }

    fun indirectlyOrbits(): Array<Satellite> {
        if (parent == null) return arrayOf()
        val parentsOrbits = parent!!.indirectlyOrbits()
        return arrayOf(parent!!, *parentsOrbits)
    }

}

class Map(orbits: List<String>) {
    private val satellites: Array<Satellite>

    init {
        satellites = orbits.foldRight(arrayOf()) { orbit, map -> addOrbit(orbit, map) }
    }

    private fun getByLabel(label: String) = findByLabel(label, satellites)!!

    private fun findByLabel(label: String, map: Array<Satellite>): Satellite? = map.find { it.label == label }

    private fun addOrbit(orbit: String, partialMap: Array<Satellite>): Array<Satellite> {
        var newMap: Array<Satellite> = partialMap.clone()

        val labels: List<String> = orbit.split(")")

        val root: Satellite = when (val existing = findByLabel(labels[0], partialMap)) {
            null -> {
                val newSatellite: Satellite =
                    Satellite(labels[0], null)
                newMap = arrayOf(newSatellite, *partialMap)
                newSatellite
            }
            else -> existing
        }

        when (val existing = findByLabel(labels[1], partialMap)) {
            null -> {
                val new: Satellite =
                    Satellite(labels[1], root)
                newMap = arrayOf(new, *newMap)
            }
            else -> {
                existing.parent = root
            }
        }

        return newMap
    }

    fun countIndirectOrbits(): Int = satellites.map(Satellite::countOrbits).sum()

    fun minJumpsBetween(a: String, b: String): Int {
        val aParents = getByLabel(a).indirectlyOrbits()
        val bParents = getByLabel(b).indirectlyOrbits()

        val commonAncestor: Satellite = aParents.first { bParents.contains(it) }
        val jumpsToA = aParents.indexOf(commonAncestor)
        val jumpsToB = bParents.indexOf(commonAncestor)

        return jumpsToA + jumpsToB
    }

}

fun main() {

    val input: List<String> = readFile("day6.txt")
    val map = Map(input)

    val numOrbits: Int = map.countIndirectOrbits()
    println("Num indirect orbits: $numOrbits")

    val numTransfers: Int = map.minJumpsBetween("YOU", "SAN")
    println("Min num orbital transfers: $numTransfers")
}
