package ru.cli.commands

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.cli.Environment
import java.io.File
import java.io.PipedInputStream
import java.io.PipedOutputStream

class CatCommandTest {
    private fun calculate(args: List<String>, environment: Environment = Environment()): List<String> {
        val command = CatCommand(args)
        val commandInput = PipedOutputStream()
        val commandOutput = PipedInputStream()
        val commandError = PipedInputStream()

        val input = PipedInputStream(commandInput)
        val out = PipedOutputStream(commandOutput)
        val error = PipedOutputStream(commandError)

        command.execute(input, out, error, environment)
        input.close()
        out.close()
        error.close()

        return listOf(String(commandOutput.readAllBytes()), String(commandError.readAllBytes()))
    }

    @Test
    fun oneFileTest() {
        val expected = listOf(File("src/test/resources/forest-gump.txt").readText(Charsets.UTF_8), "")

        val tested = calculate(listOf("src/test/resources/forest-gump.txt"))
        Assertions.assertEquals(expected, tested)
    }

    @Test
    fun multipleFilesTest() {
        val expected = listOf(
            File("src/test/resources/forest-gump.txt").readText(Charsets.UTF_8) +
                File("src/test/resources/lorem-ipsum.txt").readText(Charsets.UTF_8),
            ""
        )
        val tested = calculate(listOf("src/test/resources/forest-gump.txt", "src/test/resources/lorem-ipsum.txt"))
        Assertions.assertEquals(expected, tested)
    }

    @Test
    fun oneFileErrorTest() {
        val expected = listOf("", "bwrg: No such file or directory")
        val tested = calculate(listOf("bwrg"))
        Assertions.assertEquals(expected, tested)
    }

    @Test
    fun multipleFilesErrorTest() {
        val expected = listOf(File("src/test/resources/forest-gump.txt").readText(Charsets.UTF_8), "bwrg: No such file or directory")
        val tested = calculate(listOf("src/test/resources/forest-gump.txt", "bwrg"))
        Assertions.assertEquals(expected, tested)
    }

    @Test
    fun useCurrentPathTest() {
        val expected = listOf(File("src/test/resources/forest-gump.txt").readText(Charsets.UTF_8), "")

        val tested = calculate(listOf("resources/forest-gump.txt"), Environment(workingDir = File("src/test")))
        Assertions.assertEquals(expected, tested)
    }
}
