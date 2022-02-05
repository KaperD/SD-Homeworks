package ru.cli.commands

import java.io.InputStream
import java.io.OutputStream

class ExternalCommand(private val args: List<String>) : Command {
    override fun execute(input: InputStream, out: OutputStream, error: OutputStream): ReturnCode {
        val processBuilder = ProcessBuilder(args)
        if (input == System.`in`) {
            processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT)
        }
        val process = processBuilder.start()
        if (input != System.`in`) {
            process.outputStream.write(input.readAllBytes())
            process.outputStream.close()
        }
        process.inputStream.transferTo(out)
        process.errorStream.transferTo(error)
        process.waitFor()
        return ReturnCode.SUCCESS
    }
}
