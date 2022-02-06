package ru.cli.commands

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.PipedInputStream
import java.io.PipedOutputStream
import kotlin.io.path.Path
import kotlin.io.path.pathString
import kotlin.io.path.readText

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
        val os = System.getProperty("os.name")
        val expected = if (os.indexOf("win", ignoreCase = true) >= 0) {
            Path("src", "test", "resources", "wc-windows-empty-file.out").readText()
        } else {
            Path("src", "test", "resources", "wc-unix-empty-file.out").readText()
        }

        val tested = calculate(listOf(Path("src", "test", "resources", "empty-file.txt").pathString))
        Assertions.assertEquals(expected, tested)
    }

    @Test
    fun loremIpsumTest() {
        val os = System.getProperty("os.name")
        val expected = if (os.indexOf("win", ignoreCase = true) >= 0) {
            Path("src", "test", "resources", "wc-windows-lorem-ipsum.out").readText()
        } else {
            Path("src", "test", "resources", "wc-unix-lorem-ipsum.out").readText()
        }

        val tested = calculate(listOf(Path("src", "test", "resources", "lorem-ipsum.txt").pathString))
        Assertions.assertEquals(expected, tested)
    }

    @Test
    fun twoFilesTest() {
        val os = System.getProperty("os.name")
        val expected = if (os.indexOf("win", ignoreCase = true) >= 0) {
            Path("src", "test", "resources", "wc-windows-two-files.out").readText()
        } else {
            Path("src", "test", "resources", "wc-unix-two-files.out").readText()
        }

        val tested = calculate(
            listOf(
                Path("src", "test", "resources", "lorem-ipsum.txt").pathString,
                Path("src", "test", "resources", "forest-gump.txt").pathString
            )
        )
        Assertions.assertEquals(expected, tested)
    }
}
