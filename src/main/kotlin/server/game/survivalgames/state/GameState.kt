package net.orca.server.game.survivalgames.state

enum class GameState(val displayName: String) {
    LOBBY("Lobby"),
    PRE_GAME("PreGame"),
    LIVE_GAME("LiveGame"),
    PRE_DEATHMATCH("PreDeathmatch"),
    DEATHMATCH("Deathmatch"),
    END_GAME("EndGame"),
    CLEAN_UP("Cleanup")
}