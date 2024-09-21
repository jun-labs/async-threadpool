package project.io.app.common.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy

@EnableAsync
@Configuration
class AsyncConfiguration {

    @Bean
    fun basicAsyncThreadPool(): ThreadPoolTaskExecutor {
        val taskExecutor = ThreadPoolTaskExecutor()
        taskExecutor.corePoolSize = 1
        taskExecutor.maxPoolSize = 2
        taskExecutor.queueCapacity = 1
        taskExecutor.keepAliveSeconds = 30
        taskExecutor.initialize()
        return taskExecutor
    }

    @Bean
    fun shortKeepAliveAsyncThreadPool(): ThreadPoolTaskExecutor {
        val taskExecutor = ThreadPoolTaskExecutor()
        taskExecutor.corePoolSize = 1
        taskExecutor.maxPoolSize = 2
        taskExecutor.queueCapacity = 1
        taskExecutor.keepAliveSeconds = 1
        taskExecutor.setRejectedExecutionHandler(CallerRunsPolicy())
        taskExecutor.initialize()
        return taskExecutor
    }

    @Bean
    fun callerRunsPolicyAsyncThreadPool(): ThreadPoolTaskExecutor {
        val taskExecutor = ThreadPoolTaskExecutor()
        taskExecutor.corePoolSize = 1
        taskExecutor.maxPoolSize = 2
        taskExecutor.queueCapacity = 1
        taskExecutor.keepAliveSeconds = 30
        taskExecutor.setRejectedExecutionHandler(CallerRunsPolicy())
        taskExecutor.initialize()
        return taskExecutor
    }

    @Bean
    fun abortPolicyAsyncThreadPool(): ThreadPoolTaskExecutor {
        val taskExecutor = ThreadPoolTaskExecutor()
        taskExecutor.corePoolSize = 1
        taskExecutor.maxPoolSize = 2
        taskExecutor.queueCapacity = 1
        taskExecutor.keepAliveSeconds = 30
        taskExecutor.setRejectedExecutionHandler(AbortPolicy())
        taskExecutor.initialize()
        return taskExecutor
    }
}
