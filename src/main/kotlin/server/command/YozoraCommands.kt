package net.orca.server.command

import net.minestom.server.command.CommandSender
import net.orca.server.Yozora
import revxrsal.commands.annotation.Command
import revxrsal.commands.minestom.MinestomLamp

object YozoraCommands {
    @JvmStatic
    fun register() {
        val lamp = MinestomLamp.builder().build()
        lamp.register(CommandStop())
    }
}

private class CommandStop {
    @Command(value = ["stop", "shutdown"])
    fun onCommand(sender: CommandSender) {
        Yozora.stop()
    }
}