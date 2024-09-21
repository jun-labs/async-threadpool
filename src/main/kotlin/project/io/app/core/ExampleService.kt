package project.io.app.core

import org.springframework.stereotype.Service

@Service
class ExampleService(
    private val asyncComponent: AsyncComponent,
) {

    fun execute() {
        asyncComponent.execute()
    }
}
