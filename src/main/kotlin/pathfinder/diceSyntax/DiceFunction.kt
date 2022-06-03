package pathfinder.diceSyntax

sealed class DiceFunction<A : DiceComponent<*, *, *>?, B : DiceComponent<*, *, *>?>(a: A, b: B) :
    DiceComponent<A, B, DiceArray>(a, b, 1) {
    abstract override operator fun invoke(): DiceArray

    class DiceRoll<B : DiceComponent<*, *, *>>(a: DiceComponent<*, *, *>?, b: B) :
        DiceFunction<DiceComponent<*, *, *>, B>(a ?: one, b) {
        override fun invoke() = DiceArray((1..a.toInt()).map { Die(b.toInt()) })
    }

    class Reroll<B : DiceComponent<*, *, *>>(a: DiceComponent<*, *, DiceArray>, b: B) :
        DiceFunction<DiceComponent<*, *, DiceArray>, B>(a, b) {
        override fun invoke() = DiceArray(a().map { it.reroll(b.toInt()) })
    }

    class Explode<B : DiceComponent<*, *, *>?>(dice: DiceComponent<*, *, DiceArray>, threshold: B) :
        DiceFunction<DiceComponent<*, *, DiceArray>, B>(dice, threshold) {
        override fun invoke() = DiceArray(a().fold(emptyList()) { acc, die ->
            acc + die.explode(b?.toInt())
        })
    }

    class DropLowest(dice: DiceComponent<*, *, DiceArray>, count: DiceComponent<*, *, *>?) :
        DiceFunction<DiceComponent<*, *, DiceArray>, DiceComponent<*, *, *>>(dice, count ?: one) {
        override fun invoke(): DiceArray {
            val dice = a()
            return DiceArray(dice.minus(dice.sorted().take(b.toInt()).toSet()))
        }
    }

    class DropHighest(dice: DiceComponent<*, *, DiceArray>, count: DiceComponent<*, *, *>?) :
        DiceFunction<DiceComponent<*, *, DiceArray>, DiceComponent<*, *, *>>(dice, count ?: one) {
        override fun invoke(): DiceArray {
            val dice = a()
            return DiceArray(dice().minus(dice().sorted().takeLast(b.toInt()).toSet()))
        }
    }

    class KeepLowest(dice: DiceComponent<*, *, DiceArray>, count: DiceComponent<*, *, *>?) :
        DiceFunction<DiceComponent<*, *, DiceArray>, DiceComponent<*, *, *>>(dice, count ?: one) {
        override fun invoke(): DiceArray {
            val dice = a()
            return DiceArray(dice.minus(dice.sorted().takeLast(dice.size - b.toInt()).toSet()))
        }
    }

    class KeepHighest(dice: DiceComponent<*, *, DiceArray>, count: DiceComponent<*, *, *>?) :
        DiceFunction<DiceComponent<*, *, DiceArray>, DiceComponent<*, *, *>>(dice, count ?: one) {
        override fun invoke(): DiceArray {
            val dice = a()
            return DiceArray(dice.minus(dice.sorted().take(dice.size - b.toInt()).toSet()))
        }
    }

    companion object {
        private val one: DiceComponent<*, *, *> = DiceNumber(1)
    }
}
