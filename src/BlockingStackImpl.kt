import kotlinx.atomicfu.*

class BlockingStackImpl<E> : BlockingStack<E> {

    // ==========================
    // Segment Queue Synchronizer
    // ==========================

    private val enqIdx = atomic(0L)
    private val deqIdx = atomic(0L)

    private suspend fun suspend(): E {
        TODO("implement me")
    }

    private fun resume(element: E) {
        TODO("implement me")
    }

    // ==============
    // Blocking Stack
    // ==============


    private val head = atomic<Node<E>?>(null)
    private val elements = atomic(0)

    override fun push(element: E) {
        val elements = this.elements.getAndIncrement()
        if (elements >= 0) {
            // push the element to the top of the stack
            TODO("implement me")
        } else {
            // resume the next waiting receiver
            resume(element)
        }
    }

    override suspend fun pop(): E {
        val elements = this.elements.getAndDecrement()
        if (elements > 0) {
            // remove the top element from the stack
            TODO("implement me")
        } else {
            return suspend()
        }
    }
}

private class Node<E>(val element: Any?, val next: Node<E>?)

private val SUSPENDED = Any() //