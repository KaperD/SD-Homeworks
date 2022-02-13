package ru.cli.commands

import ru.cli.Parser
import ru.cli.QuotingType
import ru.cli.Token

/**
 * This class provides methods for creating a command from list of tokens
 */
class CommandFactory {
    private val supportedCommands: Map<String, (List<String>) -> Command> = mapOf(
        "pwd" to ::PwdCommand,
        "wc" to ::WcCommand,
        "cat" to ::CatCommand,
        "echo" to ::EchoCommand,
        "exit" to ::ExitCommand
    )

    /**
     * Create one of supported commands, external command if command name not supported or assignment command
     * @param args list of tokens
     *
     * @return the created command
     */
    fun getCommand(args: List<Token>): Command {
        val commandName = args[0].value
        if ('=' in commandName && args[0].quotingType == QuotingType.WITHOUT_QUOTE) {
            return AssignmentCommand(Parser.parseAssignmentCommand(args[0].value))
        }

        return supportedCommands[commandName]?.let { it(args.subList(1, args.size).map { it.value }) }
            ?: ExternalCommand(args.map { it.value })
    }
}
