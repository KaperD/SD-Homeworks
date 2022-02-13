package ru.cli

/**
 * This class provides methods for substitution
 */
object Substitutor {
    /**
     * Returns token with substituted vars
     *
     * @param input token with vars
     * @param environment map with vars and their values
     *
     * @return token with substituted values of vars
     */
    fun substitute(input: Token, environment: Environment): Token {
        if (input.quottingType == QuottingType.SINGLE_QUOTE) {
            return input
        }
        var result = ""
        var i = 0
        while (i < input.value.length) {
            if (input.value[i] == '$') {
                i++
                var varName = ""
                while (i < input.value.length && (input.value[i] == '_' || input.value[i].isDigit() || input.value[i].isLetter())) {
                    varName += input.value[i]
                    i++
                }
                result += (environment.vars[varName] ?: "")
                continue
            } else {
                result += input.value[i]
                i++
            }
        }
        return Token(result, input.quottingType)
    }
}