package pathfinder.diceSyntax

import pathfinder.diceSyntax.components.*
import pathfinder.diceSyntax.components.DiceFunction.*
import pathfinder.diceSyntax.components.DiceMath.MathFunction.*
import pathfinder.diceSyntax.components.DiceSelector.Max
import pathfinder.diceSyntax.components.DiceSelector.Min

class DiceParser {

    fun parse(string: String): DiceComponent<*, *, *> {
        val iterator = string.iterator()
        var new: Pair<String, Char?> = "" to null
        val arr = mutableListOf<Pair<String, Char?>>()
        while (iterator.hasNext()) {
            new = parse(new.second?.takeUnless { it in functionalSymbols }?.toString() ?: "", iterator)
            arr += filterPair(new, arr.lastOrNull())
        }
        arr.removeAll { it.first.isBlank() && it.second == null }
        return parse(arr.iterator())
    }

    private fun filterPair(new: Pair<String, Char?>, prev: Pair<String, Char?>?) =
        (if (new.first == prev?.second?.toString() && new.second != null) "" else new.first) to if (new.second?.isDigit() == true) null else new.second

    private fun parse(itr: Iterator<Pair<String, Char?>>): DiceComponent<*, *, *> {
        var workObject: DiceComponent<*, *, *>? = null
        var currOp: String? = null
        while (itr.hasNext() && currOp != ")") {
            val (s, c) = itr.next()
            fun value() = if (c == '(') parseClosedGroup(s, itr) else parseNumber(s)
            workObject = if (currOp != null) {
                when {
                    currOp in diceSymbols -> if (workObject is DiceFunction) parseDiceMods(workObject, currOp, value())
                    else throw DiceParseException("$workObject is not a valid array of dice")
                    currOp.length == 1 && currOp.first() in mathSymbols -> {
                        if (workObject !is DiceMath || reorder(currOp, workObject))
                            parseMath(workObject!!, currOp.first(), value()!!)
                        else DiceMath(
                            workObject.a as DiceComponent<*, *, *>,
                            parseMath(workObject.b as DiceComponent<*, *, *>, currOp.first(), value()!!),
                            workObject.function
                        )
                    }
                    currOp == "d" -> parseDice(workObject!!, s, c, itr)
                    else -> workObject
                }
            } else when {
                workObject != null && s.isNotBlank() -> throw DiceParseException("Missing operation between $workObject and $s.")
                c == '(' -> parseClosedGroup(s, itr)
                s.toDoubleOrNull() != null -> parseNumber(s)
                else -> throw DiceParseException("Unrecognized word: `$s`.")
            }
            currOp = s.takeIf { it in diceSymbols } ?: c?.toString()
        }
        return workObject!!
    }

    private fun reorder(currOp: String, workObject: DiceMath<*,*>) = when (workObject.function) {
        PLUS -> true
        MINUS -> true
        MULTIPLY -> currOp !in listOf("+", "-")
        DIVIDE -> currOp !in listOf("+", "-")
        EXPONENT -> false
    }

    private fun parseDice(a: DiceComponent<*, *, *>, s: String, c: Char?, itr: Iterator<Pair<String, Char?>>) =
        a d when {
            s.toIntOrNull() != null -> parseNumber(s)!!
            c == '(' -> parseClosedGroup("", itr)
            else -> throw DiceParseException("Inappropriate number of dice: $s")
        }


    private fun parseDiceMods(workObject: DiceFunction<*, *>, operation: String, o: DiceComponent<*, *, *>?) =
        when (operation) {
            "!" -> Explode(workObject, o)
            "r" -> Reroll(workObject, o ?: DiceNumber(1))
            "kh" -> KeepHighest(workObject, o)
            "kl" -> KeepLowest(workObject, o)
            "dh" -> DropHighest(workObject, o)
            "dl" -> DropLowest(workObject, o)
            else -> TODO()
        }

    private fun parseNumber(string: String) = when {
        string.isBlank() -> null
        string.toIntOrNull() != null -> DiceNumber(string.toInt())
        string.toDoubleOrNull() != null -> DiceNumber(string.toDouble())
        else -> throw DiceParseException("Not a number: $string")
    }

    private fun parseClosedGroup(string: String, itr: Iterator<Pair<String, Char?>>) = when (string) {
        "" -> DiceGroup(parse(itr))
        "max" -> max(parseDualParameter(itr))
        "min" -> min(parseDualParameter(itr))
        "ceil" -> DiceRound.Ceil(parse(itr))
        "floor" -> DiceRound.Floor(parse(itr))
        else -> throw DiceParseException("Unknown function $string().")
    }

    private fun parseMath(a: DiceComponent<*, *, *>, operation: Char, o: DiceComponent<*, *, *>) = when (operation) {
        '+' -> a + o
        '-' -> a - o
        '/' -> a / o
        '*' -> a * o
        '^' -> DiceMath(a, o, EXPONENT)
        else -> throw RuntimeException("Unrecognized math symbol: `$operation`.")
    }

    private fun parseDualParameter(itr: Iterator<Pair<String, Char?>>) =
        parse(itr.collectWhile { it.second != ',' }.iterator()) to parse(itr)

    private fun parse(curr: String, iterator: CharIterator): Pair<String, Char?> {
        if (!iterator.hasNext()) return curr to null
        val c = iterator.nextChar()
        return when {
            c.isWhitespace() -> iterator.untilNotNull {
                if (!hasNext()) curr to null
                else nextChar().takeUnless { it.isWhitespace() }?.let { curr to it }
            }
            c in functionalSymbols -> curr to c
            curr.toIntOrNull() == null && c.isDigit() -> curr to c
            curr.toIntOrNull() != null && c.isLetter() -> curr to c
            curr.toIntOrNull() != null && (c.isDigit() || c == '.') -> parse(curr + c, iterator)
            curr.toDoubleOrNull() != null && c.isDigit() -> parse(curr + c, iterator)
            curr.toDoubleOrNull() != null && c == '.' -> throw DiceParseException("A number has more than one decimal point.")
            c.isLetter() -> parse(curr + c, iterator)
            else -> throw DiceParseException("Unrecognized symbol: $c")
        }
    }

    private fun max(dual: Pair<DiceComponent<*, *, *>, DiceComponent<*, *, *>>) = Max(dual.first, dual.second)
    private fun min(dual: Pair<DiceComponent<*, *, *>, DiceComponent<*, *, *>>) = Min(dual.first, dual.second)

    private fun <A> Iterator<A>.collectWhile(f: (A) -> Boolean): List<A> {
        val out = mutableListOf<A>()
        while (hasNext() && out.all(f)) out += iterator().next()
        return out
    }

    private fun <A> CharIterator.untilNotNull(f: CharIterator.() -> A?): A {
        var out: A? = null
        while (out == null) out = f()
        return out
    }

    companion object {
        val mathSymbols = arrayOf('+', '-', '*', '/', '^')
        val diceSymbols = arrayOf("!", "r", "kh", "kl", "dh", "dl")
        val functionalSymbols = mathSymbols + '!' + '(' + ')' + ',' + 'd'
    }
}
