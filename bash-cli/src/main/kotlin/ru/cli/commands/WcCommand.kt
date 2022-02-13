package ru.cli.commands

import ru.cli.Environment
import java.io.File
import java.io.InputStream
import java.io.OutputStream

/**
 * This class provides `wc` unix command functionality.
 * `wc`(word count) is a utility that gives the following statistics: newline count, word count and byte count
 */
class WcCommand(override val args: List<String>) : Command {
    /**
     * Executes `wc` command with the specified arguments
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
            val text = String(input.readAllBytes())
            val statistics = getStatistics(text)
            writeStatistics(out, statistics)
            return ReturnCode(StatusCode.SUCCESS, 0)
        }

        val totalStatistics = Statistics()
        for (arg in args) {
            val text = String(File(arg).inputStream().readAllBytes())
            val statistics = getStatistics(text)
            totalStatistics += statistics
            writeStatistics(out, statistics, arg)
        }

        if (args.size == 1) {
            return ReturnCode(StatusCode.SUCCESS, 0)
        }

        writeStatistics(out, totalStatistics, "total")
        return ReturnCode(StatusCode.SUCCESS, 0)
    }

    private data class Statistics(
        var linesNumber: Int = 0,
        var wordsNumber: Int = 0,
        var bytesNumber: Int = 0,
    ) {
        operator fun plusAssign(other: Statistics) {
            linesNumber += other.linesNumber
            wordsNumber += other.wordsNumber
            bytesNumber += other.bytesNumber
        }
    }

    private fun toBrilliantString(integer: Int): String {
        val string = integer.toString()
        return " ".repeat(8 - string.length) + string
    }

    private fun writeStatistics(output: OutputStream, statistics: Statistics, label: String? = null) {
        output.write(toBrilliantString(statistics.linesNumber).toByteArray())
        output.write(toBrilliantString(statistics.wordsNumber).toByteArray())
        output.write(toBrilliantString(statistics.bytesNumber).toByteArray())
        if (label != null) {
            output.write(" $label${System.lineSeparator()}".toByteArray())
        }
    }

    private fun getStatistics(text: String): Statistics {
        return Statistics(
            getLinesNumber(text),
            getWordsNumber(text),
            getBytesNumber(text),
        )
    }

    private fun getLinesNumber(text: String): Int {
        return text.count { it == '\n' }
    }

    private fun getWordsNumber(text: String): Int {
        return text.split(' ', '\n').filter { it.isNotEmpty() }.size
    }

    private fun getBytesNumber(text: String): Int {
        return text.toByteArray().size
    }
}
