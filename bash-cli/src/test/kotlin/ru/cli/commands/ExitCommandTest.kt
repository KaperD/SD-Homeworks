package ru.cli.commands

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.PipedInputStream
import java.io.PipedOutputStream

class ExitCommandTest {
    private fun calculate(args: List<String>): List<String> {
        val command = ExitCommand(args)
        val commandInput = PipedOutputStream()
        val commandOutput = PipedInputStream()
        val commandError = PipedInputStream()

        val input = PipedInputStream(commandInput)
        val out = PipedOutputStream(commandOutput)
        val error = PipedOutputStream(commandError)

        command.execute(input, out, error,)
        input.close()
        out.close()
        error.close()

        return listOf(String(commandOutput.readAllBytes()), String(commandError.readAllBytes()))
    }

    @Test
    fun oneWordTest() {
        val expected = listOf("", "")

        val tested = calculate(listOf("rfvrev"))
        Assertions.assertEquals(expected, tested)
    }
}
