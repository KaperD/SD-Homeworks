package ru.cli.commands

import ru.cli.Parser
import ru.cli.QuottingType
import ru.cli.Token

object CommandFactory {
    private val supportedCommands: Map<String, (List<String>) -> Command> = mapOf(
        "pwd" to ::PwdCommand,
        "wc" to ::WcCommand,
        "cat" to ::CatCommand,
        "echo" to ::EchoCommand,
        "exit" to ::ExitCommand
    )

    fun getCommand(args: List<Token>): Command {
        val commandName = args[0].value
        if ('=' in commandName && args[0].quottingType == QuottingType.WITHOUT_QUOTE) {
            return AssignmentCommand(Parser.parseAssignmentCommand(args[0].value))
        }

        return supportedCommands[commandName]?.let { it(args.subList(1, args.size).map { it.value }) }
            ?: ExternalCommand(args.map { it.value })
    }
}
