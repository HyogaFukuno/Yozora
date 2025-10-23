package net.orca.server

import de.articdive.jnoise.generators.noisegen.opensimplex.FastSimplexNoiseGenerator
import de.articdive.jnoise.pipeline.JNoise
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.delay
import net.minestom.server.Auth
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.instance.block.Block
import net.orca.extension.addListener
import net.orca.extension.asComponent
import net.orca.extension.async
import net.orca.extension.launch
import net.orca.server.commands.YozoraCommands
import net.orca.server.util.TpsCalculator
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.system.exitProcess

@OptIn(ExperimentalAtomicApi::class)
class Yozora {
    companion object {
        private var instance: Yozora? = null

        fun instance(): Yozora {
            return instance!!
        }
    }

    private val logger = KotlinLogging.logger {}
    private val server = MinecraftServer.init(Auth.Online())
    private val running = AtomicBoolean(false)

    init {
        MinecraftServer.setBrandName("Yozora")
        instance = this
    }

    fun server(): MinecraftServer {
        return server
    }

    fun start() {
        running.store(true)

        task {
            logger.info { "Starting server..." }

            MinecraftServer.getSchedulerManager().buildShutdownTask {
                logger.info { "Good night." }
            }

            val instanceManager = MinecraftServer.getInstanceManager()
            val instance = instanceManager.createInstanceContainer()

            val noise = JNoise.newBuilder()
                .fastSimplex(FastSimplexNoiseGenerator.newBuilder().build())
                .scale(0.005)
                .build()

            instance.setGenerator { unit ->
                val start = unit.absoluteStart()
                for (x in 0 until unit.size().blockX()) {
                    for (z in 0 until unit.size().blockZ()) {
                        val bottom = start.add(x.toDouble(), 0.0, z.toDouble())

                        synchronized(noise) {
                            val height = noise.evaluateNoise(bottom.x(), bottom.z()) * 16
                            unit.modifier().fill(bottom, bottom.add(1.0, 0.0, 1.0).withY(height), Block.STONE)
                        }
                    }
                }
            }
            instance.setBlock(0, 30, 0, Block.CHEST)

            // コマンドの登録
            YozoraCommands.register()

            // Tps算出タスクの開始
            TpsCalculator().start()

            MinecraftServer.getGlobalEventHandler().addListener<AsyncPlayerConfigurationEvent> { e ->
                val player = e.player
                e.spawningInstance = instance
                player.respawnPoint = Pos(0.0, 42.0, 0.0)
                player.gameMode = GameMode.CREATIVE

                player.launch {
                    player.async {
                        player.sendMessage { "Hello World!".asComponent }
                        delay(1000L)
                        player.sendMessage { player.name }
                    }.await()
                    player.sendMessage { "await".asComponent }
                }
            }

            System.gc()

            server.start("0.0.0.0", 25565)
        }
    }

    fun stop() {
        if (running.compareAndSet(expectedValue = true, newValue = false)) {
            try {
                MinecraftServer.stopCleanly()
                Thread.sleep(500L)
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