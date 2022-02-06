package ru.cli

import ru.cli.commands.CommandFactory
import ru.cli.commands.ReturnCode

fun main() {
    generateSequence(::readLine).forEach { line ->
        val tokens = Parser.splitIntoTokens(line)
        val command = CommandFactory.getCommand(tokens)
        when (command.execute()) {
            ReturnCode.EXIT -> return
            ReturnCode.ERROR -> println("Something get wrong")
            else -> Unit
        }
    }
}
