import com.collebra.digital.newsapptask.di.CoroutinesDispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class MainCoroutineRule(
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}

@ExperimentalCoroutinesApi
fun MainCoroutineRule.runBlockingTest(block: suspend () -> Unit) =
    this.testDispatcher.runBlockingTest {
        block()
    }

@ExperimentalCoroutinesApi
fun provideFakeCoroutinesDispatcherProvider(
    dispatcher: TestCoroutineDispatcher?
): CoroutinesDispatcherProvider {
    val sharedTestCoroutineDispatcher = TestCoroutineDispatcher()
    return CoroutinesDispatcherProvider(
        dispatcher ?: sharedTestCoroutineDispatcher,
        dispatcher ?: sharedTestCoroutineDispatcher,
        dispatcher ?: sharedTestCoroutineDispatcher
    )
}