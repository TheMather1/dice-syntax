package pathfinder.diceSyntax

import org.junit.jupiter.api.Test

internal class DiceParserTest {
    val parser = DiceParser()

    @Test
    fun `Test parsing`() {
        val dice = parser.parse("min(300d20kh50d20!+10, 20)d50")
        println(dice)
        println(dice())
    }
}
