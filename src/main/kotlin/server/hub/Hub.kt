package net.orca.server.hub

import kotlinx.coroutines.delay
import net.minestom.server.MinecraftServer
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.instance.Instance
import net.orca.extension.addListener
import net.orca.extension.asComponent
import net.orca.extension.async
import net.orca.extension.launch

object Hub {
    private val instance: Instance

    init {
        val manager = MinecraftServer.getInstanceManager()
        instance = manager.createInstanceContainer()
    }

    fun instance(): Instance = instance

    fun register() {
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
}