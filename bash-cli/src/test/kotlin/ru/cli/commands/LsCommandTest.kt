package ru.cli.commands

import org.junit.jupiter.api.Test
import ru.cli.Environment
import java.io.File
import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.lang.System.lineSeparator
import kotlin.test.assertEquals

class LsCommandTest {
    private data class Result(
        val output: String,
        val error: String,
        val returnCode: ReturnCode
    )

    private fun calculate(args: List<String>, environment: Environment): Result {
        val command = LsCommand(args)
        val commandInput = PipedOutputStream()
        val commandOutput = PipedInputStream()
        val commandError = PipedInputStream()

        val input = PipedInputStream(commandInput)
        val out = PipedOutputStream(commandOutput)
        val error = PipedOutputStream(commandError)

        val returnCode = command.execute(input, out, error, environment)
        input.close()
        out.close()
        error.close()

        return Result(String(commandOutput.readAllBytes()), String(commandError.readAllBytes()), returnCode)
    }

    @Test
    fun testLsWithoutArgs() {
        val environment = Environment(workingDir = File("src/main/kotlin"))
        val (output, error, returnCode) = calculate(listOf(), environment)
        assertEquals(ReturnCode(StatusCode.SUCCESS, 0), returnCode)
        assertEquals("ru${lineSeparator()}", output)
        assertEquals(0, error.length)
    }

    @Test
    fun testLsToThisFolder() {
        val environment = Environment(workingDir = File("src/main/kotlin"))
        val (output, error, returnCode) = calculate(listOf("."), environment)
        assertEquals(ReturnCode(StatusCode.SUCCESS, 0), returnCode)
        assertEquals("ru${lineSeparator()}", output)
        assertEquals(0, error.length)
    }

    @Test
    fun testWeirdLsToFolder() {
        val environment = Environment()
        val (output, error, returnCode) = calculate(listOf("././src/./main/../main/kotlin"), environment)
        assertEquals(ReturnCode(StatusCode.SUCCESS, 0), returnCode)
        assertEquals("ru${lineSeparator()}", output)
        assertEquals(0, error.length)
    }

    @Test
    fun testAbsoluteLs() {
        val environment = Environment()
        val (output, error, returnCode) = calculate(listOf("/"), environment)
        assertEquals(ReturnCode(StatusCode.SUCCESS, 0), returnCode)
        val expectedOutput = File("/").listFiles()
            ?.map { it.name }
            ?.joinToString(separator = lineSeparator(), postfix = lineSeparator())
        assertEquals(expectedOutput, output)
        assertEquals(0, error.length)
    }

    @Test
    fun testWrongNumberOfArguments() {
        val environment = Environment()
        val (output, error, returnCode) = calculate(listOf("src", "a"), environment)
        assertEquals(ReturnCode(StatusCode.ERROR, 1), returnCode)
        assertEquals(0, output.length)
        assertEquals("Wrong number of args for ls command: expected 0 or 1${lineSeparator()}", error)
    }

    @Test
    fun testLsToFile() {
        val environment = Environment()
        val (output, error, returnCode) = calculate(listOf("././README.md"), environment)
        assertEquals(ReturnCode(StatusCode.SUCCESS, 0), returnCode)
        assertEquals("././README.md${lineSeparator()}", output)
        assertEquals(0, error.length)
    }

    @Test
    fun testLsOfNotExistingFile() {
        val environment = Environment()
        val (output, error, returnCode) = calculate(listOf("AoAoA"), environment)
        assertEquals(ReturnCode(StatusCode.ERROR, 1), returnCode)
        assertEquals(0, output.length)
        assertEquals("AoAoA no such file or directory${lineSeparator()}", error)
    }
}
