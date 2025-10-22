package net.orca.server.commands

import net.minestom.server.MinecraftServer
import revxrsal.commands.minestom.MinestomLamp

class YozoraCommands {
    companion object {
        @JvmStatic
        fun register() {
            val lamp = MinestomLamp.builder().build()
            lamp.register(CommandStop())
            lamp.register(CommandTps())
        }
    }
}