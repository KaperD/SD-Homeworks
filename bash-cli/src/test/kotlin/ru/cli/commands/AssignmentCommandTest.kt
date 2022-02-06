package ru.cli.commands

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.cli.Environment.vars
import java.io.PipedInputStream
import java.io.PipedOutputStream

class AssignmentCommandTest {
    private fun calculate(args: List<String>): List<String> {
        val command = AssignmentCommand(args)
        val commandInput = PipedOutputStream()
        val commandOutput = PipedInputStream()
        val commandError = PipedInputStream()

        val input = PipedInputStream(commandInput)
        val out = PipedOutputStream(commandOutput)
        val error = PipedOutputStream(commandError)

        command.execute(input, out, error)
        input.close()
        out.close()
        error.close()

        return listOf(String(commandOutput.readAllBytes()), String(commandError.readAllBytes()))
    }

    @BeforeEach
    fun clearEnvironment() {
        vars.clear()
    }

    @Test
    fun oneAssignmentTest() {
        val expected = listOf("", "")
        val tested = calculate(listOf("a", "2"))

        Assertions.assertEquals(vars, mapOf(Pair("a", "2")))
        Assertions.assertEquals(expected, tested)
    }

    @Test
    fun multipleAssignmentTest() {
        calculate(listOf("a", "2"))
        calculate(listOf("a", "3"))

        val expected = listOf("", "")
        val tested = calculate(listOf("b", "4"))

        Assertions.assertEquals(vars, mapOf(Pair("a", "3"), Pair("b", "4")))
        Assertions.assertEquals(expected, tested)
    }

    @Test
    fun errorAssignmentTest() {
        val expected = listOf("", "Wrong number of arguments")
        val tested = calculate(listOf("a"))
        Assertions.assertEquals(expected, tested)
    }
}
