package pathfinder.diceSyntax.components

import kotlin.math.ceil
import kotlin.math.floor

sealed class DiceRound<A: DiceComponent<*, *, *>>(a: A): DiceComponent<A, Nothing?, DiceNumber>(a, null) {
    class Ceil<A: DiceComponent<*, *, *>>(a: A): DiceRound<A>(a) {
        override fun invoke() = DiceNumber(ceil(a.toDouble()))
    }
    class Floor<A: DiceComponent<*, *, *>>(a: A): DiceRound<A>(a) {
        override fun invoke() = DiceNumber(floor(a.toDouble()))
    }
}
