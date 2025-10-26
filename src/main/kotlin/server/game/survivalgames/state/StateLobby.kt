package net.orca.server.game.survivalgames.state

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.future.await
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.event.instance.AddEntityToInstanceEvent
import net.minestom.server.event.instance.RemoveEntityFromInstanceEvent
import net.orca.extension.addListener
import net.orca.extension.asComponent
import net.orca.extension.async
import net.orca.extension.launch
import net.orca.server.Yozora
import net.orca.server.game.survivalgames.GameContext
import net.orca.server.util.StateMachine

class StateLobby(stateMachine: StateMachine<GameContext, GameState>
) : GameStateBase(stateMachine, GameState.LOBBY) {

    private val logger = KotlinLogging.logger("StateLobby")

    override suspend fun enterAsync() {

        remainTime = 120

        logger.info { "${key.displayName}.enterAsync" }
        val instance = stateMachine.context.getInstance()
        instance.eventNode().addListener<AddEntityToInstanceEvent>(Yozora.server()) { e ->
            val player = (e.entity as? Player)

            player?.gameMode = GameMode.CREATIVE
            player?.launch {
                player.teleport(Pos(0.5, 50.0, 0.5)).await()
                stateMachine.context.players.add(player)
                stateMachine.context.sidebar.addViewer(player)
                e.instance.sendMessage { player.displayName!!.append(" &6has joined&8.".asComponent()) }
            }
        }

        instance.eventNode().addListener<RemoveEntityFromInstanceEvent>(Yozora.server()) { e ->
            val player = (e.entity as? Player)

            player?.launch {
                player.async {
                    // TODO: 更新された情報を保存する
                }

                stateMachine.context.players.remove(player)
                stateMachine.context.sidebar.removeViewer(player)
                e.instance.sendMessage { player.displayName!!.append(" &6has left&8.".asComponent()) }
            }
        }
    }

    override fun update() {
        logger.info { "${key.displayName}.update" }

        if (remainTime <= 0) {
            remainTime = 120
        }

        remainTime--
        stateMachine.context.sidebar.title = sidebarTitle()
    }

    override suspend fun exitAsync() {
    }
}