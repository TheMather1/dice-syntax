package pathfinder.diceSyntax

sealed class DiceComparison<B : DiceComponent<*, *, *>>(dice: DiceComponent<*, *, DiceArray>, b: B) :
    DiceComponent<DiceComponent<*, *, DiceArray>, B, DiceNumber>(dice, b, 10) {

    class HigherThan(dice: DiceComponent<*, *, DiceArray>, value: DiceComponent<*, *, *>) :
        DiceComparison<DiceComponent<*, *, *>>(dice, value) {
        override fun invoke() = DiceNumber(a().count { it.value > b.toInt() })
    }

    class LessThan(dice: DiceComponent<*, *, DiceArray>, value: DiceComponent<*, *, *>) :
        DiceComparison<DiceComponent<*, *, *>>(dice, value) {
        override fun invoke() = DiceNumber(a().count { it.value < b.toInt() })
    }

    class HigherThanEqual(dice: DiceComponent<*, *, DiceArray>, value: DiceComponent<*, *, *>) :
        DiceComparison<DiceComponent<*, *, *>>(dice, value) {
        override fun invoke() = DiceNumber(a().count { it.value >= b.toInt() })
    }

    class LessThanEqual(dice: DiceComponent<*, *, DiceArray>, value: DiceComponent<*, *, *>) :
        DiceComparison<DiceComponent<*, *, *>>(dice, value) {
        override fun invoke() = DiceNumber(a().count { it.value <= b.toInt() })
    }

    class Equal(dice: DiceComponent<*, *, DiceArray>, value: DiceComponent<*, *, *>) :
        DiceComparison<DiceComponent<*, *, *>>(dice, value) {
        override fun invoke() = DiceNumber(a().count { it.value == b.toInt() })
    }

    class NotEqual(dice: DiceComponent<*, *, DiceArray>, value: DiceComponent<*, *, *>) :
        DiceComparison<DiceComponent<*, *, *>>(dice, value) {
        override fun invoke() = DiceNumber(a().count { it.value != b.toInt() })
    }
}
