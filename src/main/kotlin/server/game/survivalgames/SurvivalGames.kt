package net.orca.server.game.survivalgames

import java.util.concurrent.ConcurrentHashMap

object SurvivalGames {
    private val gameContexts = ConcurrentHashMap<Int, GameContext>()

    fun getGameContext(gameId: Int): GameContext {
        return gameContexts.computeIfAbsent(gameId) { GameContext(it) }
    }

    fun stopGame(gameId: Int) {
        val context = gameContexts.remove(gameId)
        context?.close()
    }

    fun stopAllGame() {
        gameContexts.values.forEach { it.close() }
        gameContexts.clear()
    }
}