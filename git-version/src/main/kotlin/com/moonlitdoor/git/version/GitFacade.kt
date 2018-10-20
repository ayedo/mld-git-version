package com.moonlitdoor.git.version

import org.apache.tools.ant.taskdefs.condition.Os
import java.io.File
import java.util.concurrent.TimeUnit

open class GitFacade {

    open fun runCommandOsIndependent(projectDir: File, vararg command: String): String {
        return if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            val singleCommand = command.joinToString(" ")
            val windowsCommand = arrayOf("cmd", "/c", singleCommand)
            runCommand(windowsCommand, projectDir)
        } else {
            return runCommand(command, projectDir)
        }
    }

    private fun runCommand(command: Array<out String>, projectDir: File): String {
        val process = ProcessBuilder(*command)
            .directory(projectDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()
        process.waitFor(60, TimeUnit.SECONDS)
        return process.inputStream.bufferedReader().readText().trim()
    }
}