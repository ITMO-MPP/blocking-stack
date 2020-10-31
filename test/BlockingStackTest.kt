import org.jetbrains.kotlinx.lincheck.*
import org.jetbrains.kotlinx.lincheck.LoggingLevel.*
import org.jetbrains.kotlinx.lincheck.annotations.Operation
import org.jetbrains.kotlinx.lincheck.strategy.stress.*
import org.jetbrains.kotlinx.lincheck.verifier.*
import org.junit.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.*

class BlockingStackTest : BlockingStack<Int> {
    private val q = BlockingStackImpl<Int>()

    @Operation(cancellableOnSuspension = false)
    override fun push(element: Int) { q.push(element) }

    @Operation(cancellableOnSuspension = false)
    override suspend fun pop(): Int = q.pop()

    @Test
    fun runTest() = StressOptions()
        .iterations(100)
        .invocationsPerIteration(50_000)
        .actorsBefore(0)
        .actorsAfter(0)
        .threads(3)
        .actorsPerThread(3)
        .sequentialSpecification(BlockingStackIntSequential::class.java)
        .logLevel(INFO)
        .check(this::class.java)
}

class BlockingStackIntSequential : BlockingStack<Int>, VerifierState() {
    private val elements = Stack<Int>()
    private val waitingReceivers = ArrayList<Continuation<Int>>()

    override fun push(element: Int) {
        if (waitingReceivers.isNotEmpty()) {
            val r = waitingReceivers.removeAt(0)
            r.resume(element)
        } else {
            elements.push(element)
        }
    }

    override suspend fun pop(): Int {
        if (elements.isNotEmpty()) {
            return elements.pop()
        } else {
            return suspendCoroutine { cont ->
                waitingReceivers.add(cont)
            }
        }
    }

    override fun extractState() = elements
}