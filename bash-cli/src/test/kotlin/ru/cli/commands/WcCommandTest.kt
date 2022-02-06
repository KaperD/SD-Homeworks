package ru.cli.commands

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.io.PipedInputStream
import java.io.PipedOutputStream

class WcCommandTest {
    private fun calculate(files: List<String>): String {
        val command = WcCommand(files)
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
    fun emptyFileTest() {
        val expected = File("src/test/resources/wc-empty-file.out").readText()
        val tested = calculate(listOf("src/test/resources/empty-file.txt"))
        Assertions.assertEquals(expected, tested)
    }

    @Test
    fun loremIpsumTest() {
        val expected = File("src/test/resources/wc-lorem-ipsum.out").readText()
        val tested = calculate(listOf("src/test/resources/lorem-ipsum.txt"))
        Assertions.assertEquals(expected, tested)
    }

    @Test
    fun twoFilesTest() {
        val expected = File("src/test/resources/wc-two-files.out").readText()
        val tested = calculate(listOf("src/test/resources/lorem-ipsum.txt", "src/test/resources/forest-gump.txt"))
        Assertions.assertEquals(expected, tested)
    }
}
