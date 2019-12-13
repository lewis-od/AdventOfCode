package uk.co.lewisodriscoll.aoc

class Satellite(val label: String, var parent: Satellite?) {
    val children: MutableList<Satellite> = mutableListOf()

    override fun toString(): String = when (parent) {
        null -> label
        else -> "$label->${parent!!.label}"
    }

    fun countOrbits(): Int {
        if (this.parent == null) return 0

        return 1 + this.parent!!.countOrbits()
    }
}

class Map {
    val satellites: MutableList<Satellite> = mutableListOf()

    private fun getByLabel(label: String): Satellite? = satellites.find { it.label == label }

    fun add(orbit: String) {
        val labels: List<String> = orbit.split(")")

        val root: Satellite = when (val existing = getByLabel(labels[0])) {
            null -> {
                val new: Satellite = Satellite(labels[0], null)
                satellites.add(new)
                new
            }
            else -> existing
        }

        when (val existing = getByLabel(labels[1])) {
            null -> {
                val new: Satellite = Satellite(labels[1], root)
                satellites.add(new)
            }
            else -> {
                existing.parent = root
            }
        }

    }

    override fun toString(): String {
        return satellites.joinToString("\n", transform = Satellite::toString)
    }
}

fun main() {
    val map = Map()

    val input = readFile("day6.txt").forEach { map.add(it) }

    val numOrbits: Int = map.satellites.map(Satellite::countOrbits).sum()
    println("Num orbits: $numOrbits")
}
