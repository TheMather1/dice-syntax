package pathfinder.diceSyntax.components

import kotlin.math.absoluteValue

sealed class DiceFunction<A : DiceComponent<*, *, *>?, B : DiceComponent<*, *, *>?>(a: A, b: B) :
    DiceComponent<A, B, DiceArray>(a, b) {
    abstract override operator fun invoke(): DiceArray

    class DiceRoll<B : DiceComponent<*, *, *>>(a: DiceComponent<*, *, *>?, b: B) :
        DiceFunction<DiceComponent<*, *, *>, B>(a ?: one, b) {
        override fun invoke() = a.toInt().let{
            DiceArray((1..it.absoluteValue).map { Die(b.toInt()) }, it < 0)
        }
        override fun toString() = "${a}d$b"
    }

    class Reroll<B : DiceComponent<*, *, *>>(a: DiceComponent<*, *, DiceArray>, b: B) :
        DiceFunction<DiceComponent<*, *, DiceArray>, B>(a, b) {
        override fun invoke() = DiceArray(a().map { it.reroll(b.toInt()) })
        override fun toString() = "${a}r$b"
    }

    class Explode<B : DiceComponent<*, *, *>?>(dice: DiceComponent<*, *, DiceArray>, threshold: B) :
        DiceFunction<DiceComponent<*, *, DiceArray>, B>(dice, threshold) {
        override fun invoke() = DiceArray(a().fold(emptyList()) { acc, die ->
            acc + die.explode(b?.toInt())
        })
        override fun toString() = "${a}!${b ?: ""}"
    }

    class DropLowest(dice: DiceComponent<*, *, DiceArray>, count: DiceComponent<*, *, *>?) :
        DiceFunction<DiceComponent<*, *, DiceArray>, DiceComponent<*, *, *>>(dice, count ?: one) {
        override fun invoke(): DiceArray {
            val dice = a()
            return DiceArray(dice.minus(dice.sorted().take(b.toInt()).toSet()))
        }
        override fun toString() = "${a}dl$b"
    }

    class DropHighest(dice: DiceComponent<*, *, DiceArray>, count: DiceComponent<*, *, *>?) :
        DiceFunction<DiceComponent<*, *, DiceArray>, DiceComponent<*, *, *>>(dice, count ?: one) {
        override fun invoke(): DiceArray {
            val dice = a()
            return DiceArray(dice().minus(dice().sorted().takeLast(b.toInt()).toSet()))
        }
        override fun toString() = "${a}dh$b"
    }

    class KeepLowest(dice: DiceComponent<*, *, DiceArray>, count: DiceComponent<*, *, *>?) :
        DiceFunction<DiceComponent<*, *, DiceArray>, DiceComponent<*, *, *>>(dice, count ?: one) {
        override fun invoke(): DiceArray {
            val dice = a()
            return DiceArray(dice.minus(dice.sorted().takeLast(dice.size - b.toInt()).toSet()))
        }
        override fun toString() = "${a}kl$b"
    }

    class KeepHighest(dice: DiceComponent<*, *, DiceArray>, count: DiceComponent<*, *, *>?) :
        DiceFunction<DiceComponent<*, *, DiceArray>, DiceComponent<*, *, *>>(dice, count ?: one) {
        override fun invoke(): DiceArray {
            val dice = a()
            return DiceArray(dice.minus(dice.sorted().take(dice.size - b.toInt()).toSet()))
        }
        override fun toString() = "${a}kh$b"
    }

    companion object {
        private val one: DiceComponent<*, *, *> = DiceNumber(1)
    }
}
