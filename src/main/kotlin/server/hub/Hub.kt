package net.orca.server.hub

import de.articdive.jnoise.generators.noisegen.opensimplex.FastSimplexNoiseGenerator
import de.articdive.jnoise.pipeline.JNoise
import kotlinx.coroutines.delay
import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.instance.Instance
import net.minestom.server.instance.anvil.AnvilLoader
import net.minestom.server.instance.block.Block
import net.minestom.server.scoreboard.Sidebar
import net.orca.extension.addListener
import net.orca.extension.asComponent
import net.orca.extension.async
import net.orca.extension.launch

object Hub {
    private val instance: Instance by lazy { createInstance() }

    private fun createInstance(): Instance {
        val manager = MinecraftServer.getInstanceManager()
        return manager.createInstanceContainer().apply {
            chunkLoader = AnvilLoader("worlds/hub")
        }
    }

    fun register() {
        instance.eventNode().addListener<PlayerSpawnEvent> { e ->
            e.player.sendMessage { Component.text("Spawn!") }
        }
        instance.eventNode().addListener<PlayerChatEvent> { e ->
            val player = e.player
            e.formattedMessage
        }
    }

    fun getInstance() = instance
}