import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val levels = parseInput(input)
        return levels.map { it.isSafe() }.count { it }
    }

    fun part2(input: List<String>): Int {
        val levels = parseInput(input)
        return levels.map { it.isSafeDampened() }.count { it }
    }

    // Test if implementation meets criteria from the description, like:
    val sampleInput = readInput("Day02_sample")
    checkInt(part1(sampleInput), 2)
    checkInt(part2(sampleInput), 4)

    // Read the input from the `src/Day02.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

private fun parseInput(input: List<String>): List<List<Int>> {
    return input.map {
        it.split(" ").map { it.toInt() }
    }
}

private fun List<Int>.isSafe(): Boolean {
    val increasing = this[0] < this[1]
    (1 until this.size).forEach { i ->
        val diff = this[i] - this[i-1]
        if (increasing && diff <= 0 || diff > 3) {
            return false
        } else if (!increasing && diff >= 0 || diff < -3) {
            return false
        }
    }

    return true
}

private fun List<Int>.isSafeDampened(): Boolean {
    return isSafe()
}