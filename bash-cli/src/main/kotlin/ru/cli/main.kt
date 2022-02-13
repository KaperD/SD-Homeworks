package ru.cli

import ru.cli.commands.CommandFactory
import ru.cli.commands.ReturnCode
import ru.cli.commands.StatusCode
import java.lang.Exception

fun main() {
    generateSequence(::readLine).forEach { line ->
        if (line.isEmpty()) {
            return@forEach
        }

        val tokens = Parser.splitIntoTokens(line)
        val command = CommandFactory.getCommand(tokens)

        try {
            when (command.execute().status) {
                StatusCode.EXIT -> return
                else -> Unit
            }
        } catch (e: Exception) {
            System.err.println(e.message)
        }
    }
}
