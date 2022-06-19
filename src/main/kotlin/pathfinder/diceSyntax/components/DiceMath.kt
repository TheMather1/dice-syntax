package pathfinder.diceSyntax.components

import kotlin.math.pow

class DiceMath<A : DiceComponent<*, *, *>, B : DiceComponent<*, *, *>>(a: A, b: B, val function: MathFunction) :
    DiceComponent<A, B, DiceNumber>(a, b) {
    enum class MathFunction(val symbol: Char) {
        PLUS('+') {
            override operator fun invoke(a: Number, b: Number) = a.toDouble() + b.toDouble()
        },
        MINUS('-') {
            override operator fun invoke(a: Number, b: Number) = a.toDouble() - b.toDouble()
        },
        MULTIPLY('*') {
            override operator fun invoke(a: Number, b: Number) = a.toDouble() * b.toDouble()
        },
        DIVIDE('/') {
            override operator fun invoke(a: Number, b: Number) = a.toDouble() / b.toDouble()
        },
        EXPONENT('^') {
            override operator fun invoke(a: Number, b: Number) = a.toDouble().pow(b.toDouble())
        };

        abstract operator fun invoke(a: Number, b: Number): Number

    }

    override fun toString() = "$a${function.symbol}$b"

    override fun invoke() = DiceNumber(function(a, b))
}
