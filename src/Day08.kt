import kotlin.math.sqrt

fun main() {
    fun part1(input: List<String>): Int {
        val input = parseInput(input)
        return antinodeCount(input, part2 = false)
    }

    fun part2(input: List<String>): Int {
        val input = parseInput(input)
        return antinodeCount(input, part2 = true)
    }

    // Test if implementation meets criteria from the description, like:
    val sampleInput = readInput("Day08_sample")
    checkInt(part1(sampleInput), 14)
    checkInt(part2(sampleInput), 34)

    // Read the input from the `src/Day08.txt` file.
    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}

private fun parseInput(input: List<String>): List<List<Char>> {
    return input.map {
        it.map { it }
    }
}

fun antinodeCount(input: List<List<Char>>, part2: Boolean): Int {
    val letters = mutableMapOf<Char, MutableList<Pair<Int, Int>>>()
    input.forEachIndexed { y, chars ->
        chars.forEachIndexed { x, c ->
            if (c != '.') {
                if (letters[c] == null) {
                    letters[c] = mutableListOf(x to y)
                } else {
                    letters[c]!!.add(x to y)
                }
            }
        }
    }

    val antinodes = mutableSetOf<Pair<Int, Int>>()
    letters.keys.forEach { letter ->
        val points = letters[letter]!!
        val pointCombinations = points.allPairs()
        pointCombinations.forEach { (p1, p2) ->
            val a = if (part2) interpolateInRange(p1, p2, input) else simpleInterpolate(p1, p2).filter { input.pointInRange(it) }
            antinodes.addAll(a)
        }

        if (part2 && points.size > 1) {
            antinodes.addAll(points)
        }
    }

    return antinodes.size
}

fun simpleInterpolate(p1: Pair<Int, Int>, p2: Pair<Int, Int>): List<Pair<Int, Int>> {
    val x1 = if (p1.first < p2.first) p1.first else p2.first
    val y1 = if (p1.first < p2.first) p1.second else p2.second
    val x2 = if (p1.first < p2.first) p2.first else p1.first
    val y2 = if (p1.first < p2.first) p2.second else p1.second

    val dx = x2 - x1
    val dy = y2 - y1
    return listOf(x1 - dx to y1 - dy, x2 + dx to y2 + dy)
}

fun interpolateInRange(p1: Pair<Int, Int>, p2: Pair<Int, Int>, input: List<List<Char>>): List<Pair<Int, Int>> {
    val x1 = if (p1.first < p2.first) p1.first else p2.first
    val y1 = if (p1.first < p2.first) p1.second else p2.second
    val x2 = if (p1.first < p2.first) p2.first else p1.first
    val y2 = if (p1.first < p2.first) p2.second else p1.second

    val dx = x2 - x1
    val dy = y2 - y1

    val retList = mutableListOf<Pair<Int, Int>>()
    var multiplier = 1
    var inRange = true

    // Positive
    while (inRange) {
        val newPoint = x2 + dx * multiplier to y2 + dy * multiplier
        if (input.pointInRange(newPoint)) {
            retList.add(newPoint)
            multiplier += 1
        } else {
            inRange = false
        }
    }

    // Negative
    inRange = true
    multiplier = 1
    while (inRange) {
        val newPoint = x1 - dx * multiplier to y1 - dy * multiplier
        if (input.pointInRange(newPoint)) {
            retList.add(newPoint)
            multiplier += 1
        } else {
            inRange = false
        }
    }

    return retList
}
