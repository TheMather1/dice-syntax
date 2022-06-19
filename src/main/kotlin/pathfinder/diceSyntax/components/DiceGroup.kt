package pathfinder.diceSyntax.components

class DiceGroup<A : DiceComponent<*, *, *>>(a: A) :
    DiceComponent<A, Nothing?, Number>(a, null) {
    override operator fun invoke() = a()

    override fun toString() = "($a)"
}
