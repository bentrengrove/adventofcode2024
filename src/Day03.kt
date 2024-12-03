import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        return processMulCommand(input)
    }

    fun part2(input: List<String>): Int {
        return processMulCommand(input, partTwo = true)
    }

    // Test if implementation meets criteria from the description, like:
    val sampleBadInput = listOf("mul ( 2 , 4 )", "mul(4*", "mul(6,9!", "?(12,34)")
    val sampleGoodInput = listOf("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))")
    checkInt(processMulCommand(sampleBadInput), 0)
    checkInt(processMulCommand(sampleGoodInput), 161)

    val samplePart2 = listOf("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")
    checkInt(part2(samplePart2), 48)
    // Read the input from the `src/Day03.txt` file.
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

val mulRegex = Regex("mul\\((\\d{1,3}),\\s*(\\d{1,3})\\)|don't\\(\\)|do\\(\\)")

private fun processMulCommand(lines: List<String>, partTwo: Boolean = false): Int {
    var sum = 0
    val oneLine = lines.joinToString("\n")
    var enabled = true
    mulRegex.findAll(oneLine).forEach {
        if (it.value.startsWith("mul") && enabled) {
            sum += it.groupValues[1].toInt() * it.groupValues[2].toInt()
        } else {
            if (partTwo) {
                enabled = it.value == ENABLE
            }
        }
    }

    return sum
}

val DISABLE = "don't()"
val ENABLE = "do()"
