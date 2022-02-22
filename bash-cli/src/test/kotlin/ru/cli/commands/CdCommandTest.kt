package ru.cli.commands

import org.junit.jupiter.api.Test
import ru.cli.Environment
import java.io.File
import java.io.PipedInputStream
import java.io.PipedOutputStream
import kotlin.test.assertEquals

class CdCommandTest {
    private data class Result(
        val output: String,
        val error: String,
        val returnCode: ReturnCode
    )

    private fun calculate(args: List<String>, environment: Environment): Result {
        val command = CdCommand(args)
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
    fun testCorrectCd() {
        val environment = Environment()
        val startDir = environment.currentPath
        val (output, error, returnCode) = calculate(listOf("src"), environment)
        assertEquals(ReturnCode(StatusCode.SUCCESS, 0), returnCode)
        assertEquals(0, output.length)
        assertEquals(0, error.length)
        assertEquals(startDir.resolve("src"), environment.currentPath)
    }

    @Test
    fun testCorrectCdToThisFolder() {
        val environment = Environment()
        val startDir = environment.currentPath
        val (output, error, returnCode) = calculate(listOf("."), environment)
        assertEquals(ReturnCode(StatusCode.SUCCESS, 0), returnCode)
        assertEquals(0, output.length)
        assertEquals(0, error.length)
        assertEquals(startDir, environment.currentPath)
    }

    @Test
    fun testCorrectHomeCd() {
        val environment = Environment()
        val (output, error, returnCode) = calculate(listOf(), environment)
        assertEquals(ReturnCode(StatusCode.SUCCESS, 0), returnCode)
        assertEquals(0, output.length)
        assertEquals(0, error.length)
        assertEquals(File(System.getProperty("user.home")), environment.currentPath)
    }

    @Test
    fun testCorrectWeirdCd() {
        val environment = Environment()
        val startDir = environment.currentPath
        val (output, error, returnCode) = calculate(listOf("././src/./main/../"), environment)
        assertEquals(ReturnCode(StatusCode.SUCCESS, 0), returnCode)
        assertEquals(0, output.length)
        assertEquals(0, error.length)
        assertEquals(startDir.resolve("src"), environment.currentPath)
    }

    @Test
    fun testCorrectAbsoluteCd() {
        val environment = Environment()
        val (output, error, returnCode) = calculate(listOf("/"), environment)
        assertEquals(ReturnCode(StatusCode.SUCCESS, 0), returnCode)
        assertEquals(0, output.length)
        assertEquals(0, error.length)
        assertEquals(File("/"), environment.currentPath)
    }

    @Test
    fun testWrongNumberOfArguments() {
        val environment = Environment()
        val startDir = environment.currentPath
        val (output, error, returnCode) = calculate(listOf("src", "a"), environment)
        assertEquals(ReturnCode(StatusCode.ERROR, 1), returnCode)
        assertEquals(0, output.length)
        assertEquals("Wrong number of args for cd command: expected 0 or 1${System.lineSeparator()}", error)
        assertEquals(startDir, environment.currentPath)
    }

    @Test
    fun testCdToFile() {
        val environment = Environment()
        val startDir = environment.currentPath
        val (output, error, returnCode) = calculate(listOf("README.md"), environment)
        assertEquals(ReturnCode(StatusCode.ERROR, 1), returnCode)
        assertEquals(0, output.length)
        assertEquals("README.md is not a directory${System.lineSeparator()}", error)
        assertEquals(startDir, environment.currentPath)
    }

    @Test
    fun testCdToNotExistingFile() {
        val environment = Environment()
        val startDir = environment.currentPath
        val (output, error, returnCode) = calculate(listOf("AoAoA"), environment)
        assertEquals(ReturnCode(StatusCode.ERROR, 1), returnCode)
        assertEquals(0, output.length)
        assertEquals("AoAoA no such file or directory${System.lineSeparator()}", error)
        assertEquals(startDir, environment.currentPath)
    }
}
