package ru.cli.commands

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.PipedInputStream
import java.io.PipedOutputStream

class PwdCommandTest {
    private fun calculate(args: List<String>): String {
        val command = PwdCommand(args)
        val commandInput = PipedOutputStream()
        val commandOutput = PipedInputStream()
        val commandError = PipedInputStream()

        val input = PipedInputStream(commandInput)
        val out = PipedOutputStream(commandOutput)
        val error = PipedOutputStream(commandError)

        command.execute(input, out, error)
        out.close()

        return String(commandOutput.readAllBytes())
    }

    @Test
    fun withoutArguments() {
        val process = ProcessBuilder("pwd").start()
        process.waitFor()

        val expected = String(process.inputStream.readAllBytes())
        val tested = calculate(listOf())

        Assertions.assertEquals(expected, tested)
    }

    @Test
    fun withArguments() {
        val process = ProcessBuilder("pwd").start()
        process.waitFor()

        val expected = String(process.inputStream.readAllBytes())
        val tested = calculate(listOf("hse", "spb"))

        Assertions.assertEquals(expected, tested)
    }
}