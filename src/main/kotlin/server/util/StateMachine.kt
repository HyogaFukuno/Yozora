package net.orca.server.util

class StateMachine<TContext, TKey>(val context: TContext) {

    abstract class State<TContext, TKey>(protected val stateMachine: StateMachine<TContext, TKey>, protected val key: TKey) {
        protected abstract suspend fun enterAsync()
        protected abstract fun update()
        protected abstract suspend fun exitAsync()
    }

    fun requestKey(next: TKey) {

    }
}