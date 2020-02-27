package org.rekotlin

/**
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

//TODO read up on crossinline
inline fun <S> subscriber(crossinline block: (S) -> Unit) = object : Subscriber<S> {
    override fun newState(state: S) = block(state)
}

inline fun <E : Effect> listener(crossinline block: (E) -> Unit) = object : Listener<E> {
    override fun onEffect(effect: E) = block(effect)
}

fun <State> store(
        reducer: Reducer<State>,
        state: State? = null,
        vararg middleware: Middleware<State> = arrayOf()
) : Store<State> =
        ParentStore(reducer, state, middleware.toList(), true)

fun <State> rootStore(
        reducer: Reducer<State>,
        state: State? = null,
        vararg middleware: Middleware<State> = arrayOf()
) : RootStore<State> =
        ParentStore(reducer, state, middleware.toList(), true)

/**
 * Initial Action that is dispatched as soon as the store is created.
 * Reducers respond to this action by configuring their initial state.
 */
// TODO: do we need this?
object ReKotlinInit : Action

internal fun <T> Subscription<T>.skipRepeatsTransform(): Subscription<T> = this.skipRepeats()
internal fun <T> stateIdentity(sub: Subscription<T>) = sub
internal fun <T: Effect> effectIdentity(effect: T) = effect
internal fun <T, S> compose(first: T.() -> S, second: S.() -> S): T.() -> S = { first().second() }