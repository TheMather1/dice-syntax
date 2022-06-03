package pathfinder.diceSyntax

import kotlin.math.pow

class DiceMath<A : DiceComponent<*, *, *>, B : DiceComponent<*, *, *>>(a: A, b: B, private val function: MathFunction) :
    DiceComponent<A, B, DiceNumber>(a, b, 2 + function.priority) {
    enum class MathFunction(val priority: Int) {
        PLUS(2) {
            override operator fun invoke(a: Number, b: Number) = a.toFloat() + b.toFloat()
        },
        MINUS(2) {
            override operator fun invoke(a: Number, b: Number) = a.toFloat() - b.toFloat()
        },
        MULTIPLY(1) {
            override operator fun invoke(a: Number, b: Number) = a.toFloat() * b.toFloat()
        },
        DIVIDE(1) {
            override operator fun invoke(a: Number, b: Number) = a.toFloat() / b.toFloat()
        },
        EXPONENT(0) {
            override operator fun invoke(a: Number, b: Number) = a.toFloat().pow(b.toFloat())
        };

        abstract operator fun invoke(a: Number, b: Number): Number
    }

    override fun invoke() = DiceNumber(function(a, b))
}
