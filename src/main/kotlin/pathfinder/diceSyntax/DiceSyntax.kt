package pathfinder.diceSyntax

import pathfinder.diceSyntax.components.DiceArray
import pathfinder.diceSyntax.components.DiceComponent
import pathfinder.diceSyntax.components.DiceFunction.*
import pathfinder.diceSyntax.components.DiceMath
import pathfinder.diceSyntax.components.DiceMath.MathFunction.*
import pathfinder.diceSyntax.components.DiceNumber

infix fun DiceComponent<*, *, *>.d(o: DiceComponent<*, *, *>) = DiceRoll(this, o)
infix fun Int.d(o: DiceComponent<*, *, *>) = DiceRoll(DiceNumber(this), o)
infix fun DiceComponent<*, *, *>.d(o: Int) = DiceRoll(this, DiceNumber(o))
infix fun Int.d(o: Int) = DiceRoll(DiceNumber(this), DiceNumber(o))

infix fun DiceComponent<*, *, DiceArray>.r(o: DiceComponent<*, *, *>) = Reroll(this, o)
infix fun DiceComponent<*, *, DiceArray>.r(o: Int) = Reroll(this, DiceNumber(o))

infix fun DiceComponent<*, *, DiceArray>.dl(o: DiceComponent<*, *, *>) = DropLowest(this, o)
infix fun DiceComponent<*, *, DiceArray>.dl(o: Int) = DropLowest(this, DiceNumber(o))

infix fun DiceComponent<*, *, DiceArray>.dh(o: DiceComponent<*, *, *>) = DropHighest(this, o)
infix fun DiceComponent<*, *, DiceArray>.dh(o: Int) = DropHighest(this, DiceNumber(o))

infix fun DiceComponent<*, *, DiceArray>.kl(o: DiceComponent<*, *, *>) = KeepLowest(this, o)
infix fun DiceComponent<*, *, DiceArray>.kl(o: Int) = KeepLowest(this, DiceNumber(o))

infix fun DiceComponent<*, *, DiceArray>.kh(o: DiceComponent<*, *, *>) = KeepHighest(this, o)
infix fun DiceComponent<*, *, DiceArray>.kh(o: Int) = KeepHighest(this, DiceNumber(o))

operator fun DiceComponent<*, *, DiceArray>.inc() = Explode(this, null)
infix fun DiceComponent<*, *, DiceArray>.ex(o: DiceComponent<*, *, *>) = Explode(this, o)
infix fun DiceComponent<*, *, DiceArray>.ex(o: Int) = Explode(this, DiceNumber(o))

operator fun DiceComponent<*, *, *>.times(o: DiceComponent<*, *, *>) = DiceMath(this, o, MULTIPLY)
operator fun Number.times(o: DiceComponent<*, *, *>) = DiceMath(DiceNumber(this), o, MULTIPLY)
operator fun DiceComponent<*, *, *>.times(o: Number) = DiceMath(this, DiceNumber(o), MULTIPLY)

operator fun DiceComponent<*, *, *>.div(o: DiceComponent<*, *, *>) = DiceMath(this, o, DIVIDE)
operator fun Number.div(o: DiceComponent<*, *, *>) = DiceMath(DiceNumber(this), o, DIVIDE)
operator fun DiceComponent<*, *, *>.div(o: Number) = DiceMath(this, DiceNumber(o), DIVIDE)

operator fun DiceComponent<*, *, *>.plus(o: DiceComponent<*, *, *>) = DiceMath(this, o, PLUS)
operator fun Number.plus(o: DiceComponent<*, *, *>) = DiceMath(DiceNumber(this), o, PLUS)
operator fun DiceComponent<*, *, *>.plus(o: Number) = DiceMath(this, DiceNumber(o), PLUS)

operator fun DiceComponent<*, *, *>.minus(o: DiceComponent<*, *, *>) = DiceMath(this, o, MINUS)
operator fun Number.minus(o: DiceComponent<*, *, *>) = DiceMath(DiceNumber(this), o, MINUS)
operator fun DiceComponent<*, *, *>.minus(o: Number) = DiceMath(this, DiceNumber(o), MINUS)
