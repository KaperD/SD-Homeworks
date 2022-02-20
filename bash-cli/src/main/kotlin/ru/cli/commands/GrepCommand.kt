package ru.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
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
        lateinit var returnCode: ReturnCode
        object : CliktCommand() {
            val isInsetiveCase by option("-i").flag()
            val isFullMatch by option("-w").flag()
            val A: Int by option().int().default(0)
            val regexStr by argument()
            val sources by argument().file().multiple()

            override fun run() {
                findLines(input, out, regexStr.toRegex(), A)
                returnCode = ReturnCode(StatusCode.SUCCESS, 0)
            }

        }.main(args)

        return returnCode
    }

    fun findLines(input: InputStream, output: OutputStream, regex: Regex, contextSize: Int) {
        input.bufferedReader().useLines { lines ->
            output.bufferedWriter().use { out ->
                var curUnprintedContextSize = 0
                for (line in lines) {
                    if (regex.containsMatchIn(line)) {
                        curUnprintedContextSize = contextSize + 1
                    }
                    if (curUnprintedContextSize > 0) {
                        out.write(line)
                        out.newLine()
                        curUnprintedContextSize--
                    }
                }
            }
        }
    }
}
