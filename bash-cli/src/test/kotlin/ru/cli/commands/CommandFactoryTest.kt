package ru.cli.commands

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ru.cli.QuottingType
import ru.cli.Token
import java.lang.RuntimeException
import kotlin.test.assertEquals

class CommandFactoryTest {
    @Test
    fun supportedCommandTest() {
        val supportedCommands = listOf("pwd", "wc", "cat", "echo", "exit")

        val commandFactory = CommandFactory()

        supportedCommands.forEach {
            val command = commandFactory.getCommand(listOf(Token(it, QuottingType.WITHOUT_QUOTE)))
            assertTrue(command.args.isEmpty())
            assertEquals(
                when (it) {
                    "pwd" -> PwdCommand::class
                    "wc" -> WcCommand::class
                    "cat" -> CatCommand::class
                    "echo" -> EchoCommand::class
                    "exit" -> ExitCommand::class
                    else -> throw RuntimeException()
                },
                command::class
            )
        }
    }

    @Test
    fun externalCommandTest() {
        val commandFactory = CommandFactory()
        val command = commandFactory.getCommand(listOf(Token("python", QuottingType.WITHOUT_QUOTE)))
        assertEquals(listOf("python"), command.args)
        assertEquals(ExternalCommand::class, command::class)
    }

    @Test
    fun incorrectAssignmentCommandTest() {
        val commandFactory = CommandFactory()
        val command = commandFactory.getCommand(listOf(Token("x=1", QuottingType.SINGLE_QUOTE)))
        assertEquals(ExternalCommand::class, command::class)
    }

    @Test
    fun correctAssignmentCommand() {
        val commandFactory = CommandFactory()
        val command = commandFactory.getCommand(listOf(Token("x=1", QuottingType.WITHOUT_QUOTE)))
        assertEquals(AssignmentCommand::class, command::class)
        assertEquals(listOf("x", "1"), command.args)
    }
}
