package ru.cli

enum class QuottingType {
    WITHOUT_QUOTE,
    SINGLE_QUOTE,
    DOUBLE_QUOTE
}

data class Token(val value: String, val quottingType: QuottingType)
