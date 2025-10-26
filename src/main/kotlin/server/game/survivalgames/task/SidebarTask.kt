package net.orca.server.game.survivalgames.task

import net.orca.extension.asComponent
import net.orca.server.game.survivalgames.GameContext
import net.orca.server.game.survivalgames.SurvivalGames
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class SidebarTask(val context: GameContext) : Runnable {
    companion object {
        private val FORMATTER_DATE_ONLY: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH)
        private val FORMATTER_TIME_ONLY: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a z", Locale.ENGLISH)
    }

    override fun run() {

        val now = ZonedDateTime.now()
        val country = Locale.getDefault().country
        val dateOnly = FORMATTER_DATE_ONLY.format(now)
        val timeOnly = FORMATTER_TIME_ONLY.format(now)

        context.sidebar.updateLineContent("sg_sidebar_10", "&6&l» Time".asComponent())
        context.sidebar.updateLineContent("sg_sidebar_09", dateOnly.asComponent())
        context.sidebar.updateLineContent("sg_sidebar_08", timeOnly.asComponent())
        context.sidebar.updateLineContent("sg_sidebar_06", "&6&l» Server".asComponent())
        context.sidebar.updateLineContent("sg_sidebar_05", "&3$country&8: &r${context.gameId}".asComponent())
        context.sidebar.updateLineContent("sg_sidebar_03", "&6&l» Players".asComponent())
        context.sidebar.updateLineContent("sg_sidebar_02", "Playing: ${context.players.size}".asComponent())
        context.sidebar.updateLineContent("sg_sidebar_01", "Watching: ${context.spectators.size}".asComponent())
    }
}