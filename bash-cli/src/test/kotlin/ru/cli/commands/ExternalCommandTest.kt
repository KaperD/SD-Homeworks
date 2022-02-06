package ru.cli.commands

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.io.PipedInputStream
import java.io.PipedOutputStream

class ExternalCommandTest {
    private fun calculate(args: List<String>): String {
        val command = ExternalCommand(args)
        val commandInput = PipedOutputStream()
        val commandOutput = PipedInputStream()
        val commandError = PipedInputStream()

        val input = PipedInputStream(commandInput)
        val out = PipedOutputStream(commandOutput)
        val error = PipedOutputStream(commandError)

        commandInput.close()
        command.execute(input, out, error)
        out.close()

        return String(commandOutput.readAllBytes())
    }

    @Test
    fun withoutArguments() {
        val expected = File("src/test/resources/python-print.out").readText()
        val tested = calculate(listOf("python3", "src/test/resources/print.py"))
        Assertions.assertEquals(expected, tested)
    }
}