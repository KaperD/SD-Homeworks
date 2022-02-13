package ru.cli.commands

import ru.cli.Environment
import java.io.InputStream
import java.io.OutputStream

/**
 * This class provides execution of any external command
 */
class ExternalCommand(override val args: List<String>) : Command {
    /**
     * Executes the specified external command
     *
     * @param input the input stream
     * @param out the output stream
     * @param error the error stream
     * @param environment the environment of process
     *
     * @return the execution code
     */
    override fun execute(input: InputStream, out: OutputStream, error: OutputStream, environment: Environment): ReturnCode {
        val processBuilder = ProcessBuilder(args)
        processBuilder.environment().let { env ->
            environment.vars.forEach {
                env[it.key] = it.value
            }
        }

        if (input == System.`in`) {
            processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT)
        }

        val process = processBuilder.start()
        if (input != System.`in`) {
            process.outputStream.write(input.readAllBytes())
            process.outputStream.flush()
        }

        process.inputStream.transferTo(out)
        process.errorStream.transferTo(error)

        val result = process.waitFor()
        if (result == 0) {
            return ReturnCode(StatusCode.SUCCESS, result)
        }

        return ReturnCode(StatusCode.ERROR, result)
    }
}
