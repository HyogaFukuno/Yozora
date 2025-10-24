package net.orca.server.command

import revxrsal.commands.minestom.MinestomLamp

class YozoraCommands {
    companion object {
        @JvmStatic
        fun register() {
            val lamp = MinestomLamp.builder().build()
            lamp.register(CommandStop())
            lamp.register(CommandTps())

            lamp.register(CommandGameModeCreative())
            lamp.register(CommandGameModeAdventure())
            lamp.register(CommandGameModeSurvival())
            lamp.register(CommandGameModeSpectator())
            lamp.register(CommandHub())
            lamp.register(CommandSg())
        }
    }
}