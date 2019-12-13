package uk.co.lewisodriscoll.aoc

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

class Map {
    private val satellites: MutableList<Satellite> = mutableListOf()

    private fun findByLabel(label: String): Satellite? = satellites.find { it.label == label }

    fun getByLabel(label: String) = findByLabel(label)!!

    fun add(orbit: String) {
        val labels: List<String> = orbit.split(")")

        val root: Satellite = when (val existing = findByLabel(labels[0])) {
            null -> {
                val new: Satellite = Satellite(labels[0], null)
                satellites.add(new)
                new
            }
            else -> existing
        }

        when (val existing = findByLabel(labels[1])) {
            null -> {
                val new: Satellite = Satellite(labels[1], root)
                satellites.add(new)
            }
            else -> {
                existing.parent = root
            }
        }

    }

    fun countIndirectOrbits(): Int = satellites.map(Satellite::countOrbits).sum()

    fun jumpsBetween(a: Satellite, b: Satellite): Int {
        val aParents = a.indirectlyOrbits()
        val bParents = b.indirectlyOrbits()

        val commonAncestor: Satellite = aParents.first { bParents.contains(it) }
        val jumpsToA = aParents.indexOf(commonAncestor)
        val jumpsToB = bParents.indexOf(commonAncestor)

        return jumpsToA + jumpsToB
    }

}

fun main() {
    val map = Map()
    val input: List<String> = readFile("day6.txt")

    input.forEach { map.add(it) }
    val numOrbits: Int = map.countIndirectOrbits()
    println("Num indirect orbits: $numOrbits")

    val me: Satellite = map.getByLabel("YOU")
    val santa: Satellite = map.getByLabel("SAN")
    val numTransfers: Int = map.jumpsBetween(me, santa)
    println("Min num orbital transfers: $numTransfers")
}
