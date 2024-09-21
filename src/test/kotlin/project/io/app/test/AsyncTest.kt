package project.io.app.test

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import project.io.app.core.ExampleService
import project.io.app.logger
import java.util.concurrent.CountDownLatch
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.TimeUnit.SECONDS

@SpringBootTest
class ThreadPoolTest {

    private val log = logger()

    @Autowired
    lateinit var basicAsyncThreadPool: ThreadPoolTaskExecutor

    @Autowired
    lateinit var callerRunsPolicyAsyncThreadPool: ThreadPoolTaskExecutor

    @Autowired
    lateinit var abortPolicyAsyncThreadPool: ThreadPoolTaskExecutor

    @Autowired
    lateinit var shortKeepAliveAsyncThreadPool: ThreadPoolTaskExecutor

    @Autowired
    lateinit var async: ExampleService

    @Test
    @Disabled
    fun asyncDebuggingTest() {
        async.execute()
    }

    @Test
    fun whenSetKeepAliveThenMinThreadShouldBeRemained() {
        val latch = CountDownLatch(5)
        for (thread in 1..5) {
            try {
                shortKeepAliveAsyncThreadPool.submit {
                    log.info("[${Thread.currentThread().name}] start")
                    SECONDS.sleep(1)
                    log.info("[${Thread.currentThread().name}] complete")
                }
            } catch (ex: Exception) {
                log.error("${ex.message}")
            } finally {
                latch.countDown()
            }
        }

        latch.await()

        log.info("Current pool size after waiting: ${shortKeepAliveAsyncThreadPool.poolSize}")
        log.info("Active thread count after waiting: ${shortKeepAliveAsyncThreadPool.activeCount}")

        val startTime = System.currentTimeMillis()
        val duration = 5_000L
        while (System.currentTimeMillis() - startTime < duration) {
            val elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000
            log.info("Elapsed Time: ${elapsedSeconds}s")
            SECONDS.sleep(1)
        }

        assertEquals(1, shortKeepAliveAsyncThreadPool.poolSize)
        assertEquals(0, shortKeepAliveAsyncThreadPool.activeCount)
        log.info("Current pool size after waiting: ${shortKeepAliveAsyncThreadPool.poolSize}")
        log.info("Active thread count after waiting: ${shortKeepAliveAsyncThreadPool.activeCount}")
    }

    @Test
    fun whenNotSetRejectPolicyThenRejectedExecutionExceptionShouldBeHappen() {
        val latch = CountDownLatch(5)
        var rejectedCount = 0
        for (thread in 1..5) {
            try {
                basicAsyncThreadPool.submit {
                    log.info("[${Thread.currentThread().name}] start")
                    SECONDS.sleep(1)
                    log.info("[${Thread.currentThread().name}] complete")
                }
            } catch (ex: RejectedExecutionException) {
                log.error("[${Thread.currentThread().name}] error: {${ex.message}}")
                rejectedCount++
            } finally {
                latch.countDown()
            }
        }

        latch.await()
        assertTrue(rejectedCount > 0)
    }

    @Test
    fun whenSetRejectPolicyThenRejectedExecutionExceptionShouldNotBeHappen() {
        val latch = CountDownLatch(5)
        var rejectedCount = 0
        for (thread in 1..5) {
            try {
                callerRunsPolicyAsyncThreadPool.submit {
                    log.info("[${Thread.currentThread().name}] start")
                    SECONDS.sleep(1)
                    log.info("[${Thread.currentThread().name}] complete")
                }
            } catch (ex: RejectedExecutionException) {
                log.error("[${Thread.currentThread().name}] error: {${ex.message}}")
                rejectedCount++
            } finally {
                latch.countDown()
            }
        }
        latch.await()
        assertEquals(0, rejectedCount)
    }

    @Test
    fun whenSetAbortPolicyThenRejectCountShouldIncrease() {
        val latch = CountDownLatch(5)
        var rejectedCount = 0
        for (thread in 1..5) {
            try {
                abortPolicyAsyncThreadPool.submit {
                    log.info("[${Thread.currentThread().name}] start")
                    SECONDS.sleep(1)
                    log.info("[${Thread.currentThread().name}] complete")
                }
            } catch (ex: RejectedExecutionException) {
                log.error("[${Thread.currentThread().name}] error: {${ex.message}}")
                rejectedCount++
            } finally {
                latch.countDown()
            }
        }

        latch.await()
        assertTrue(rejectedCount > 0)
    }
}

