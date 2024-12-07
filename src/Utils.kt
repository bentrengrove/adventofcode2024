import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

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