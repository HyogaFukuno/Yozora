package net.orca.extension

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

val String.asComponent: Component get() = LegacyComponentSerializer.legacyAmpersand().deserialize(this)