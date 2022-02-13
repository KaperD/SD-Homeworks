package ru.cli.commands

import java.io.InputStream
import java.io.OutputStream

/**
 * Status of command execution
 */
enum class StatusCode {
    SUCCESS,
    ERROR,
    EXIT
}

/**
 * Define result of command execution
 */
data class ReturnCode(val status: StatusCode, val code: Int)

/**
 * Provides interface for all supported commands
 */
interface Command {
    val args: List<String>
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
