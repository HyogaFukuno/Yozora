package net.orca.extension

import com.github.shynixn.mccoroutine.minestom.scope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import net.minestom.server.entity.Entity
import net.minestom.server.timer.ExecutionType
import net.orca.server.Yozora
import kotlin.coroutines.CoroutineContext

fun Entity.launch(block: suspend CoroutineScope.() -> Unit): Job {
    val dispatcher = SchedulerDispatcher(this)
    return Yozora.server().scope.launch(dispatcher, CoroutineStart.DEFAULT, block)
}

fun <T> Entity.async(block: suspend CoroutineScope.() -> T): Deferred<T> {
    val dispatcher = SchedulerDispatcher(this)
    return Yozora.server().scope.async(dispatcher, CoroutineStart.DEFAULT, block)
}

fun launch(block: suspend CoroutineScope.() -> Unit): Job {
    return Yozora.server().scope.launch(block = block)
}

fun <T> async(block: suspend CoroutineScope.() -> T): Deferred<T> {
    return Yozora.server().scope.async(block = block)
}


internal class SchedulerDispatcher(private val entity: Entity) : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        entity.scheduler().scheduleNextProcess(block, ExecutionType.TICK_START)
    }
}