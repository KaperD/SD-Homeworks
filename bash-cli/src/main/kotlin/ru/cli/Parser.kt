package ru.cli

import java.util.regex.Matcher
import java.util.regex.Pattern

object Parser {
    fun splitIntoTokens(commandStr: String): List<Token> {
        val tokenList = mutableListOf<Token>()
        val regex = "\'([^\']*)\'|\"([^\"]*)\"|(\\S+)"
        val m: Matcher = Pattern.compile(regex).matcher(commandStr)
        while (m.find()) {
            if (m.group(1) != null) {
                tokenList.add(Token(m.group(1).toString(), QuottingType.SINGLE_QUOTE))
            } else if (m.group(2) != null) {
                tokenList.add(Token(m.group(2).toString(), QuottingType.DOUBLE_QUOTE))
            } else {
                tokenList.add(Token(m.group(3).toString(), QuottingType.WITHOUT_QUOTE))
            }
        }
        return tokenList
    }
}
