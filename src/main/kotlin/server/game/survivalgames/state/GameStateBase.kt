package net.orca.server.game.survivalgames.state

import net.kyori.adventure.text.Component
import net.orca.extension.asComponent
import net.orca.server.game.survivalgames.GameContext
import net.orca.server.util.StateMachine

abstract class GameStateBase(stateMachine: StateMachine<GameContext, GameState>, key: GameState
) : StateMachine.State<GameContext, GameState>(stateMachine, key) {
    protected var remainTime = 0

    protected fun sidebarTitle(): Component {
        val minutes = remainTime / 60
        val seconds = remainTime % 60
        return "&a${key.displayName} &c$minutes:${"%02d".format(seconds)}".asComponent()
    }
}