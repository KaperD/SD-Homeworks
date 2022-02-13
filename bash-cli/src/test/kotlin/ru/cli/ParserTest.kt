package ru.cli

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ParserTest {
    @Test
    fun splitIntoTokensTestNoQuote() {
        Assertions.assertEquals(
            listOf(
                Token("a", QuotingType.WITHOUT_QUOTE)
            ),
            Parser.splitIntoTokens("a")
        )
        Assertions.assertEquals(
            listOf(
                Token("a", QuotingType.WITHOUT_QUOTE)
            ),
            Parser.splitIntoTokens("   a ")
        )
        Assertions.assertEquals(
            listOf(
                Token("a", QuotingType.WITHOUT_QUOTE),
                Token("b", QuotingType.WITHOUT_QUOTE),
                Token("c", QuotingType.WITHOUT_QUOTE)
            ),
            Parser.splitIntoTokens("a b c")
        )
        Assertions.assertEquals(
            listOf(
                Token("a", QuotingType.WITHOUT_QUOTE),
                Token("bbda", QuotingType.WITHOUT_QUOTE),
                Token("c", QuotingType.WITHOUT_QUOTE)
            ),
            Parser.splitIntoTokens("a bbda c")
        )
        Assertions.assertEquals(
            listOf(
                Token("a", QuotingType.WITHOUT_QUOTE),
                Token("bc-0_!2", QuotingType.WITHOUT_QUOTE),
                Token("c", QuotingType.WITHOUT_QUOTE)
            ),
            Parser.splitIntoTokens("a bc-0_!2 c")
        )
    }

    @Test
    fun splitIntoTokensTestWithQuotes() {
        Assertions.assertEquals(
            listOf(
                Token("a", QuotingType.WITHOUT_QUOTE),
                Token("b b", QuotingType.SINGLE_QUOTE)
            ),
            Parser.splitIntoTokens("a \'b b\'")
        )
        Assertions.assertEquals(
            listOf(
                Token("a", QuotingType.WITHOUT_QUOTE),
                Token("b b", QuotingType.DOUBLE_QUOTE)
            ),
            Parser.splitIntoTokens("a \"b b\"")
        )
        Assertions.assertEquals(
            listOf(
                Token("a", QuotingType.WITHOUT_QUOTE),
                Token("b b", QuotingType.SINGLE_QUOTE),
                Token("c c", QuotingType.DOUBLE_QUOTE)
            ),
            Parser.splitIntoTokens("a \'b b\' \"c c\"")
        )
        Assertions.assertEquals(
            listOf(
                Token("a", QuotingType.WITHOUT_QUOTE),
                Token("b \"kek\" b", QuotingType.SINGLE_QUOTE)
            ),
            Parser.splitIntoTokens("a \'b \"kek\" b\'")
        )
        Assertions.assertEquals(
            listOf(
                Token("a", QuotingType.WITHOUT_QUOTE),
                Token("b \'kek\' b", QuotingType.DOUBLE_QUOTE)
            ),
            Parser.splitIntoTokens("a \"b \'kek\' b\"")
        )
        Assertions.assertEquals(
            listOf(
                Token("a=\"b  c\"", QuotingType.WITHOUT_QUOTE)
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

    @Test
    fun splitIntoCommandsTest() {
        Assertions.assertEquals(
            listOf(
                listOf(
                    Token("a", QuotingType.WITHOUT_QUOTE),
                    Token("b", QuotingType.WITHOUT_QUOTE)
                ),
                listOf(
                    Token("c", QuotingType.WITHOUT_QUOTE)
                ),
                listOf(
                    Token("a", QuotingType.WITHOUT_QUOTE),
                    Token("b", QuotingType.WITHOUT_QUOTE),
                    Token("c", QuotingType.WITHOUT_QUOTE),
                    Token("d", QuotingType.WITHOUT_QUOTE)
                )
            ),
            Parser.splitIntoCommands(
                listOf(
                    Token("a", QuotingType.WITHOUT_QUOTE),
                    Token("b", QuotingType.WITHOUT_QUOTE),
                    Token("|", QuotingType.WITHOUT_QUOTE),
                    Token("c", QuotingType.WITHOUT_QUOTE),
                    Token("|", QuotingType.WITHOUT_QUOTE),
                    Token("a", QuotingType.WITHOUT_QUOTE),
                    Token("b", QuotingType.WITHOUT_QUOTE),
                    Token("c", QuotingType.WITHOUT_QUOTE),
                    Token("d", QuotingType.WITHOUT_QUOTE)
                )
            )
        )
    }

    @Test
    fun splitIntoCommandsQuotingTest() {
        Assertions.assertEquals(
            listOf(
                listOf(
                    Token("a", QuotingType.WITHOUT_QUOTE),
                    Token("b", QuotingType.WITHOUT_QUOTE),
                    Token("|", QuotingType.SINGLE_QUOTE),
                    Token("c", QuotingType.WITHOUT_QUOTE),
                    Token("|", QuotingType.DOUBLE_QUOTE),
                    Token("a", QuotingType.WITHOUT_QUOTE),
                    Token("b", QuotingType.WITHOUT_QUOTE),
                    Token("c", QuotingType.WITHOUT_QUOTE),
                    Token("d", QuotingType.WITHOUT_QUOTE)
                )
            ),
            Parser.splitIntoCommands(
                listOf(
                    Token("a", QuotingType.WITHOUT_QUOTE),
                    Token("b", QuotingType.WITHOUT_QUOTE),
                    Token("|", QuotingType.SINGLE_QUOTE),
                    Token("c", QuotingType.WITHOUT_QUOTE),
                    Token("|", QuotingType.DOUBLE_QUOTE),
                    Token("a", QuotingType.WITHOUT_QUOTE),
                    Token("b", QuotingType.WITHOUT_QUOTE),
                    Token("c", QuotingType.WITHOUT_QUOTE),
                    Token("d", QuotingType.WITHOUT_QUOTE)
                )
            )
        )
    }
}
