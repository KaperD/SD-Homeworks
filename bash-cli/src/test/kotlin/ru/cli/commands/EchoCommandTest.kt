package ru.cli.commands

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.PipedInputStream
import java.io.PipedOutputStream

class EchoCommandTest {
    private fun calculate(args: List<String>): List<String> {
        val command = EchoCommand(args)
        val commandInput = PipedOutputStream()
        val commandOutput = PipedInputStream()
        val commandError = PipedInputStream()

        val input = PipedInputStream(commandInput)
        val out = PipedOutputStream(commandOutput)
        val error = PipedOutputStream(commandError)

        command.execute(input, out, error)
        input.close()
        out.close()
        error.close()

        return listOf(String(commandOutput.readAllBytes()), String(commandError.readAllBytes()))
    }

    @Test
    fun oneWordTest() {
        val expected = listOf("rfvrev" + System.lineSeparator(), "")

        val tested = calculate(listOf("rfvrev"))
        Assertions.assertEquals(expected, tested)
    }

    @Test
    fun multipleWordTest() {
        val expected = listOf("rfvrev eetetg" + System.lineSeparator(), "")

        val tested = calculate(listOf("rfvrev", "eetetg"))
        Assertions.assertEquals(expected, tested)
    }
}
