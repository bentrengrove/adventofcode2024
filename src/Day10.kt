import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val input = parseInput(input)
        return countAllTrails(input, part2 = false)
    }

    fun part2(input: List<String>): Int {
        val input = parseInput(input)
        return countAllTrails(input, part2 = true)
    }

    // Test if implementation meets criteria from the description, like:
    val sampleInput = readInput("Day10_sample")
    checkInt(part1(sampleInput), 36)
    checkInt(part2(sampleInput), 81)

    // Read the input from the `src/Day05.txt` file.
    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}

private fun parseInput(input: List<String>): List<List<Int>> {
    return input.map {
        it.toCharArray().map { it.digitToInt() }
    }
}

private fun countAllTrails(map: List<List<Int>>, part2: Boolean): Int {
    var total = 0
    map.indices.forEach { y ->
        map[y].indices.forEach { x ->
            val pt = x to y
            val height = map[y][x]
            if (height == 0) {
                val count = if (part2) countDistinctTrailsAt(pt, map) else countTrailsAt(pt, map).size
                total += count
            }
        }
    }

    return total
}

private fun countTrailsAt(start: Pair<Int, Int>, map: List<List<Int>>, currentHeight: Int = 0, found: Set<Pair<Int, Int>> = setOf()): Set<Pair<Int, Int>> {
    val possibles = setOf(
        start.first to start.second - 1,
        start.first to start.second + 1,
        start.first - 1 to start.second,
        start.first + 1 to start.second
    )

    var newFound = found
    possibles.forEach {
        val height = map.getOrNull(it.second)?.getOrNull(it.first)
        if (currentHeight == 8 && height == 9) {
            newFound = newFound + setOf(it)
        } else if (height == currentHeight + 1) {
            newFound = newFound + countTrailsAt(it, map, height, found)
        }
    }

    return newFound
}

private fun countDistinctTrailsAt(start: Pair<Int, Int>, map: List<List<Int>>, currentHeight: Int = 0, currentCount: Int = 0, visited: Set<Pair<Int, Int>> = setOf()): Int {
    val newVisited = visited + setOf(start)
    val possibles = setOf(
        start.first to start.second - 1,
        start.first to start.second + 1,
        start.first - 1 to start.second,
        start.first + 1 to start.second
    )

    var newCount = currentCount
    possibles.subtract(newVisited).forEach {
        val height = map.getOrNull(it.second)?.getOrNull(it.first)
        if (currentHeight == 8 && height == 9) {
            newCount++
        } else if (height == currentHeight + 1) {
            newCount += countDistinctTrailsAt(it, map, height, currentCount, newVisited)
        }
    }

    return newCount
}