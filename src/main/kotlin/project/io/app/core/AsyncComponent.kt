package project.io.app.core

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import project.io.app.logger

@Service
class AsyncComponent {

    private val log = logger()

    @Async
    fun execute() {
        log.info("[Async Service] --------------------x>")
        log.info("[Async Service] <x--------------------")
    }
}
