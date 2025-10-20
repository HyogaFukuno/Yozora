package net.orca.extension

import net.minestom.server.event.Event
import net.minestom.server.event.EventNode

inline fun <reified T : Event> EventNode<Event>.addListener(noinline function: (T) -> Unit): EventNode<Event> =
    addListener(T::class.java) { event -> function(event) }