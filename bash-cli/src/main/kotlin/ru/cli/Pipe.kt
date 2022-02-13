package ru.cli

import ru.cli.commands.Command
import ru.cli.commands.ReturnCode
import ru.cli.commands.StatusCode
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream

/**
 * This class provides methods for piping list of commands
 */
class Pipe(val commands: List<Command>) {
    /**
     * Execute list of commands, so that output of previous command is input for next command
     *
     * @param input the input stream
     * @param out the output stream
     * @param error the error stream
     * @param environment the environment of process
     *
     * @return the execution code
     */
    fun execute(
        input: InputStream = System.`in`,
        out: OutputStream = System.out,
        error: OutputStream = System.err,
        environment: Environment
    ): ReturnCode {
        var tmpInput = ByteArrayInputStream(
            when (input) {
                System.`in` -> byteArrayOf()
                else -> input.readAllBytes()
            }
        )
        val tmpOutput = ByteArrayOutputStream()

        commands.forEach { command ->
            tmpOutput.reset()

            val returnCode = command.execute(tmpInput, tmpOutput, error, environment)

            when (returnCode.status) {
                StatusCode.SUCCESS -> Unit
                else -> return returnCode
            }

            tmpInput = ByteArrayInputStream(tmpOutput.toByteArray())
        }

        out.write(tmpOutput.toByteArray())

        return ReturnCode(StatusCode.SUCCESS, 0)
    }
}
