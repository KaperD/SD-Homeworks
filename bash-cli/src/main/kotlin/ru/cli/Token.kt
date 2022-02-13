package ru.cli

/**
 * Define whether string was surrounded by ", ', or \s
 */
enum class QuotingType {
    WITHOUT_QUOTE,
    SINGLE_QUOTE,
    DOUBLE_QUOTE
}

/**
 * This class stores token value from original string and quotting type of this value
 */
data class Token(val value: String, val quotingType: QuotingType)
