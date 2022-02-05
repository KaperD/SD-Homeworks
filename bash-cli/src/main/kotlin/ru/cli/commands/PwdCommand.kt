package ru.cli.commands

import java.io.InputStream
import java.io.OutputStream

class PwdCommand(private val args: List<String>) : Command {
    override fun execute(input: InputStream, out: OutputStream, error: OutputStream): ReturnCode {
        out.write(getCurrentDirectory().toByteArray())
        return ReturnCode.SUCCESS
    }

    private fun getCurrentDirectory(): String {
        return System.getProperty("user.dir")
    }
}
