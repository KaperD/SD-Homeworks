package ru.cli

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * This class provides methods for tokenization and parsing assignment
 */
object Parser {
    private val assignmentRegex = "^([a-zA-Z_]+)=(([^\\s\'\"]+)|\'([^\']*)\'|\"([^\"]*)\")$" // (1)=(2 (3) | ('4') | ("5"))

    /**
     * returns list of tokens
     * token matches regexp "\'([^\']*)\'|\"([^\"]*)\"|([^\\s\'\"]+)"
     */
    fun splitIntoTokens(commandStr: String): List<Token> {
        val tokenList = mutableListOf<Token>()
        val regex = "\'([^\']*)\'|" + // '(1)'
            "\"([^\"]*)\"|" + // "(2)"
            "(($assignmentRegex)|[^\\s\'\"]+)" // (3)
        val quottingTypes = listOf(QuottingType.SINGLE_QUOTE, QuottingType.DOUBLE_QUOTE, QuottingType.WITHOUT_QUOTE)
        val m: Matcher = Pattern.compile(regex).matcher(commandStr)

        while (m.find()) {
            listOf(1, 2, 3)
                .filter { m.group(it) != null }
                .forEach {
                    tokenList.add(
                        Token(m.group(it).toString(), quottingTypes[it - 1])
                    )
                }
        }

        return tokenList
    }

    /**
     * returns list arguments for assignment (name and value), separated by '='
     */
    fun parseAssignmentCommand(line: String): List<String> {
        val strList = mutableListOf<String>()
        val m: Matcher = Pattern.compile(assignmentRegex).matcher(line)

        if (m.find()) {
            listOf(1, 3, 4, 5)
                .filter { m.group(it) != null }
                .forEach { strList.add(m.group(it).toString()) }
        }

        if (strList.size != 2) {
            throw IllegalArgumentException("Invalid assigment")
        }

        return strList
    }
}
