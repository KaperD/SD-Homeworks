package ru.cli.commands

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.io.PipedInputStream
import java.io.PipedOutputStream

class ExternalCommandTest {
    private fun calculate(args: List<String>): Pair<String, String> {
        val command = ExternalCommand(args)
        val commandInput = PipedOutputStream()
        val commandOutput = PipedInputStream()
        val commandError = PipedInputStream()

        val input = PipedInputStream(commandInput)
        val out = PipedOutputStream(commandOutput)
        val error = PipedOutputStream(commandError)

        commandInput.close()
        command.execute(input, out, error)
        input.close()
        out.close()
        error.close()

        return Pair(String(commandOutput.readAllBytes()), String(commandError.readAllBytes()))
    }

    @Test
    fun pythonPrint() {
        val expected = File("src/test/resources/python-print.out").readText()
        val tested = calculate(listOf("python3", "src/test/resources/print.py")).first
        Assertions.assertEquals(expected, tested)
    }

//    @Test
//    fun pythonError() {
//        val expected = File("src/test/resources/python-error.err").readText()
//        val tested = calculate(listOf("python3", "src/test/resources/script-with-error.py")).second
//        Assertions.assertEquals(expected, tested)
//    }
}
