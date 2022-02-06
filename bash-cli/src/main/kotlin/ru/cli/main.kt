package ru.cli

import ru.cli.commands.CommandFactory
import ru.cli.commands.ReturnCode
import java.lang.Exception

fun main() {
    generateSequence(::readLine).forEach { line ->
        if (line.isEmpty()) {
            return@forEach
        }

        val tokens = Parser.splitIntoTokens(line)
        val command = CommandFactory.getCommand(tokens)

        try {
            when (command.execute()) {
                ReturnCode.EXIT -> return
                else -> Unit
            }
        } catch (e: Exception) {
            System.err.println(e.message)
        }
    }
}
