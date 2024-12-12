import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Long {
        val input = parseInput(input)
        val temp = findRegions(input).map { "${it.letter}: ${it.area} x ${it.perimeter} = ${it.cost()}" }
        return findRegions(input).sumOf { it.cost()}.toLong()
    }

    fun part2(input: List<String>): Long {
        val input = parseInput(input)
        val temp = findRegions(input).map { "${it.letter}: ${it.area} x ${it.sides} = ${it.discountCost()}" }
        return findRegions(input).sumOf { it.discountCost()}.toLong()
    }

    // Test if implementation meets criteria from the description, like:
    val sampleInput1 = readInput("Day12_sample1")
    val sampleInput2 = readInput("Day12_sample2")
    val sampleInput3 = readInput("Day12_sample3")
    val sampleInput4 = readInput("Day12_sample4")
    val sampleInput5 = readInput("Day12_sample5")

    checkLong(part1(sampleInput1), 140)
    checkLong(part1(sampleInput2), 1930)

    checkLong(part2(sampleInput1), 80)
    checkLong(part2(sampleInput2), 1206)
    checkLong(part2(sampleInput3), 236)
    checkLong(part2(sampleInput4), 436)
    checkLong(part2(sampleInput5), 368)

    // Read the input from the `src/Day05.txt` file.
    val input = readInput("Day12")
    part1(input).println()
    part2(input).println() // wrong 867201
}

private fun parseInput(input: List<String>): List<CharArray> {
    return input.map { it.toCharArray() }
}

data class Region(val letter: Char, val points: List<Pair<Int, Int>>, val perimeter: Int, val sides: Int) {
    val area get() = points.size

    fun cost(): Int = perimeter * area
    fun discountCost(): Int = sides * area
}

private fun findRegions(input: List<CharArray>): List<Region> {
    val visited = mutableSetOf<Pair<Int, Int>>()
    val regions = mutableListOf<Region>()

    input.indices.forEach { y ->
        input[y].indices.forEach xloop@{ x ->
            var perimeter = 0
            var sides = 0
            val pt = x to y
            val char = input[y][x]
            if (visited.contains(pt)) return@xloop
            val regionPoints = mutableListOf<Pair<Int, Int>>()
            regionPoints.add(pt)
            visited.add(pt)

            val toVisit = mutableSetOf<Pair<Int, Int>>()

            val neighbours = neighbours(pt)
            perimeter += neighbours.filter { input.getOrNull(it.second)?.getOrNull(it.first) != char }.size
            sides += pt.corners(input)

            toVisit.addAll(neighbours)

            while (toVisit.isNotEmpty()) {
                val child = toVisit.first()
                toVisit.remove(child)

                if (!visited.contains(child)) {
                    if (input.getOrNull(child.second)?.getOrNull(child.first) == char) {
                        val neighbours = neighbours(child)
                        perimeter += neighbours.filter { input.getOrNull(it.second)?.getOrNull(it.first) != char }.size
                        sides += child.corners(input)

                        visited.add(child)
                        regionPoints.add(child)
                        toVisit.addAll(neighbours(child).subtract(visited))
                    }
                }
            }

            regions.add(Region(char, regionPoints, perimeter, sides))
        }
    }

    return regions
}

private fun neighbours(pt: Pair<Int, Int>): List<Pair<Int, Int>> {
    return listOf(
        pt.first - 1 to pt.second,
        pt.first to pt.second - 1,
        pt.first + 1 to pt.second,
        pt.first to pt.second + 1
    )
}

private fun Pair<Int, Int>.corners(grid: List<CharArray>): Int {
    val c = grid.get(this)
    var corners = 0

    // Convex
    if (grid.get(this.first to this.second + 1) != c &&
        grid.get(this.first - 1 to this.second) != c) corners++

    if (grid.get(this.first to this.second + 1) != c &&
        grid.get(this.first + 1 to this.second) != c) corners++

    if (grid.get(this.first + 1 to this.second) != c &&
        grid.get(this.first to this.second - 1) != c) corners++

    if (grid.get(this.first - 1 to this.second) != c &&
        grid.get(this.first to this.second - 1) != c) corners++

    // Concave
    // up right
    if (grid.get(this.first + 1 to this.second) != c &&
        grid.get(this.first + 1 to this.second + 1) == c &&
        grid.get(this.first to this.second + 1) == c) corners++

    // down left
    if (grid.get(this.first - 1 to this.second) != c &&
        grid.get(this.first - 1 to this.second - 1) == c &&
        grid.get(this.first to this.second - 1) == c) corners++

    // down right
    if (grid.get(this.first + 1 to this.second) != c &&
        grid.get(this.first + 1 to this.second - 1) == c &&
        grid.get(this.first to this.second - 1) == c) corners++

    // up left
    if (grid.get(this.first to this.second - 1) != c &&
        grid.get(this.first + 1 to this.second - 1) == c &&
        grid.get(this.first + 1 to this.second) == c) corners++

    // Intersecting regions

    return corners
}
