import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Long {
        val input = parseInput(input)
        val inputLine = getFullSpace(input)
        val compressed = compress(inputLine)
        return checksum(compressed)
    }

    fun part2(input: List<String>): Long {
        val input = parseInput(input)
        val inputLine = getFullSpace(input)
        val compressed = compressWholeBlocks(inputLine)
        return checksum(compressed)
    }

    // Test if implementation meets criteria from the description, like:
    val sampleInput = readInput("Day09_sample")
    checkLong(part1(sampleInput), 1928)
    checkLong(part2(sampleInput), 2858)

    // Read the input from the `src/Day09.txt` file.
    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

private fun parseInput(input: List<String>): List<Int> {
    return input.first().map { it.digitToInt() }
}

private fun compress(fullLine: String): String {
    val chars = fullLine.toCharArray()
    var lastIndex = chars.size-1
    chars.indices.forEach { index ->
        while (chars[lastIndex] == '.') {
            lastIndex--
        }
        if (index < lastIndex && chars[index] == '.') {
            val last = chars[lastIndex]
            chars[lastIndex] = chars[index]
            chars[index] = last
        }
    }

    return chars.joinToString("")
}

private fun compressWholeBlocks(fullLine: String): String {
    val chars = fullLine.toCharArray()
    val blocks = mutableListOf<String>()

    // Convert the string into an array of blocks
    var block = fullLine.first().toString()
    chars.indices.forEach { i ->
        if (i == 0) return@forEach

        if (chars[i] == block.first()) {
            block += chars[i]
        } else {
            blocks.add(block)
            block = chars[i].toString()
        }
    }
    blocks.add(block)

    // Compress the array of blocks
    var blockIndex = blocks.size - 1
    while (blockIndex > 0) {
        val b = blocks[blockIndex]
        if (b.first() != '.') {
            val moveTo = blocks.indices.firstOrNull {
                blocks[it].first() == '.' &&
                        blocks[it].length >= b.length
            }

            if (moveTo != null && moveTo < blockIndex) {
                val freeSpace = blocks[moveTo].length

                blocks[moveTo] = b
                blocks[blockIndex] = '.'.repeat(b.length)
                if (b.length < freeSpace) {
                    if (blocks[moveTo+1].first() == '.') {
                        blocks[moveTo+1] += '.'.repeat(freeSpace - b.length)
                    } else {
                        blocks.add(moveTo + 1, '.'.repeat(freeSpace - b.length))
                        blockIndex++
                    }
                }
            }
        }

        blockIndex--
    }

    return blocks.joinToString("")
}

private fun getFullSpace(input: List<Int>): String {
    var readingFreeSpace = false
    var index = 0
    return buildString {
        input.forEach {
            if (!readingFreeSpace) {
                val encoded = getCharForId(index)
                assert(encoded != '.')
                repeat(it) { append(encoded) }
                index++
            } else {
                repeat(it) { append(".") }
            }

            readingFreeSpace = !readingFreeSpace
        }
    }
}

private fun getCharForId(id: Int): Char {
    return if (id < 10) {
        id.digitToChar()
    } else {
        ('A' + id - 10)
    }
}

private fun getIdForChar(char: Char): Int {
    return if (char in '0'..'9') {
        char.digitToInt()
    } else {
        char - 'A' + 10
    }
}

private fun checksum(input: String): Long {
    var total = 0L
    input.forEachIndexed { index, c ->
        if (c != '.') {
            total += index.toLong() * getIdForChar(c)
        }
    }
    return total
}

fun Char.repeat(n :Int): String {
    val c = this
    return buildString {
        repeat(n) { append(c) }
    }
}