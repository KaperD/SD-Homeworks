package ru.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import ru.cli.Environment
import java.io.InputStream
import java.io.OutputStream

/**
 * This class provides `grep` unix command functionality.
 * `grep` is a utility for searching plain-text data sets for lines that match a regular expression
 */
class GrepCommand(override val args: List<String>) : Command {
    /**
     * Executes `grep` command
     *
     * @param input the input stream
     * @param out the output stream
     * @param error the error stream
     * @param environment the environment of process
     *
     * @return the execution code
     */
    override fun execute(
        input: InputStream,
        out: OutputStream,
        error: OutputStream,
        environment: Environment
    ): ReturnCode {
        return GrepCommandImpl(input, out, error, environment).let {
            it.main(args)
            it.returnCode
        }
    }

    private class GrepCommandImpl(
        input: InputStream,
        out: OutputStream,
        error: OutputStream,
        environment: Environment
    ) : CliktCommand() {
        val isInsetiveCase by option("-i").flag()
        val isFullMatch by option("-w").flag()
        val A: Int by option().int().default(0)

        lateinit var returnCode: ReturnCode

        override fun run() {
            TODO("Not yet implemented")
        }
    }
}
