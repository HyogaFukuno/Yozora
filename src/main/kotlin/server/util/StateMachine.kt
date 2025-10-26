package net.orca.server.util

import net.orca.extension.async
import net.orca.extension.launch

class StateMachine<TContext, TKey>(val context: TContext) {

    abstract class State<TContext, TKey>(protected val stateMachine: StateMachine<TContext, TKey>, internal val key: TKey) {
        internal abstract suspend fun enterAsync()
        internal abstract fun update()
        internal abstract suspend fun exitAsync()
    }

    private enum class TransitionPhase {
        NONE,
        EXITING,
        ENTERING
    }

    private val states = mutableMapOf<TKey, State<TContext, TKey>>()
    private var currentState: State<TContext, TKey>? = null
    private var nextKey: TKey? = null
    private var transitionPhase = TransitionPhase.NONE

    fun registerState(state: State<TContext, TKey>) {
        states[state.key] = state
    }

    fun requestKey(next: TKey) {
        nextKey = next
    }

    fun update() {
        if (nextKey != null && transitionPhase == TransitionPhase.NONE) {
            launch {
                async {
                    transitionPhase = TransitionPhase.EXITING
                    currentState?.exitAsync()
                    currentState = states[nextKey]
                    currentState?.enterAsync()
                }.await()

                nextKey = null
                transitionPhase = TransitionPhase.NONE
            }
        }

        if (transitionPhase != TransitionPhase.NONE) {
            return
        }

        currentState?.update()
    }
}