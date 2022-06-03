package pathfinder.diceSyntax

import kotlin.random.Random

class Die(val sides: Int) : Comparable<Die>, Number() {
    val value = Random.nextInt(1, sides)
    fun reroll(threshold: Int): Die = if (threshold in value until sides) Die(sides).reroll(threshold) else this

    fun explode(threshold: Int?): List<Die> =
        if (value >= (threshold ?: sides)) listOf(this) + Die(sides).explode(threshold)
        else listOf(this)

    override fun toByte() = value.toByte()
    override fun toChar() = value.toChar()
    override fun toDouble() = value.toDouble()
    override fun toFloat() = value.toFloat()
    override fun toInt() = value
    override fun toLong() = value.toLong()
    override fun toShort() = value.toShort()
    override fun compareTo(other: Die) = value.compareTo(other.value)
}
