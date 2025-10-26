package net.orca.server

import io.github.oshai.kotlinlogging.KotlinLogging
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.orca.extension.addListener
import net.orca.server.command.YozoraCommands
import net.orca.server.game.survivalgames.SurvivalGames
import net.orca.server.hub.Hub
import net.orca.server.util.TpsCalculator
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.system.exitProcess

@OptIn(ExperimentalAtomicApi::class)
object Yozora {
    private val logger = KotlinLogging.logger {}
    private val running = AtomicBoolean(false)
    private val server = MinecraftServer.init()

    fun server(): MinecraftServer = server
    fun isRunning(): Boolean = running.load()


    init {
        MinecraftServer.setBrandName("Yozora")
    }

    fun start() {
        running.store(true)

        task {
            logger.info { "Starting server..." }

            MinecraftServer.getSchedulerManager().buildShutdownTask {
                logger.info { "Good night." }
            }

            // コマンドの登録
            YozoraCommands.register()

            // Tps算出タスクの開始
            TpsCalculator().start()

            MinecraftServer.getGlobalEventHandler().addListener<AsyncPlayerConfigurationEvent> { e ->
                val player = e.player
                e.spawningInstance = Hub.instance()
                player.respawnPoint = Pos(-36.5, 162.0, 0.5)
                player.gameMode = GameMode.SURVIVAL
            }

            System.gc()

            server.start("0.0.0.0", 25565)
        }
    }

    fun stop() {
        if (running.compareAndSet(expectedValue = true, newValue = false)) {
            try {
                SurvivalGames.stopAllGame()
                MinecraftServer.stopCleanly()
                exitProcess(0)
            }catch (ex: Exception) {
                logger.error(ex) { "Failed to shutdown process." }
            }
        }
    }

    fun stopWithError(error: Throwable) {
        if (running.compareAndSet(expectedValue = true, newValue = false)) {
            error.printStackTrace()
            MinecraftServer.stopCleanly()
            exitProcess(0)
        }
    }

    private fun <T> task(task: () -> T) {
        try {
            task()
        } catch (error: Throwable) {
            stopWithError(error)
        }
    }
}