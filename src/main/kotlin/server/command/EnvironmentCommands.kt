package net.orca.server.commands

import net.kyori.adventure.text.Component
import net.orca.server.Yozora
import net.orca.server.util.TpsCalculator
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.CommandPlaceholder
import revxrsal.commands.minestom.actor.MinestomCommandActor

@Command("stop")
class CommandStop {
    @CommandPlaceholder
    fun onCommand() {
        Yozora.instance().stop()
    }
}

@Command("tps")
class CommandTps {
    @CommandPlaceholder
    fun onCommand(actor: MinestomCommandActor) {
        actor.reply { Component.text("TPS: ${TpsCalculator.instance().getTps()}") }
    }
}