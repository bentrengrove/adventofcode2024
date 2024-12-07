import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Long {
        val input = parseInput(input)
        return input.filter { it.canBeCalculated(canCombine = false) }.sumOf { it.testValue }
    }

    fun part2(input: List<String>): Long {
        val input = parseInput(input)
        val filtered = input.filter {
            it.canBeCalculated(canCombine = true)
        }
        return filtered.sumOf { it.testValue }
    }

    // Test if implementation meets criteria from the description, like:
    val sampleInput = readInput("Day07_sample")
    checkLong(part1(sampleInput), 3749)
    checkLong(part2(sampleInput), 11387)

    // Read the input from the `src/Day07.txt` file.
    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

private data class Calibration(
    val testValue: Long,
    val numbers: List<Long>
)

private fun parseInput(input: List<String>): List<Calibration> {
    return input.map {
        val split = it.split(": ")
        Calibration(
            split[0].toLong(),
            split[1].split(" ").map { it.toLong() }
        )
    }
}

private fun Calibration.canBeCalculated(canCombine: Boolean): Boolean {
    return calculate(this.testValue, numbers, 1, this.numbers.first(), canCombine) != null
}

private fun calculate(testValue: Long, numbers: List<Long>, index: Int, total: Long, canCombine: Boolean): Long? {
    if (total == testValue && index == numbers.size) {
        return total
    } else if (index == numbers.size) {
        return null
    }

    val additionTotal = total + numbers[index]
    calculate(testValue, numbers, index+1, additionTotal, canCombine)?.let { return it }

    val multiTotal = total * numbers[index]
    calculate(testValue, numbers, index+1, multiTotal, canCombine)?.let { return it }

    if (canCombine) {
        val combineTotal = "$total${numbers[index]}".toLong()
        val newNumbers = numbers.toMutableList()
        newNumbers.removeAt(index)
        newNumbers[index - 1] = combineTotal
        calculate(testValue, newNumbers, index, combineTotal, true)?.let { return it }
    }
    return null
}
