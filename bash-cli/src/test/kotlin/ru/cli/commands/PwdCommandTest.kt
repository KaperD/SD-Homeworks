package ru.cli.commands

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.cli.Environment
import java.io.File
import java.io.PipedInputStream
import java.io.PipedOutputStream

class PwdCommandTest {
    private fun calculate(args: List<String>, environment: Environment = Environment()): String {
        val command = PwdCommand(args)
        val commandInput = PipedOutputStream()
        val commandOutput = PipedInputStream()
        val commandError = PipedInputStream()

        val input = PipedInputStream(commandInput)
        val out = PipedOutputStream(commandOutput)
        val error = PipedOutputStream(commandError)

        command.execute(input, out, error, environment)
        out.close()

        return String(commandOutput.readAllBytes())
    }

    @Test
    fun withoutArguments() {
        val expected = System.getProperty("user.dir") + System.lineSeparator()
        val tested = calculate(listOf())
        Assertions.assertEquals(expected, tested)
    }

    @Test
    fun withArguments() {
        val expected = System.getProperty("user.dir") + System.lineSeparator()
        val tested = calculate(listOf("hse", "spb"))
        Assertions.assertEquals(expected, tested)
    }

    @Test
    fun differentEnvironment() {
        val environment = Environment(currentPath = File("src"))
        val tested = calculate(listOf(), environment = environment)
        Assertions.assertEquals(environment.currentPath.path + System.lineSeparator(), tested)
    }
}
