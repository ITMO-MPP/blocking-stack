import java.util.concurrent.atomic.*

class BlockingStackImpl<E> : BlockingStack<E> {

    // ==========================
    // Segment Queue Synchronizer
    // ==========================

    private val enqIdx = AtomicLong()
    private val deqIdx = AtomicLong()

    private suspend fun suspend(): E {
        TODO("implement me")
    }

    private fun resume(element: E) {
        TODO("implement me")
    }

    // ==============
    // Blocking Stack
    // ==============


    private val head = AtomicReference<Node<E>?>()
    private val elements = AtomicInteger()

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

private val SUSPENDED = Any()