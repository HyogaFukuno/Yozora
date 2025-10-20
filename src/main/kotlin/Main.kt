package net.orca

import de.articdive.jnoise.generators.noisegen.opensimplex.FastSimplexNoiseGenerator
import de.articdive.jnoise.pipeline.JNoise
import net.minestom.server.Auth
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.instance.block.Block
import net.orca.extension.addListener

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val server = MinecraftServer.init(Auth.Online())
    val instanceManager = MinecraftServer.getInstanceManager()
    val instance = instanceManager.createInstanceContainer()

    val noise = JNoise.newBuilder()
        .fastSimplex(FastSimplexNoiseGenerator.newBuilder().build())
        .scale(0.005)
        .build()

    instance.setGenerator { unit ->
        val start = unit.absoluteStart()
        for (x in 0 until unit.size().blockX()) {
            for (z in 0 until unit.size().blockZ()) {
                val bottom = start.add(x.toDouble(), 0.0, z.toDouble())

                synchronized(noise) {
                    val height = noise.evaluateNoise(bottom.x(), bottom.z()) * 16
                    unit.modifier().fill(bottom, bottom.add(1.0, 0.0, 1.0).withY(height), Block.STONE)
                }
            }
        }
    }

    MinecraftServer.getGlobalEventHandler().addListener<AsyncPlayerConfigurationEvent> { e ->
        val player = e.player
        e.spawningInstance = instance
        player.respawnPoint = Pos(0.0, 42.0, 0.0)
    }
    server.start("0.0.0.0", 25565)
}