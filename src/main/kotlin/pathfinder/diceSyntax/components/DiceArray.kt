package pathfinder.diceSyntax.components

class DiceArray(dice: List<Die>, val negative: Boolean = false) : List<Die> by dice, DiceComponent<Nothing?, Nothing?, DiceArray>(null, null) {
    private fun sum() = fold(0) { acc, die -> acc + die.toInt() }.let {
        if (negative) 0 - it else it
    }

    override fun toByte() = sum().toByte()
    override fun toChar() = sum().toChar()
    override fun toDouble() = sum().toDouble()
    override fun toFloat() = sum().toFloat()
    override fun toInt() = sum()
    override fun toLong() = sum().toLong()
    override fun toShort() = sum().toShort()

    override fun toString() = "${map { it.toString() }}"

    override fun invoke() = this
}
