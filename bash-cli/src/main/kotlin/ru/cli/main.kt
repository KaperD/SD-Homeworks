package ru.cli

import ru.cli.commands.CommandFactory
import ru.cli.commands.StatusCode
import java.lang.Exception

fun main() {
    generateSequence(::readLine).forEach { line ->
        if (line.isEmpty()) {
            return@forEach
        }

        val commandFactory = CommandFactory()
        val environment = Environment()

        val tokens = Parser.splitIntoTokens(line).flatMap { token ->
            Substitutor.substitute(token, environment).let { substitutedToken ->
                when (substitutedToken.quotingType) {
                    QuotingType.WITHOUT_QUOTE -> Parser.splitIntoTokens(substitutedToken.value)
                    else -> listOf(substitutedToken)
                }
            }
        }

        val commands = Parser.splitIntoCommands(tokens).map { commandFactory.getCommand(it) }
        val pipe = Pipe(commands)

        try {
            when (pipe.execute(environment = environment).status) {
                StatusCode.EXIT -> return
                else -> Unit
            }
        } catch (e: Exception) {
            System.err.println(e.message)
        }
    }
}
