package ru.cli.commands

import ru.cli.Environment
import java.io.InputStream
import java.io.OutputStream

/**
 * This class provides `cat` unix command functionality.
 * `cat` is a standard Unix utility that reads files sequentially, writing them to standard output
 */
class CatCommand(override val args: List<String>) : Command {
    /**
     * Executes `cat` command
     *
     * @param input the input stream
     * @param out the output stream
     * @param error the error stream
     * @param environment the environment of process
     *
     * @return the execution code
     */
    override fun execute(input: InputStream, out: OutputStream, error: OutputStream, environment: Environment): ReturnCode {
        if (args.isEmpty()) {
            out.write(input.readAllBytes())
            return ReturnCode(StatusCode.SUCCESS, 0)
        }

        for (filename in args) {
            val file = environment.workingDir.resolve(filename)
            if (file.exists()) {
                out.write(file.readBytes())
            } else {
                error.write(("$filename: No such file or directory").toByteArray())
                return ReturnCode(StatusCode.ERROR, 1)
            }
        }
        return ReturnCode(StatusCode.SUCCESS, 0)
    }
}
