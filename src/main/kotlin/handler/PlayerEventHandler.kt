package net.orca.handler

import net.minestom.server.MinecraftServer
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.orca.extension.addListener

// TODO: GlobalなEventHandlerを作成するというより、
// 各インスタンスごとにEventHandlerを持たせたほうが良いかも？
// 例えば、ワールドごとに異なる挙動をさせたい場合など。
//class PlayerEventHandler {
//    init {
//        val handler = MinecraftServer.getGlobalEventHandler()
//        handler.addListener<AsyncPlayerConfigurationEvent>(this::onConfiguration)
//    }
//
//    fun onConfiguration(e: AsyncPlayerConfigurationEvent) {
//
//    }
//}

