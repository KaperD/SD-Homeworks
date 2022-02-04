package ru.cli.commands

import java.io.InputStream
import java.io.OutputStream

enum class ReturnCode {
    SUCCESS,
    ERROR,
    EXIT
}

interface Command {
    fun execute(
        input: InputStream = System.`in`,
        out: OutputStream = System.out,
        error: OutputStream = System.err
    ): ReturnCode
}
