/**
 * An element is transferred from sender to receiver only when [push] and [pop]
 * invocations meet in time (rendezvous), so [push] suspends until another coroutine
 * invokes [pop] and [pop] suspends until another coroutine invokes [push].
 */
interface BlockingStack<E> {
    /**
     * Sends the specified [element] to this channel, suspending if there is no waiting
     * [pop] invocation on this channel.
     */
    fun push(element: E)

    /**
     * Retrieves and removes an element from this channel if there is a waiting [push] invocation on it,
     * suspends the caller if this channel is empty.
     */
    suspend fun pop(): E
}