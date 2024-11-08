package pathfinder.diceSyntax

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class DiceParserTest {
    val parser = DiceParser()

    @Test
    fun `Test parsing`() {
        val dice = parser.parse("-1d20")
        println(dice)
        val roll = dice()
        println(roll)
        println(roll.toInt())
    }

    @Test
    fun `Test syntax`() {
        val dice = (6 d 8 r 2 kh 3) * 12
        assertEquals("6d8r2kh3*12", dice.toString())
        val result = dice()
        println(result)
        assertEquals(0, result.toInt()%12)
        assert((result.toInt()/12)/3 > 4)
    }
}
