package pathfinder.diceSyntax

class DiceArray(dice: List<Die>) : List<Die> by dice, DiceComponent<Nothing?, Nothing?, DiceArray>(null, null, 0) {
    private fun sum() = fold(0) { acc, die -> acc + die.toInt() }

    override fun toByte() = sum().toByte()
    override fun toChar() = sum().toChar()
    override fun toDouble() = sum().toDouble()
    override fun toFloat() = sum().toFloat()
    override fun toInt() = sum()
    override fun toLong() = sum().toLong()
    override fun toShort() = sum().toShort()

    override fun invoke() = this
}
