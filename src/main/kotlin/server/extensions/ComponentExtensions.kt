package net.orca.server.extensions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

fun String.asComponent(): Component = LegacyComponentSerializer.legacyAmpersand().deserialize(this)