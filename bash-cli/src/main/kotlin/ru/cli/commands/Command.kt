package ru.cli.commands

import java.io.InputStream
import java.io.OutputStream

/**
 * Define result of command execution
 */
enum class ReturnCode {
    SUCCESS,
    ERROR,
    EXIT
}

/**
 * Provides interface for all supported commands
 */
interface Command {
    /**
     * Executes command
     *
     * @param input the input stream
     * @param out the output stream
     * @param error the error stream
     *
     * @return the execution code
     */
    fun execute(
        input: InputStream = System.`in`,
        out: OutputStream = System.out,
        error: OutputStream = System.err
    ): ReturnCode
}
