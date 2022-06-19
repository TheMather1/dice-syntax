package pathfinder.diceSyntax

import org.junit.jupiter.api.Test

internal class DiceParserTest {
    val parser = DiceParser()

    @Test
    fun `Test parsing`() {
        val dice = parser.parse("((2d4)d6)*2^5")
        println(dice)
        println(dice())
    }
}
