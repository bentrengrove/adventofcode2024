import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val arrs = parseInput(input)
        return calculatePairDistance(arrs.first, arrs.second)
    }

    fun part2(input: List<String>): Int {
        val arrs = parseInput(input)
        return calculatePairSimilarity(arrs.first, arrs.second)
    }

    // Test if implementation meets criteria from the description, like:
    val sampleInput = listOf("3   4", "4   3", "2   5", "1   3", "3   9", "3   3")
    check(part1(sampleInput) == 11)
    check(part2(sampleInput) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

private fun parseInput(input: List<String>): Pair<List<Int>, List<Int>> {
    val left = mutableListOf<Int>()
    val right = mutableListOf<Int>()

    val splitRegex = "\\s+".toRegex()
    input.forEach { s ->
        val split = s.split(splitRegex)

        val l = split[0].toInt()
        val r = split[1].toInt()

        left.insertToKeepSorted(l)
        right.insertToKeepSorted(r)
    }

    return left to right
}

/**
 * Assumes lists are already sorted.
 * An unnecessary complication that I thought would make it run faster but never bothered to test
 * **/
private fun calculatePairDistance(left: List<Int>, right: List<Int>): Int {
    var sum = 0
    left.forEachIndexed { index, l ->
        val r = right[index]
        sum += abs(l - r)
    }

    return sum
}

private fun calculatePairSimilarity(left: List<Int>, right: List<Int>): Int {
    val counts = mutableMapOf<Int, Int>()
    right.forEach {
        val currentCount = counts.getOrDefault(it, 0)
        counts[it] = currentCount + 1
    }

    return left.sumOf {
        it * counts.getOrDefault(it, 0)
    }
}

/**
 * Assuming the list is already sorted, insert an element at the correct location to
 * keep it sorted.
 */
private fun MutableList<Int>.insertToKeepSorted(element: Int) {
    val index = binarySearch(element)
    if (index >= 0) {
        add(index, element)
    } else {
        add(abs(index + 1), element)
    }
}