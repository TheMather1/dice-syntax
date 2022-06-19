package pathfinder.diceSyntax.components

sealed class DiceSelector<A: DiceComponent<*, *, *>, B: DiceComponent<*, *, *>>(a: A, b: B): DiceComponent<A, B, DiceNumber>(a, b) {
    class Max<A: DiceComponent<*, *, *>, B: DiceComponent<*, *, *>>(a: A, b: B): DiceSelector<A, B>(a, b) {
        override fun invoke() = DiceNumber(maxOf(a.toDouble(), b.toDouble()))
    }
    class Min<A: DiceComponent<*, *, *>, B: DiceComponent<*, *, *>>(a: A, b: B): DiceSelector<A, B>(a, b) {
        override fun invoke() = DiceNumber(minOf(a.toDouble(), b.toDouble()))
    }
}
