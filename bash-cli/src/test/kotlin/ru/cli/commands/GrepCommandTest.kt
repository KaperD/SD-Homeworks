package ru.cli.commands

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.cli.Environment
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream
import kotlin.io.path.Path
import kotlin.io.path.pathString
import kotlin.io.path.readText

class GrepCommandTest {
    private fun calculate(
        args: List<String>,
        trueInputStream: InputStream? = null,
        environment: Environment = Environment()
    ): String {
        val command = GrepCommand(args)
        val commandOutput = PipedInputStream()
        val commandError = PipedInputStream()
        val commandInput = PipedOutputStream()

        val input = PipedInputStream(commandInput)
        val out = PipedOutputStream(commandOutput)
        val error = PipedOutputStream(commandError)

        if (trueInputStream == null) {
            command.execute(input, out, error, environment)
        } else {
            command.execute(trueInputStream, out, error, environment)
        }

        out.close()

        return String(commandOutput.readAllBytes())
    }

    @Test
    fun simpleFileTest() {
        val pathInput = Path("src", "test", "resources", "grep-test-01.in")
        val expectedOutputString = Path("src", "test", "resources", "grep-test-01.out").readText()
        val actualOutputString = calculate(listOf("lol|aboba", pathInput.pathString))
        Assertions.assertEquals(expectedOutputString, actualOutputString)
    }

    @Test
    fun simpleStreamTest() {
        val inputString = Path("src", "test", "resources", "grep-test-01.out").readText()
        val expectedOutputString = Path("src", "test", "resources", "grep-test-01.out").readText()
        val actualOutputString = calculate(listOf("lol|aboba"), ByteArrayInputStream(inputString.toByteArray()))
        Assertions.assertEquals(expectedOutputString, actualOutputString)
    }

    @Test
    fun simpleStreamTestI() {
        val inputString = Path("src", "test", "resources", "grep-test-01.out").readText()
        val expectedOutputString = Path("src", "test", "resources", "grep-test-01.out").readText()
        val actualOutputString = calculate(listOf("loL|aboba", "-i"), ByteArrayInputStream(inputString.toByteArray()))
        Assertions.assertEquals(expectedOutputString, actualOutputString)
    }

    @Test
    fun simpleStreamTestW() {
        val inputString = Path("src", "test", "resources", "grep-test-01.out").readText()
        val expectedOutputString = "lol" + System.lineSeparator()
        val actualOutputString = calculate(listOf("lol|ab", "-w"), ByteArrayInputStream(inputString.toByteArray()))
        Assertions.assertEquals(expectedOutputString, actualOutputString)
    }

    @Test
    fun flagAWithIntersection() {
        val input = Path("src", "test", "resources", "grep-test-flag-a.in").readText()
        val expected = Path("src", "test", "resources", "grep-test-flag-a.out").readText()
        val tested = calculate(listOf("lol", "-A", "1"), ByteArrayInputStream(input.toByteArray()))
        Assertions.assertEquals(expected, tested)
    }

    @Test
    fun useCurrentPathTest() {
        val pathInput = Path("resources", "grep-test-01.in")
        val expectedOutputString = Path("src", "test", "resources", "grep-test-01.out").readText()
        val actualOutputString = calculate(
            listOf("lol|aboba", pathInput.pathString),
            environment = Environment(workingDir = File("src/test"))
        )
        Assertions.assertEquals(expectedOutputString, actualOutputString)
    }
}
