package net.orca.server.command

import net.kyori.adventure.text.Component
import net.minestom.server.entity.GameMode
import net.orca.server.hub.Hub
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.CommandPlaceholder
import revxrsal.commands.minestom.actor.MinestomCommandActor

@Command(value = ["gamemode creative", "gmc"])
class CommandGameModeCreative {
    @CommandPlaceholder
    fun onCommand(actor: MinestomCommandActor) {
        if (actor.isPlayer) {
            actor.asPlayer()?.gameMode = GameMode.CREATIVE
            actor.reply { Component.text("Set own game mode to Creative Mode") }
        }
    }
}

@Command(value = ["gamemode adventure", "gma"])
class CommandGameModeAdventure {
    @CommandPlaceholder
    fun onCommand(actor: MinestomCommandActor) {
        if (actor.isPlayer) {
            actor.asPlayer()?.gameMode = GameMode.ADVENTURE
            actor.reply { Component.text("Set own game mode to Adventure Mode") }
        }
    }
}

@Command(value = ["gamemode survival", "gms"])
class CommandGameModeSurvival {
    @CommandPlaceholder
    fun onCommand(actor: MinestomCommandActor) {
        if (actor.isPlayer) {
            actor.asPlayer()?.gameMode = GameMode.SURVIVAL
            actor.reply { Component.text("Set own game mode to Survival Mode") }
        }
    }
}

@Command(value = ["gamemode spectator", "gmp"])
class CommandGameModeSpectator {
    @CommandPlaceholder
    fun onCommand(actor: MinestomCommandActor) {
        if (actor.isPlayer) {
            actor.asPlayer()?.gameMode = GameMode.SPECTATOR
            actor.reply { Component.text("Set own game mode to Spectator Mode") }
        }
    }
}

@Command("hub")
class CommandHub {
    @CommandPlaceholder
    fun onCommand(actor: MinestomCommandActor) {
        if (actor.isPlayer) {
            actor.asPlayer()?.instance = Hub.instance()
        }
    }
}