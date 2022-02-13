package ru.cli

import org.junit.jupiter.api.Test
import ru.cli.commands.AssignmentCommand
import ru.cli.commands.CatCommand
import ru.cli.commands.EchoCommand
import ru.cli.commands.ExitCommand
import ru.cli.commands.PwdCommand
import ru.cli.commands.StatusCode
import ru.cli.commands.WcCommand
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals

class PipeTest {
    @Test
    fun connectionTest() {
        val commands = listOf(PwdCommand(listOf()), CatCommand(listOf()))
        val pipe = Pipe(commands)
        val output = ByteArrayOutputStream()
        pipe.execute(out = output, environment = Environment())

        assertEquals("${System.getProperty("user.dir")}${System.lineSeparator()}", String(output.toByteArray()))
    }

    @Test
    fun nonStandardInput() {
        val commands = listOf(CatCommand(listOf()), WcCommand(listOf()))
        val pipe = Pipe(commands)
        val input = ByteArrayInputStream("bash".toByteArray())
        val output = ByteArrayOutputStream()
        pipe.execute(input, output, environment = Environment())

        assertEquals("       0       1       4", String(output.toByteArray()))
    }

    @Test
    fun exitPipeTest() {
        val beforeCommand = EchoCommand(listOf("bash"))
        val afterCommand = CatCommand(listOf())
        val output = ByteArrayOutputStream()
        val usualPipe = Pipe(listOf(beforeCommand, afterCommand))
        usualPipe.execute(environment = Environment(), out = output)

        assertEquals("bash${System.lineSeparator()}", String(output.toByteArray()))

        val exitPipe = Pipe(listOf(beforeCommand, ExitCommand(listOf()), afterCommand))
        output.reset()

        assertEquals(StatusCode.EXIT, exitPipe.execute(environment = Environment(), out = output).status)
        assertEquals("", String(output.toByteArray()))
    }

    @Test
    fun errorPipeTest() {
        val beforeCommand = EchoCommand(listOf("bash"))
        val afterCommand = CatCommand(listOf())
        val output = ByteArrayOutputStream()
        val usualPipe = Pipe(listOf(beforeCommand, afterCommand))
        usualPipe.execute(environment = Environment(), out = output)

        assertEquals("bash${System.lineSeparator()}", String(output.toByteArray()))

        val errorPipe = Pipe(listOf(beforeCommand, AssignmentCommand(listOf("x", "y", "z")), afterCommand))
        output.reset()

        val error = ByteArrayOutputStream()

        assertEquals(StatusCode.ERROR, errorPipe.execute(out = output, error = error, environment = Environment()).status)
        assertEquals("Wrong number of arguments", String(error.toByteArray()))
    }
}
