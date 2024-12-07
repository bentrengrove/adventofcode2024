import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val input = parseInput(input)
        return input.pages.filter {
            isOrderedByRules(input.orderRules, it)
        }.sumOf {
            it.middle()
        }
    }

    fun part2(input: List<String>): Int {
        val input = parseInput(input)
        val incorrect = input.pages.filter {
            !isOrderedByRules(input.orderRules, it)
        }
        return incorrect.map { orderByRules(input.orderRules, it) }.sumOf { it.middle() }
    }

    // Test if implementation meets criteria from the description, like:
    val sampleInput = readInput("Day05_sample")
    checkInt(part1(sampleInput), 143)
    checkInt(part2(sampleInput), 123)

    // Read the input from the `src/Day05.txt` file.
    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

private data class Input(
    val orderRules: List<Pair<Int, Int>>,
    val pages: List<List<Int>>
)

private fun parseInput(input: List<String>): Input {
    val orderRules = mutableListOf<Pair<Int, Int>>()
    val pages = mutableListOf<List<Int>>()

    input.forEach {
        if (it.contains("|")) {
            orderRules.add(it.split("|").map { it.toInt() }.toPair())
        } else if (it.contains(",")) {
            pages.add(it.split(",").map { it.toInt() })
        }
    }
    return Input(orderRules, pages)
}

private fun isOrderedByRules(orderRules: List<Pair<Int, Int>>, pages: List<Int>): Boolean {
    orderRules.forEach { rule ->
        val firstIndex = pages.indexOfOrNull(rule.first) ?: return@forEach
        val secondIndex = pages.indexOfOrNull(rule.second) ?: return@forEach

        if (secondIndex < firstIndex) return false
    }

    return true
}

private fun orderByRules(orderRules: List<Pair<Int, Int>>, pages: List<Int>): List<Int> {
    val outList = pages.sortedWith(Comparator { a, b ->
        val rule = setOfNotNull(
            orderRules.find { it.first == a && it.second == b },
            orderRules.find { it.first == b && it.second == a }
        ).firstOrNull() ?: return@Comparator 0

        if (a == rule.first) {
            -1
        } else {
            1
        }
    })

    return outList
}

private fun <T> List<T>.toPair(): Pair<T, T> = this[0] to this[1]