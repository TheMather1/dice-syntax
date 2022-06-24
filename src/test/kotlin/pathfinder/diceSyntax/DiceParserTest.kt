package pathfinder.diceSyntax

import org.junit.jupiter.api.Test

internal class DiceParserTest {
    val parser = DiceParser()

    @Test
    fun `Test parsing`() {
        val dice = parser.parse("1d20")
        println(dice)
        println(dice())
    }
}
