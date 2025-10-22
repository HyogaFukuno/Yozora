package net.orca.server.util

import net.minestom.server.MinecraftServer
import net.minestom.server.timer.Task
import net.minestom.server.timer.TaskSchedule

class TpsCalculator {

    companion object {

        private var instance: TpsCalculator? = null
        private const val INTERVAL_SECONDS = 60
        private const val INTERVAL_MILLISECONDS = INTERVAL_SECONDS * 1000L

        fun instance(): TpsCalculator {
            return instance!!
        }
    }

    private val timestamps = ArrayDeque<Long>()
    private var task: Task? = null

    init {
        instance = this
    }

    fun start() {
        task = MinecraftServer.getSchedulerManager().submitTask {

            val now = System.currentTimeMillis()
            timestamps.addLast(now)

            // インターバルを超えた古いタイムスタンプを削除する
            while (timestamps.isNotEmpty() && now - timestamps.first() > INTERVAL_MILLISECONDS) {
                timestamps.removeFirst()
            }

            TaskSchedule.nextTick()
        }
    }

    fun getTps(): Double {
        if (timestamps.size <= 1) {
            return 20.0
        }

        val elapsed = (timestamps.last() - timestamps.first()).coerceAtLeast(1)
        return (timestamps.size - 1) * 1000.0 / elapsed * 20.0
    }
}