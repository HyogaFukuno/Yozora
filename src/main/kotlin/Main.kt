package net.orca

import kotlinx.coroutines.delay
import net.orca.server.Yozora
import kotlin.system.exitProcess
import kotlin.time.Duration.Companion.seconds

suspend fun main() {
    Yozora.run()
    while (Yozora.isRunning()) {
        delay(1.seconds)
    }
    exitProcess(0)
}