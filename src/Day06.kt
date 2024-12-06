import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val input = parseInput(input)
        return countSteps(input)
    }

    fun part2(input: List<String>): Int {
        val input = parseInput(input)
        var countLoops = 0
        input.indices.forEach { y ->
            input[y].indices.forEach { x ->
                if (input[y][x] == '.') {
                    val newInput = input.map { it.copyOf() }
                    newInput[y][x] = '#'
                    val steps = countSteps(newInput)
                    if (steps == -1) {
                        countLoops++
                    }
                }
            }
        }

        return countLoops
    }

    // Test if implementation meets criteria from the description, like:
    val sampleInput = readInput("Day06_sample")
    checkInt(part1(sampleInput), 41)
    checkInt(part2(sampleInput), 6)

    // Read the input from the `src/Day05.txt` file.
    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

private fun parseInput(input: List<String>): List<CharArray> {
    return input.map { it.toCharArray() }
}

private fun countSteps(input: List<CharArray>): Int {
    val startY = input.indexOfFirst { it.contains('^') }
    val startX = input[startY].indexOf('^')
    var y = startY
    var x = startX
    var facingY = -1
    var facingX = 0
    val previous = mutableMapOf<Pair<Int, Int>, MutableSet<Pair<Int, Int>>>()

    while(y < input.size && y >= 0 && x < input[0].size && x >= 0) {
        val inFront = input.getOrNull(y + facingY)?.getOrNull(x + facingX)
        if (inFront == '#') {
            val temp = facingX
            facingX = -facingY
            facingY = temp
        } else {
            input[y][x] = 'X'

            val position = y to x
            val facing = facingY to facingX
            if (previous[position] == null) {
                previous[position] = mutableSetOf()
            } else if (previous[position]?.contains(facing) == true) {
                return -1
            }

            previous[position]?.add(facing)
            y += facingY
            x += facingX
        }
    }

    return input.sumOf { it.count { it == 'X' } }
}
