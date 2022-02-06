package ru.cli

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ParserTest {
    @Test
    fun splitIntoTokensTestNoQuote() {
        Assertions.assertEquals(
            listOf(
                Token("a", QuottingType.WITHOUT_QUOTE)
            ),
            Parser.splitIntoTokens("a")
        )
        Assertions.assertEquals(
            listOf(
                Token("a", QuottingType.WITHOUT_QUOTE)
            ),
            Parser.splitIntoTokens("   a ")
        )
        Assertions.assertEquals(
            listOf(
                Token("a", QuottingType.WITHOUT_QUOTE),
                Token("b", QuottingType.WITHOUT_QUOTE),
                Token("c", QuottingType.WITHOUT_QUOTE)
            ),
            Parser.splitIntoTokens("a b c")
        )
        Assertions.assertEquals(
            listOf(
                Token("a", QuottingType.WITHOUT_QUOTE),
                Token("bbda", QuottingType.WITHOUT_QUOTE),
                Token("c", QuottingType.WITHOUT_QUOTE)
            ),
            Parser.splitIntoTokens("a bbda c")
        )
        Assertions.assertEquals(
            listOf(
                Token("a", QuottingType.WITHOUT_QUOTE),
                Token("bc-0_!2", QuottingType.WITHOUT_QUOTE),
                Token("c", QuottingType.WITHOUT_QUOTE)
            ),
            Parser.splitIntoTokens("a bc-0_!2 c")
        )
    }

    @Test
    fun splitIntoTokensTestWithQuotes() {
        Assertions.assertEquals(
            listOf(
                Token("a", QuottingType.WITHOUT_QUOTE),
                Token("b b", QuottingType.SINGLE_QUOTE)
            ),
            Parser.splitIntoTokens("a \'b b\'")
        )
        Assertions.assertEquals(
            listOf(
                Token("a", QuottingType.WITHOUT_QUOTE),
                Token("b b", QuottingType.DOUBLE_QUOTE)
            ),
            Parser.splitIntoTokens("a \"b b\"")
        )
        Assertions.assertEquals(
            listOf(
                Token("a", QuottingType.WITHOUT_QUOTE),
                Token("b b", QuottingType.SINGLE_QUOTE),
                Token("c c", QuottingType.DOUBLE_QUOTE)
            ),
            Parser.splitIntoTokens("a \'b b\' \"c c\"")
        )
        Assertions.assertEquals(
            listOf(
                Token("a", QuottingType.WITHOUT_QUOTE),
                Token("b \"kek\" b", QuottingType.SINGLE_QUOTE)
            ),
            Parser.splitIntoTokens("a \'b \"kek\" b\'")
        )
        Assertions.assertEquals(
            listOf(
                Token("a", QuottingType.WITHOUT_QUOTE),
                Token("b \'kek\' b", QuottingType.DOUBLE_QUOTE)
            ),
            Parser.splitIntoTokens("a \"b \'kek\' b\"")
        )
        Assertions.assertEquals(
            listOf(
                Token("a=\"b  c\"", QuottingType.WITHOUT_QUOTE)
            ),
            Parser.splitIntoTokens("a=\"b  c\"")
        )
    }

    @Test
    fun parseAssignmentCommandTestGood() {
        Assertions.assertEquals(
            listOf("a", "b"),
            Parser.parseAssignmentCommand("a=b")
        )
        Assertions.assertEquals(
            listOf("a", "b"),
            Parser.parseAssignmentCommand("a=\"b\"")
        )
        Assertions.assertEquals(
            listOf("a", "b  b"),
            Parser.parseAssignmentCommand("a=\"b  b\"")
        )
        Assertions.assertEquals(
            listOf("a", "b  b"),
            Parser.parseAssignmentCommand("a=\'b  b\'")
        )
        Assertions.assertEquals(
            listOf("a", "b \"d\" b"),
            Parser.parseAssignmentCommand("a=\'b \"d\" b\'")
        )
    }

    @Test
    fun parseAssignmentCommandTestFail() {
        Assertions.assertThrows(
            IllegalArgumentException::class.java
        ) { Parser.parseAssignmentCommand("a= b") }
        Assertions.assertThrows(
            IllegalArgumentException::class.java
        ) { Parser.parseAssignmentCommand("a =b") }
        Assertions.assertThrows(
            IllegalArgumentException::class.java
        ) { Parser.parseAssignmentCommand("a-a=b") }
        Assertions.assertThrows(
            IllegalArgumentException::class.java
        ) { Parser.parseAssignmentCommand("a=b b") }
        Assertions.assertThrows(
            IllegalArgumentException::class.java
        ) { Parser.parseAssignmentCommand("a=\"b\" b\"") }
    }
}
