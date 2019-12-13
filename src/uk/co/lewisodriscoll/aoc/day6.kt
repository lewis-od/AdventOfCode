package uk.co.lewisodriscoll.aoc

class Satellite(val label: String, var parent: Satellite?) {
    val children: MutableList<Satellite> = mutableListOf()

    override fun toString(): String = when (parent) {
        null -> label
        else -> "$label->${parent!!.label}"
    }
}

class Map {
    private val map: MutableList<Satellite> = mutableListOf()

    private fun getByLabel(label: String): Satellite? = map.find { it.label == label }

    fun add(orbit: String) {
        val labels: List<String> = orbit.split(")")

        val root: Satellite = when (val existing = getByLabel(labels[0])) {
            null -> {
                val new: Satellite = Satellite(labels[0], null)
                map.add(new)
                new
            }
            else -> existing
        }

        when (val existing = getByLabel(labels[1])) {
            null -> {
                val new: Satellite = Satellite(labels[1], root)
                map.add(new)
            }
            else -> {
                existing.parent = root
            }
        }

    }

    override fun toString(): String {
        return map.joinToString("\n", transform = Satellite::toString)
    }
}

fun main() {
    val map = Map()

    val input = ("COM)B\n" +
            "B)C\n" +
            "C)D\n" +
            "D)E\n" +
            "E)F\n" +
            "B)G\n" +
            "G)H\n" +
            "D)I\n" +
            "E)J\n" +
            "J)K\n" +
            "K)L").split("\n").forEach { map.add(it) }

    println(map)
}
