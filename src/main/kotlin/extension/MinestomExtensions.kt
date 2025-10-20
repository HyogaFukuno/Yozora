package net.orca.extension

import com.github.shynixn.mccoroutine.minestom.addSuspendingListener
import net.minestom.server.MinecraftServer
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode

inline fun <reified T : Event> EventNode<in T>.addListener(noinline function: (T) -> Unit): EventNode<in T> =
    addListener(T::class.java) { event -> function(event) }

inline fun <reified T : Event> EventNode<in T>.addListener(server: MinecraftServer, noinline function: suspend (T) -> Unit) {
    addSuspendingListener(server, T::class.java) { event -> function(event) }
}

