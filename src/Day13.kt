fun main() {
    fun part1(input: List<String>): Long {
        val input = parseInput(input)
        return input.sumOf {
            val cost = calculate(it)
            println("${it.prize} = $cost")

            if (cost == null) return@sumOf 0
            cost.first * ACOST + cost.second * BCOST
        }
    }

    fun part2(input: List<String>): Long {
        val input = parseInput(input)
        return input.sumOf {
            val cost = calculate(it, prizeOffset = 10000000000000)
            println("${it.prize} = $cost")

            if (cost == null) return@sumOf 0
            cost.first * ACOST + cost.second * BCOST
        }
    }

    // Test if implementation meets criteria from the description, like:
    val sampleInput = readInput("Day13_sample")
    checkLong(part1(sampleInput), 480)
    //checkLong(part2(sampleInput), 0)

    // Read the input from the `src/Day05.txt` file.
    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}

val ACOST = 3
val BCOST = 1

private data class Game(val buttonA: Pair<Int, Int>, val buttonB: Pair<Int, Int>, val prize: Pair<Int, Int>)
private fun parseInput(input: List<String>): List<Game> {
    val games = mutableListOf<Game>()

    var buttonA: Pair<Int, Int>
    var buttonB: Pair<Int, Int>
    var prize: Pair<Int, Int>

    var index = 0
    while (index < input.size) {
        val a = input[index].substring("Button A: ".length).split(",")
        buttonA = a[0].substring(2).toInt() to a[1].trim().substring(2).toInt()
        val b = input[index+1].substring("Button B: ".length).split(",")
        buttonB = b[0].substring(2).toInt() to b[1].trim().substring(2).toInt()
        val p = input[index+2].substring("Prize: ".length).split(",")
        prize = p[0].substring(2).trim().toInt() to p[1].trim().substring(2).toInt()

        games.add(
            Game(buttonA, buttonB, prize)
        )
        index += 4
    }

    return games
}

private fun calculate(game: Game, prizeOffset: Long = 0): Pair<Long, Long>? {
    val i: Long
    val j: Long
    val pX = game.prize.first + prizeOffset
    val pY = game.prize.second + prizeOffset

    with(game) {
        i = (pX*buttonB.second - pY*buttonB.first) / (buttonA.first*buttonB.second - buttonA.second*buttonB.first)
        j = (pY*buttonA.first - pX*buttonA.second) / (buttonA.first*buttonB.second - buttonA.second*buttonB.first)

        if (i * buttonA.first.toLong() + j * buttonB.first != pX) return null
        if (i * buttonA.second.toLong() + j * buttonB.second != pY) return null
    }

    return i to j
}