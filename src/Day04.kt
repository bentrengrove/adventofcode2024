import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val lines = parseInput(input)
        return countXmas(lines)
    }

    fun part2(input: List<String>): Int {
        val lines = parseInput(input)
        return countMAS(lines)
    }

    // Test if implementation meets criteria from the description, like:
    val sampleInput = readInput("Day04_sample")
    checkInt(part1(sampleInput), 18)
    checkInt(part2(sampleInput), 9)

    // Read the input from the `src/Day04.txt` file.
    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

private fun parseInput(input: List<String>): List<CharArray> {
    return input.map {
        it.toCharArray()
    }
}

private fun countXmas(input: List<CharArray>): Int {
    var count = 0
    input.forEachIndexed { y, chars ->
        chars.forEachIndexed { x, c ->
            if (c == 'X') {
                val possibles = getPossiblesXmas(input, x, y)
                count += possibles.count { it == "XMAS" }
            }
        }
    }

    return count
}

private fun countMAS(input: List<CharArray>): Int {
    var count = 0
    input.forEachIndexed { y, chars ->
        chars.forEachIndexed { x, c ->
            if (c == 'A' && isMAS(input, x, y)) {
                count += 1
            }
        }
    }

    return count
}

private fun getPossiblesXmas(input: List<CharArray>, x: Int, y: Int): List<String> {
    if (input[y][x] != 'X') return emptyList()

    val possibles = mutableListOf<String>()

    //l2r
    possibles.add(
        listOfNotNull(
            'X',
            input.getOrNull(y)?.getOrNull(x + 1),
            input.getOrNull(y)?.getOrNull(x + 2),
            input.getOrNull(y)?.getOrNull(x + 3)
        ).joinToString(separator = "")
    )

    //r2l
    possibles.add(
        listOfNotNull(
            'X',
            input.getOrNull(y)?.getOrNull(x - 1),
            input.getOrNull(y)?.getOrNull(x - 2),
            input.getOrNull(y)?.getOrNull(x - 3)
        ).joinToString(separator = "")
    )

    //b2t
    possibles.add(
        listOfNotNull(
            'X',
            input.getOrNull(y - 1)?.getOrNull(x),
            input.getOrNull(y - 2)?.getOrNull(x),
            input.getOrNull(y - 3)?.getOrNull(x)
        ).joinToString(separator = "")
    )

    //t2b
    possibles.add(
        listOfNotNull(
            'X',
            input.getOrNull(y + 1)?.getOrNull(x),
            input.getOrNull(y + 2)?.getOrNull(x),
            input.getOrNull(y + 3)?.getOrNull(x)
        ).joinToString(separator = "")
    )

    //diagonal up to right
    possibles.add(
        listOfNotNull(
            'X',
            input.getOrNull(y - 1)?.getOrNull(x + 1),
            input.getOrNull(y - 2)?.getOrNull(x + 2),
            input.getOrNull(y - 3)?.getOrNull(x + 3)
        ).joinToString(separator = "")
    )

    //diagonal down to right
    possibles.add(
        listOfNotNull(
            'X',
            input.getOrNull(y + 1)?.getOrNull(x + 1),
            input.getOrNull(y + 2)?.getOrNull(x + 2),
            input.getOrNull(y + 3)?.getOrNull(x + 3)
        ).joinToString(separator = "")
    )

    //diagonal down to left
    possibles.add(
        listOfNotNull(
            'X',
            input.getOrNull(y + 1)?.getOrNull(x - 1),
            input.getOrNull(y + 2)?.getOrNull(x - 2),
            input.getOrNull(y + 3)?.getOrNull(x - 3)
        ).joinToString(separator = "")
    )

    //diagonal up to left
    possibles.add(
        listOfNotNull(
            'X',
            input.getOrNull(y - 1)?.getOrNull(x - 1),
            input.getOrNull(y - 2)?.getOrNull(x - 2),
            input.getOrNull(y - 3)?.getOrNull(x - 3)
        ).joinToString(separator = "")
    )

    return possibles
}


private fun isMAS(input: List<CharArray>, x: Int, y: Int): Boolean {
    if (input[y][x] != 'A') return false

    val tl = input.getOrNull(y-1)?.getOrNull(x-1) ?: return false
    val tr = input.getOrNull(y+1)?.getOrNull(x-1) ?: return false
    val bl = input.getOrNull(y-1)?.getOrNull(x+1) ?: return false
    val br = input.getOrNull(y+1)?.getOrNull(x+1) ?: return false

    val chars = setOf(tl, tr, bl, br)
    if (chars.size != 2 || !chars.contains('M') || !chars.contains('S')) return false

    return (tl != br) && (tr != bl)
}
