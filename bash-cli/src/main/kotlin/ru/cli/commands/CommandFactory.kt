package ru.cli.commands

import ru.cli.Token

object CommandFactory {
    private val supportedCommands: Map<String, (List<String>) -> Command> = mapOf(
        "pwd" to { _ -> PwdCommand(listOf()) },
        "wc" to { _ -> WcCommand(listOf()) },
        "cat" to { tokens -> CatCommand(tokens) },
        "echo" to { tokens -> EcoCommand(tokens) },
        "exit" to { _ -> ExitCommand(listOf()) }
    )

    fun getCommand(args: List<String>): Command {
        TODO()
    }
}
