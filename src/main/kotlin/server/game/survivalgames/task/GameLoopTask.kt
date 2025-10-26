package net.orca.server.game.survivalgames.task

import net.minestom.server.MinecraftServer
import net.orca.extension.asComponent

/**
 * Survival Gamesのメインゲームループ。
 */
class GameLoopTask : Runnable {
    override fun run() {
        MinecraftServer.getConnectionManager().onlinePlayers.forEach { it.sendMessage { "Hello from GameLoop!".asComponent() } }
    }
}