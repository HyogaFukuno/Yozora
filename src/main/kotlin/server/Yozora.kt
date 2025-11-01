package net.orca.server

import io.github.oshai.kotlinlogging.KotlinLogging
import net.kyori.adventure.text.logger.slf4j.ComponentLogger
import net.minestom.server.MinecraftServer
import net.minestom.server.adventure.audience.Audiences
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.instance.InstanceManager
import net.minestom.server.instance.block.Block
import net.minestom.server.timer.SchedulerManager
import net.orca.Console
import net.orca.extension.addListener
import net.orca.extension.asComponent
import net.orca.extension.async
import net.orca.extension.launch
import net.orca.server.command.YozoraCommands
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.system.exitProcess

object Yozora {
    private val logger = KotlinLogging.logger(Yozora.javaClass.name)
    private val server: MinecraftServer = MinecraftServer.init()
    private val instanceManager: InstanceManager = MinecraftServer.getInstanceManager()
    private val schedulerManager: SchedulerManager = MinecraftServer.getSchedulerManager()
    private val eventHandler: GlobalEventHandler = MinecraftServer.getGlobalEventHandler()
    private val isRunning = AtomicBoolean(false)

    init {
        MinecraftServer.setBrandName("Yozora")
    }

    fun run() {
        isRunning.set(true)

        logger.info { "Starting Console..." }
        Console.start()

        logger.info { "Initializing Yozora..." }

        schedulerManager.buildShutdownTask {
            logger.info { "Good night." }
        }

        val instance = instanceManager.createInstanceContainer()
        instance.setGenerator { unit -> unit.modifier().fillHeight(0, 40, Block.STONE) }

        logger.info { "Registering Events..." }

        eventHandler.addListener<AsyncPlayerConfigurationEvent> { e ->
            val player = e.player
            e.spawningInstance = instance
            player.respawnPoint = Pos(0.0, 42.0, 0.0)
        }

        logger.info { "Registering Commands..." }
        YozoraCommands.register()

        logger.info { "Launching Yozora to 0.0.0.0:25565" }
        server.start("0.0.0.0", 25565)
    }

    fun stop() {
        if (isRunning.getAndSet(false)) {
            MinecraftServer.stopCleanly()
            Console.stop()
            exitProcess(0)
        }
    }

    fun server(): MinecraftServer {
        return server
    }
    fun isRunning(): Boolean {
        return isRunning.get()
    }
}