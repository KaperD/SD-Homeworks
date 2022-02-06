package ru.cli.commands

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.PipedInputStream
import java.io.PipedOutputStream
import kotlin.io.path.*

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
        val expected = Path("src", "test", "resources", "wc-empty-file.out").readText()
        val tested = calculate(listOf(Path("src", "test", "resources", "empty-file.txt").pathString))
        Assertions.assertEquals(expected, tested)
    }

    @Test
    fun loremIpsumTest() {
        val expected = Path("src", "test", "resources", "wc-lorem-ipsum.out").readText()
        val tested = calculate(listOf(Path("src", "test", "resources", "lorem-ipsum.txt").pathString))
        Assertions.assertEquals(expected, tested)
    }

    @Test
    fun twoFilesTest() {
        val expected = Path("src", "test", "resources", "wc-two-files.out").readText()
        val tested = calculate(listOf(
            Path("src", "test", "resources", "lorem-ipsum.txt").pathString,
            Path("src", "test", "resources", "forest-gump.txt").pathString
        ))
        Assertions.assertEquals(expected, tested)
    }
}
