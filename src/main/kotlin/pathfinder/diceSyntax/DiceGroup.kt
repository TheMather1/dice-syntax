package pathfinder.diceSyntax

class DiceGroup<A : DiceComponent<*, *, C>, C : DiceComponent<*, *, *>>(a: A) :
    DiceComponent<A, Nothing?, C>(a, null, 0) {
    override operator fun invoke() = a()
}
