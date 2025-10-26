package net.orca.server.game.survivalgames.state

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.event.EventNode
import net.minestom.server.event.instance.AddEntityToInstanceEvent
import net.minestom.server.event.instance.RemoveEntityFromInstanceEvent
import net.minestom.server.instance.Instance
import net.minestom.server.instance.anvil.AnvilLoader
import net.orca.extension.addListener
import net.orca.extension.asComponent
import net.orca.extension.async
import net.orca.extension.launch
import net.orca.server.Yozora
import net.orca.server.game.survivalgames.GameContext
import net.orca.server.game.survivalgames.SurvivalGames.sidebar
import net.orca.server.util.StateMachine

class StateLobby(stateMachine: StateMachine<GameContext, GameState>
) : StateMachine.State<GameContext, GameState>(stateMachine, GameState.LOBBY) {

    private val instance: Instance by lazy { createInstance() }
    private val eventNodes = mutableListOf<EventNode<*>>()

    private fun createInstance(): Instance {
        val manager = MinecraftServer.getInstanceManager()
        return manager.createInstanceContainer(AnvilLoader("worlds/sg-lobby"))
    }

    override suspend fun enterAsync() {
        eventNodes.add(instance.eventNode().addListener<AddEntityToInstanceEvent>(Yozora.server()) { e ->
            val player = (e.entity as? Player)

            player?.launch {
                player.async {
                    // TODO: データベースから必要な情報を取得する（ランクなど）
                    player.displayName = "&9${player.username}".asComponent()
                }.await()

                e.instance.sendMessage { player.displayName!!.append(" &6has joined&8.".asComponent()) }
            }
        })
        eventNodes.add(instance.eventNode().addListener<RemoveEntityFromInstanceEvent>(Yozora.server()) { e ->
            val player = (e.entity as? Player)

            player?.launch {
                player.async {
                    // TODO: 更新された情報を保存する
                }

                e.instance.sendMessage { player.displayName!!.append(" &6has left&8.".asComponent()) }
            }
        })
    }

    override fun update() {

    }

    override suspend fun exitAsync() {

    }
}