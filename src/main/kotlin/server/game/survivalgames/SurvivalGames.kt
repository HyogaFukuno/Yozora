package net.orca.server.game.survivalgames

import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.event.instance.AddEntityToInstanceEvent
import net.minestom.server.event.instance.RemoveEntityFromInstanceEvent
import net.minestom.server.instance.Instance
import net.minestom.server.instance.anvil.AnvilLoader
import net.minestom.server.scoreboard.Sidebar
import net.minestom.server.timer.Task
import net.minestom.server.timer.TaskSchedule
import net.orca.extension.addListener
import net.orca.extension.asComponent
import net.orca.extension.async
import net.orca.extension.launch
import net.orca.server.Yozora
import net.orca.server.game.survivalgames.state.GameState
import net.orca.server.game.survivalgames.task.GameLoopTask
import net.orca.server.game.survivalgames.task.SidebarTask
import net.orca.server.util.StateMachine
import java.nio.file.Path

object SurvivalGames {
    private val instanceLobby: Instance by lazy { createInstanceLobby() }
    private val sidebar: Sidebar by lazy { createSidebar() }
    private val taskGameLoop: Task
    private val taskSidebar: Task

    private val context = GameContext()
    private val stateMachine = StateMachine<GameContext, GameState>(context)

    private fun createInstanceLobby(): Instance {
        val manager = MinecraftServer.getInstanceManager()
        return manager.createInstanceContainer(AnvilLoader(Path.of("worlds/sg-lobby")))
    }

    //private fun createInstanceGame(): Instance {}

    private fun createSidebar(): Sidebar {
        return Sidebar("&aLiveGame &c30:00".asComponent()).apply {
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

    fun instanceLobby() = instanceLobby
    fun sidebar() = sidebar

    init {
        instanceLobby.eventNode().addListener<AddEntityToInstanceEvent>(Yozora.server()) { e ->

        }

        instanceLobby.eventNode().addListener<RemoveEntityFromInstanceEvent>(Yozora.server()) { e ->

        }

        val scheduler = MinecraftServer.getSchedulerManager()
        scheduler.buildShutdownTask { stop() }

        taskGameLoop = scheduler.scheduleTask(GameLoopTask(), TaskSchedule.immediate(), TaskSchedule.seconds(1L))
        taskSidebar = scheduler.scheduleTask(SidebarTask(), TaskSchedule.immediate(), TaskSchedule.seconds(1L))
    }

    fun stop() {
        taskSidebar.cancel()
        taskGameLoop.cancel()
    }
}