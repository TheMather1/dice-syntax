package pathfinder.diceSyntax

class DiceNumber(private val value: Number) : DiceComponent<Nothing?, Nothing?, DiceNumber>(null, null, 0) {
    override fun invoke() = this

    override fun toByte() = value.toByte()
    override fun toChar() = value.toChar()
    override fun toDouble() = value.toDouble()
    override fun toFloat() = value.toFloat()
    override fun toInt() = value.toInt()
    override fun toLong() = value.toLong()
    override fun toShort() = value.toShort()
}
