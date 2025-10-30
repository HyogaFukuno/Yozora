package net.orca.server

import io.github.togar2.pvp.MinestomPvP
import io.github.togar2.pvp.feature.CombatFeatures
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.extras.lan.OpenToLAN
import net.minestom.server.instance.block.Block
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.orca.server.extensions.addListener
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.system.exitProcess

object Yozora {

    private val running = AtomicBoolean(false)
    private val server = MinecraftServer.init()

    fun server(): MinecraftServer = server
    fun isRunning(): Boolean = running.get()

    init {
        MinecraftServer.setBrandName("Yozora")
    }

    fun start() {
        try {
            running.set(true)

            MinestomPvP.init()
            val combatFeatures = CombatFeatures.legacyVanilla()

            val instance = MinecraftServer.getInstanceManager().createInstanceContainer()
            instance.setGenerator { unit ->
                val start = unit.absoluteStart()
                val size = unit.size()
                for (x in 0..<size.blockX()) {
                    for (z in 0..<size.blockZ()) {
                        for (y in 0..<(40 - start.blockY()).coerceAtMost(size.blockY())) {
                            unit.modifier().setBlock(start.add(x.toDouble(), y.toDouble(), z.toDouble()), Block.STONE)
                        }
                    }
                }
            }

            val handler = MinecraftServer.getGlobalEventHandler()
            handler.addChild(combatFeatures.createNode())
            handler.addListener<AsyncPlayerConfigurationEvent> {
                val player = it.player

                it.spawningInstance = instance
                player.respawnPoint = Pos(0.0, 42.0, 0.0)
                player.inventory.addItemStack(ItemStack.of(Material.STONE_SWORD))
                player.inventory.addItemStack(ItemStack.of(Material.FISHING_ROD))
                player.inventory.addItemStack(ItemStack.of(Material.IRON_HELMET))
                player.inventory.addItemStack(ItemStack.of(Material.IRON_CHESTPLATE))
                player.inventory.addItemStack(ItemStack.of(Material.IRON_LEGGINGS))
                player.inventory.addItemStack(ItemStack.of(Material.IRON_BOOTS))
                player.setGameMode(GameMode.SURVIVAL)
            }

            MinecraftServer.LOGGER.info("Opening to LAN...")

            OpenToLAN.open()

            server.start("0.0.0.0", 25565)
            System.gc()
        }
        catch (error: Throwable) {
            stopWithError(error)
        }
    }

    fun stop() {
        if (running.compareAndSet(true, false)) {
            MinecraftServer.stopCleanly()
            exitProcess(0)
        }
    }

    private fun stopWithError(error: Throwable) {
        if (running.compareAndSet(false, true)) {
            error.printStackTrace()
            MinecraftServer.stopCleanly()
            exitProcess(0)
        }
    }
}