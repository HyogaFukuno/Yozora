package net.orca

import io.github.oshai.kotlinlogging.KotlinLogging
import net.minecrell.terminalconsole.SimpleTerminalConsole
import net.minecrell.terminalconsole.TerminalConsoleAppender
import net.minestom.server.MinecraftServer
import net.orca.server.Yozora
import java.util.concurrent.atomic.AtomicBoolean

object Console : SimpleTerminalConsole() {
    private val isRunning = AtomicBoolean(false)
    private val logger = KotlinLogging.logger(Console.javaClass.name)
    private val thread = Thread { super.start() }

    init {
        TerminalConsoleAppender.isAnsiSupported()
    }

    override fun start() {
        thread.isDaemon = true
        thread.start()
    }

    fun stop() {
        if (isRunning.getAndSet(false)) {
            thread.interrupt()
        }
    }

    override fun isRunning(): Boolean {
        return isRunning.get()
    }

    override fun runCommand(command: String?) {
        command?.let {
            logger.info { "/$it" }
            MinecraftServer.getCommandManager().executeServerCommand(it)
        }
    }

    override fun shutdown() {
        Yozora.stop()
    }
}