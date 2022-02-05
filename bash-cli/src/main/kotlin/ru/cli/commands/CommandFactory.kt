package ru.cli.commands

import ru.cli.Token

object CommandFactory {
    private val supportedCommands: Map<String, (List<Token>) -> Command> = mapOf(
        "pwd" to {tokens -> PwdCommand(listOf())},
        "wc" to {tokens -> WcCommand(listOf())},
    )

    operator fun invoke(args: List<Token>): Command {
        TODO()
    }
}
