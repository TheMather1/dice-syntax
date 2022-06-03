package pathfinder.diceSyntax

abstract class DiceComponent<A, B, C : Number>(val a: A, val b: B, private val priority: Int) :
    Comparable<DiceComponent<*, *, *>>, Number() {
    abstract operator fun invoke(): C

    override fun compareTo(other: DiceComponent<*, *, *>) = priority.compareTo(other.priority)

    override fun toByte(): Byte = this().toByte()
    override fun toChar(): Char = this().toChar()
    override fun toDouble(): Double = this().toDouble()
    override fun toFloat(): Float = this().toFloat()
    override fun toInt(): Int = this().toInt()
    override fun toLong(): Long = this().toLong()
    override fun toShort(): Short = this().toShort()
}
