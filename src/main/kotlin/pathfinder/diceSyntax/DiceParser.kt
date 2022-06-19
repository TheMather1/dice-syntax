package pathfinder.diceSyntax

import pathfinder.diceSyntax.components.*
import pathfinder.diceSyntax.components.DiceFunction.*
import pathfinder.diceSyntax.components.DiceMath.MathFunction.*
import pathfinder.diceSyntax.components.DiceSelector.Max
import pathfinder.diceSyntax.components.DiceSelector.Min

class DiceParser {

    fun parse(string: String): DiceComponent<*, *, *> {
        val iterator = string.iterator()
        var last: Pair<String, Char?> = "" to null
        val arr = mutableListOf<Pair<String, Char?>>()
        while (iterator.hasNext()) {
            last = parse(last.second?.takeUnless { it in functionalSymbols }?.toString() ?: "", iterator)
            arr += (if (last.first == arr.lastOrNull()?.second?.toString() && last.second != null) "" else last.first) to
                    if (last.second?.isDigit() == true) null else last.second
        }
        arr.removeAll { it.first.isBlank() && it.second == null }
        return parse(arr.iterator())
    }

    private fun parse(itr: Iterator<Pair<String, Char?>>): DiceComponent<*, *, *> {
        var workObject: DiceComponent<*, *, *>? = null
        var ongoingOperation: String? = null
        while (itr.hasNext() && ongoingOperation != ")") {
            val (s, c) = itr.next()
            fun o() = if(c== '(') parseClosedGroup(s, itr) else parseNumber(s)
            workObject = if (ongoingOperation != null) {
                when {
                    ongoingOperation in diceSymbols -> if(workObject is DiceFunction)
                        parseDiceMods(workObject, ongoingOperation, o())
                    else throw DiceParseException("$workObject is not a valid array of dice")
                    ongoingOperation.length == 1 && ongoingOperation.first() in mathSymbols -> {
                        if(
                            workObject !is DiceMath || when(workObject.function) {
                                PLUS -> true
                                MINUS -> true
                                MULTIPLY -> ongoingOperation !in listOf("+", "-")
                                DIVIDE -> ongoingOperation !in listOf("+", "-")
                                EXPONENT -> false
                            }
                        ) parseMath(workObject!!, ongoingOperation.first(), o()!!) else DiceMath(
                            workObject.a as DiceComponent<*, *, *>,
                            parseMath(workObject.b as DiceComponent<*, *, *>,ongoingOperation.first(), o()!!),
                            workObject.function
                        )
                    }
                    ongoingOperation == "d" -> parseDice(workObject!!, s, c, itr)
                    else -> workObject
                }
            } else when {
                workObject != null && s.isNotBlank() -> throw DiceParseException("Missing operation between $workObject and $s.")
                c == '(' -> parseClosedGroup(s, itr)
                s.toDoubleOrNull() != null -> parseNumber(s)
                else -> throw DiceParseException("Unrecognized word: `$s`.")
            }
            ongoingOperation = s.takeIf { it in diceSymbols } ?: c?.toString()
        }
        return workObject!!
    }

    private fun parseDice(a: DiceComponent<*, *, *>, s: String, c: Char?, itr: Iterator<Pair<String, Char?>>) = a d when {
            s.toIntOrNull() != null -> parseNumber(s)!!
            c == '(' -> parseClosedGroup("", itr)
            else -> throw DiceParseException("Inappropriate number of dice: $s")
        }


    private fun parseDiceMods(workObject: DiceFunction<*,*>, operation: String, o: DiceComponent<*, *, *>?) = when (operation) {
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

    private fun parseClosedGroup(string: String, itr: Iterator<Pair<String, Char?>>) = when(string) {
        "" -> DiceGroup(parse(itr))
        "max" -> max(parseDualParameter(itr))
        "min" -> min(parseDualParameter(itr))
        "ceil" -> DiceRound.Ceil(parse(itr))
        "floor" -> DiceRound.Floor(parse(itr))
        else -> throw DiceParseException("Unknown function $string().")
    }

    private fun parseMath(a: DiceComponent<*,*,*>, operation: Char, o: DiceComponent<*,*,*>) =
        when(operation){
            '+' -> a + o
            '-' -> a - o
            '/' -> a / o
            '*' -> a * o
            '^' -> DiceMath(a, o, EXPONENT)
            else -> throw RuntimeException("Unrecognized math symbol: `$operation`.")
        }

    private fun parseDualParameter(itr: Iterator<Pair<String, Char?>>) =
        parse(itr.collectWhile {it.second != ','}.iterator()) to parse(itr)

    private fun parse(curr: String, iterator: CharIterator): Pair<String, Char?> {
        if (!iterator.hasNext()) return curr to null
        val c = iterator.nextChar()
        return when {
            c.isWhitespace() -> iterator.untilNotNull {
                if (!hasNext()) curr to null
                else nextChar().takeUnless { it.isWhitespace() }
                    ?.let {curr to it }
            }
            c in functionalSymbols -> curr to c
            curr.toIntOrNull() == null && c.isDigit() -> curr to c
            curr.toIntOrNull() != null && c.isLetter() -> curr to c
            curr.toIntOrNull() != null && (c.isDigit() || c == '.') -> parse(curr+c, iterator)
            curr.toDoubleOrNull() != null && c.isDigit() -> parse(curr+c, iterator)
            curr.toDoubleOrNull() != null && c == '.' -> throw DiceParseException("A number has more than one decimal point.")
            c.isLetter() -> parse(curr+c, iterator)
            else -> throw DiceParseException("Unrecognized symbol: $c")
        }
    }

    private fun max(dual: Pair<DiceComponent<*,*,*>,DiceComponent<*,*,*>>) = Max(dual.first, dual.second)
    private fun min(dual: Pair<DiceComponent<*,*,*>,DiceComponent<*,*,*>>) = Min(dual.first, dual.second)

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
        val mathSymbols = arrayOf('+','-','*','/','^')
        val diceSymbols = arrayOf("!", "r", "kh", "kl", "dh", "dl")
        val functionalSymbols = mathSymbols + '!'+'('+')'+','+'d'
    }
}