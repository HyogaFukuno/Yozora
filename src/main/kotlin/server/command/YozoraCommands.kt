package net.orca.server.commands

import net.orca.server.command.CommandGameModeAdventure
import net.orca.server.command.CommandGameModeCreative
import net.orca.server.command.CommandGameModeSpectator
import net.orca.server.command.CommandGameModeSurvival
import net.orca.server.command.CommandHub
import net.orca.server.command.CommandStop
import net.orca.server.command.CommandTps
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
        }
    }
}