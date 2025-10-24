package net.orca.server.hub

import de.articdive.jnoise.generators.noisegen.opensimplex.FastSimplexNoiseGenerator
import de.articdive.jnoise.pipeline.JNoise
import kotlinx.coroutines.delay
import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.instance.Instance
import net.minestom.server.instance.block.Block
import net.minestom.server.scoreboard.Sidebar
import net.orca.extension.addListener
import net.orca.extension.asComponent
import net.orca.extension.async
import net.orca.extension.launch

class Hub {
    companion object {
        private var INSTANCE: Hub? = null

        fun instance(): Hub {
            return INSTANCE!!
        }
    }

    private val instance: Instance
    private val sidebar: Sidebar

    init {
        INSTANCE = this
        instance = MinecraftServer.getInstanceManager().createInstanceContainer()
        val noise = JNoise.newBuilder()
            .fastSimplex(FastSimplexNoiseGenerator.newBuilder().build())
            .scale(0.01)
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

        sidebar = Sidebar(Component.text("Hub Sidebar"))
        sidebar.createLine(Sidebar.ScoreboardLine("hub_sidebar_01", Component.text("Line 1"), 1))
    }

    fun register() {
        instance.eventNode().addListener<PlayerSpawnEvent> { e ->
            e.player.sendMessage { Component.text("Spawn!") }
            sidebar.addViewer(e.player)
        }
        instance.eventNode().addListener<PlayerChatEvent> { e ->
            val player = e.player

            player.launch {
                player.async {
                    player.sendMessage { "Hello World!".asComponent }
                    delay(1000L)
                    player.sendMessage { player.name }
                }.await()
                player.sendMessage { "await".asComponent }
            }
        }
    }

    fun getInstance() = instance
}