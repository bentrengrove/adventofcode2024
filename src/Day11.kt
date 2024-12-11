import java.util.LinkedList
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>, blinkTimes: Int): Long {
        val input = parseInput(input)
        return blink(blinkTimes, input)
    }

    // Test if implementation meets criteria from the description, like:
    val sampleInput = readInput("Day11_sample")
    checkLong(part1(sampleInput, 1), 3)
    checkLong(part1(sampleInput, 6), 22)
    checkLong(part1(sampleInput, 25), 55312)

    // Read the input from the `src/Day05.txt` file.
    val input = readInput("Day11")
    part1(input, 25).println()
    part1(input, 75).println()
}

private fun parseInput(input: List<String>): List<Long> {
    return input.first().split(" ").map { it.toLong() }
}

private fun blink(n: Int, stones: List<Long>, cache: MutableMap<Pair<Int, Long>, Long> = mutableMapOf()): Long {
    val results = stones.map { blink(n, it, cache) }
    return results.sum()
}

private fun blink(n: Int, stone: Long, cache: MutableMap<Pair<Int, Long>, Long> = mutableMapOf(), stones: Long = 1): Long {
    if (n == 0) { return stones }

    val cacheKey = n to stone
    if (cache[cacheKey] != null) {
        return cache[cacheKey]!!
    }

    val result = if (stone == 0L) {
        blink(n-1, 1, cache, stones)
    } else if ("$stone".length % 2 == 0) {
        val stoneStr = "$stone"
        val left = stoneStr.substring(0, stoneStr.length / 2).toLong()
        val right = stoneStr.substring(stoneStr.length / 2, stoneStr.length).toLong()

        blink(n-1, left, cache, stones) + blink(n-1, right, cache, stones)
    } else {
        blink(n-1, stone * 2024, cache, stones)
    }

    cache[cacheKey] = result
    return result
}