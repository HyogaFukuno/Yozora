package net.orca.server.game.survivalgames

import io.github.oshai.kotlinlogging.KotlinLogging
import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.instance.Instance
import net.minestom.server.instance.anvil.AnvilLoader
import net.minestom.server.scoreboard.Sidebar
import net.minestom.server.timer.Task
import net.minestom.server.timer.TaskSchedule
import net.orca.extension.asComponent
import net.orca.server.game.survivalgames.state.GameState
import net.orca.server.game.survivalgames.state.StateLobby
import net.orca.server.game.survivalgames.task.SidebarTask
import net.orca.server.util.StateMachine

class GameContext(val gameId: Int) {

    private val logger = KotlinLogging.logger("GameContext[$gameId]")
    private val taskGameLoop = createGameLoopTask()
    private val taskSidebar = createSidebarTask()
    private val stateMachine by lazy { createStateMachine() }
    private val instanceLobby by lazy { createInstanceLobby() }

    var currentState = GameState.LOBBY
    val players = mutableSetOf<Player>()
    val spectators = mutableSetOf<Player>()
    val sidebar by lazy { createSidebar() }

    private fun createStateMachine(): StateMachine<GameContext, GameState> {
        return StateMachine<GameContext, GameState>(this).apply {
            registerState(StateLobby(this))
            requestKey(GameState.LOBBY)
        }
    }

    private fun createGameLoopTask(): Task {
        return MinecraftServer.getSchedulerManager().scheduleTask({
            stateMachine.update()
        }, TaskSchedule.immediate(), TaskSchedule.seconds(1L))
    }

    private fun createSidebarTask(): Task {
        logger.info { "createSidebarTask" }
        return MinecraftServer.getSchedulerManager().scheduleTask(SidebarTask(this),
            TaskSchedule.immediate(),
            TaskSchedule.seconds(1L))
    }

    private fun createInstanceLobby(): Instance {
        val manager = MinecraftServer.getInstanceManager()
        return manager.createInstanceContainer(AnvilLoader("worlds/sg-lobby"))
    }

    private fun createSidebar(): Sidebar {
        return Sidebar("&aLobby &c30:00".asComponent()).apply {
            createLine(Sidebar.ScoreboardLine("sg_sidebar_10", Component.empty(), 10))
            createLine(Sidebar.ScoreboardLine("sg_sidebar_09", Component.empty(), 9))
            createLine(Sidebar.ScoreboardLine("sg_sidebar_08", Component.empty(), 8))
            createLine(Sidebar.ScoreboardLine("sg_sidebar_07", Component.empty(), 7))
            createLine(Sidebar.ScoreboardLine("sg_sidebar_06", Component.empty(), 6))
            createLine(Sidebar.ScoreboardLine("sg_sidebar_05", Component.empty(), 5))
            createLine(Sidebar.ScoreboardLine("sg_sidebar_04", Component.empty(), 4))
            createLine(Sidebar.ScoreboardLine("sg_sidebar_03", Component.empty(), 3))
            createLine(Sidebar.ScoreboardLine("sg_sidebar_02", Component.empty(), 2))
            createLine(Sidebar.ScoreboardLine("sg_sidebar_01", Component.empty(), 1))
        }
    }

    fun getInstance(): Instance {
        if (currentState == GameState.LOBBY) {
            return instanceLobby
        }
        return instanceLobby
    }

    fun close() {
        players.forEach { it.kick("Server restarting") }
        spectators.forEach { it.kick("Server restarting") }

        taskGameLoop.cancel()
        taskSidebar.cancel()
    }
}