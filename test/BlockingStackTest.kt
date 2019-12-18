import org.jetbrains.kotlinx.lincheck.LinChecker
import org.jetbrains.kotlinx.lincheck.LoggingLevel.DEBUG
import org.jetbrains.kotlinx.lincheck.annotations.LogLevel
import org.jetbrains.kotlinx.lincheck.annotations.Operation
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressCTest
import org.jetbrains.kotlinx.lincheck.verifier.VerifierState
import org.junit.Test
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@LogLevel(DEBUG)
@StressCTest(actorsBefore = 0, threads = 3, actorsPerThread = 3, invocationsPerIteration = 20_000,
             sequentialSpecification = SynchronousQueueSequential::class)
class BlockingStackTest : BlockingStack<Int> {
    val q = BlockingStackImpl<Int>()

    @Operation
    override fun push(element: Int) { q.push(element) }

    @Operation
    override suspend fun pop(): Int = q.pop()

    @Test
    fun runTest() = LinChecker.check(this::class.java)
}

class SynchronousQueueSequential : BlockingStack<Int>, VerifierState() {
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