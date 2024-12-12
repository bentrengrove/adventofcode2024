import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.sqrt

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun checkInt(input: Int, expected: Int) {
    if (input != expected) {
        error("Expected $expected but was $input")
    }
}

fun checkLong(input: Long, expected: Long) {
    if (input != expected) {
        error("Expected $expected but was $input")
    }
}

fun List<Int>.middle(): Int = this[this.size / 2]

fun <T> List<T>.indexOfOrNull(element: T): Int? {
    val index = this.indexOf(element)
    if (index == -1) return null
    return index
}

fun <T> MutableList<T>.swap(firstIndex: Int, secondIndex: Int) {
    val tmp = this[firstIndex]
    this[firstIndex] = this[secondIndex]
    this[secondIndex] = tmp
}

fun <T> List<T>.allPairs(): List<Pair<T, T>> {
    val pairs = mutableListOf<Pair<T, T>>()
    for (i in 0 until this.size - 1) {
        for (j in i + 1 until this.size) {
            pairs.add(this[i] to this[j])
        }
    }
    return pairs
}

fun List<List<*>>.pointInRange(p: Pair<Int, Int>): Boolean {
    return (p.first >= 0 && p.second >= 0 && p.first < this[0].size && p.second < this.size)
}

fun distanceBetween(p1: Pair<Int, Int>, p2: Pair<Int, Int>): Double {
    val (x1, y1) = p1
    val (x2, y2) = p2
    val dx = x2 - x1
    val dy = y2 - y1
    return sqrt(dx * dx + dy * dy.toDouble())
}

fun gradient(p1: Pair<Int, Int>, p2: Pair<Int, Int>): Double {
    val (x1, y1) = p1
    val (x2, y2) = p2
    return (y2 - y1).toDouble() / (x2 - x1)
}

fun interpolatePoints(p1: Pair<Int, Int>, p2: Pair<Int, Int>): List<Pair<Int, Int>> {
    val (x1, y1) = p1
    val (x2, y2) = p2
    val dist = distanceBetween(p1, p2)

    val m = gradient(p1, p2)

    // Calculate the x and y increments based on the gradient and distance
    val dx = sqrt(dist * dist / (1 + m * m))
    val dy = m * dx

    // Adjust the direction based on the gradient
    val direction = if (x2 > x1 || (x2 == x1 && y2 > y1)) 1 else -1

    // Calculate the new points
    val newX1 = (x1 - direction * dx).toInt()
    val newY1 = (y1 - direction * dy).toInt()
    val newX2 = (x2 + direction * dx).toInt()
    val newY2 = (y2 + direction * dy).toInt()

    return listOf(Pair(newX1, newY1), Pair(newX2, newY2))
}

fun List<CharArray>.get(pair: Pair<Int, Int>): Char? {
    return this.getOrNull(pair.second)?.getOrNull(pair.first)
}