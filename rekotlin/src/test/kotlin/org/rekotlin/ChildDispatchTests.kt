package org.rekotlin

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class ChildDispatchTests {

    //
    // Root
    //

    private data class RootState(
        val string: String = "string"
    )

    private fun rootReducer(action: Action, oldState: RootState?): RootState {
        val state = oldState ?: RootState()
        return when (action) {
            is SetRootString -> state.copy(string = action.value)
            else -> state
        }
    }

    //
    // Child1
    //

    private data class ChildState1(
        val integer: Int = 1
    )

    private fun child1Reducer(action: Action, oldState: ChildState1?): ChildState1 {
        val state = oldState ?: ChildState1()
        return when (action) {
            is SetChild1Integer -> state.copy(integer = action.value)
            else -> state
        }
    }

    //
    // Child2
    //

    private data class ChildState2(
        val boolean: Boolean = false
    )

    private fun child2Reducer(action: Action, oldState: ChildState2?): ChildState2 {
        val state = oldState ?: ChildState2()
        return when (action) {
            is SetChild2Boolean -> state.copy(boolean = action.value)
            else -> state
        }
    }

    // Actions
    private data class SetRootString(val value: String) : Action
    private data class SetChild1Integer(val value: Int) : Action
    private data class SetChild2Boolean(val value: Boolean) : Action

    // Stores
    private val rootStore: RootStore<RootState> = rootStore(::rootReducer, null)
    private val childStore1 = rootStore + ::child1Reducer
    private val childStore2 = rootStore + ::child2Reducer

    @Test
    @Disabled
    fun `test subscribers dispatch to childStore2`() {
        println("\n=== Subscribe ===\n")

        rootStore.subscribeTo { println("rootStore.state=$it") }
        childStore1.subscribeTo { println("childStore1.state=$it") }
        childStore2.subscribeTo { println("childStore2.state=$it") }

        println("\n=== Dispatch Actions ===\n")

        println("> SetRootString(value = \"root\")")
        childStore2.dispatch(SetRootString(value = "root"))
        assert(rootStore.state.string == "root")
        println()

        println("> SetInteger(value = 111)")
        childStore2.dispatch(SetChild1Integer(value = 111))
        assert(childStore1.state.second.integer == 111)
        println()

        println("> SetInteger(value = 111)")
        childStore2.dispatch(SetChild2Boolean(value = true))
        assert(childStore2.state.second.boolean)
        println()

        println("\n=== States ===\n")
        println("> RootStore: ${rootStore.state}")
        println("> ChildStore1: ${childStore1.state}")
        println("> ChildStore2: ${childStore2.state}")
        println()
        assert(rootStore.state.string == childStore1.state.first.string) { "rootStore.state.string '${rootStore.state.string}' != childStore1.state.first.string '${childStore1.state.first.string}'" }
    }

    @Test
    @Disabled
    fun `test subscribers dispatch to root`() {
        println("\n=== Subscribe ===\n")

        rootStore.subscribeTo { println("rootStore.state=$it") }
        childStore1.subscribeTo { println("childStore1.state=$it") }
        childStore2.subscribeTo { println("childStore2.state=$it") }

        println("\n=== Dispatch Actions ===\n")

        println("> SetRootString(value = \"root\")")
        rootStore.dispatch(SetRootString(value = "root"))
        assert(rootStore.state.string == "root")
        println()

        println("> SetInteger(value = 111)")
        rootStore.dispatch(SetChild1Integer(value = 111))
        assert(childStore1.state.second.integer == 111)
        println()

        println("> SetInteger(value = 111)")
        rootStore.dispatch(SetChild2Boolean(value = true))
        assert(childStore2.state.second.boolean)
        println()

        println("\n=== States ===\n")
        println("> RootStore: ${rootStore.state}")
        println("> ChildStore1: ${childStore1.state}")
        println("> ChildStore2: ${childStore2.state}")
        println()

        assert(rootStore.state.string == childStore1.state.first.string) { "rootStore.state.string '${rootStore.state.string}' != childStore1.state.first.string '${childStore1.state.first.string}'" }
    }
}
