package net.orca.server.survivalgames

import kotlinx.coroutines.delay
import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.instance.Instance
import net.minestom.server.scoreboard.Sidebar
import net.orca.extension.addListener
import net.orca.extension.asComponent
import net.orca.extension.async
import net.orca.extension.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class SurvivalGames {
    companion object {
        private var INSTANCE: SurvivalGames? = null
        private val FORMATTER_DATE_ONLY: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH)
        private val FORMATTER_TIME_ONLY: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a z", Locale.ENGLISH)

        fun instance(): SurvivalGames {
            return INSTANCE!!
        }
    }

    private val instance: Instance
    private val sidebar: Sidebar

    init {
        INSTANCE = this
        instance = MinecraftServer.getInstanceManager().createInstanceContainer()
        sidebar = Sidebar("&aLiveGame &c30:00".asComponent)

        val now = ZonedDateTime.now()
        val country = Locale.getDefault().country
        val dateOnly = FORMATTER_DATE_ONLY.format(now)
        val timeOnly = FORMATTER_TIME_ONLY.format(now)

        sidebar.createLine(Sidebar.ScoreboardLine("sg_sidebar_10", "&6&l» Time".asComponent,          10))
        sidebar.createLine(Sidebar.ScoreboardLine("sg_sidebar_09", dateOnly.asComponent,            9))
        sidebar.createLine(Sidebar.ScoreboardLine("sg_sidebar_08", timeOnly.asComponent,            8))
        sidebar.createLine(Sidebar.ScoreboardLine("sg_sidebar_07", Component.empty(),               7))
        sidebar.createLine(Sidebar.ScoreboardLine("sg_sidebar_06", "&6&l» Server".asComponent,        6))
        sidebar.createLine(Sidebar.ScoreboardLine("sg_sidebar_05", "&3$country&8: &r1".asComponent, 5))
        sidebar.createLine(Sidebar.ScoreboardLine("sg_sidebar_04", Component.empty(),               4))
        sidebar.createLine(Sidebar.ScoreboardLine("sg_sidebar_03", "&6&l» Players".asComponent,       3))
        sidebar.createLine(Sidebar.ScoreboardLine("sg_sidebar_02", "Playing: 1".asComponent,        2))
        sidebar.createLine(Sidebar.ScoreboardLine("sg_sidebar_01", "Watching: 1".asComponent,       1))
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