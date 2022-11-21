package name.valery1707.problem.rshb.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import name.valery1707.problem.rshb.service.DigitalCookService
import ru.rshb.svoe.rodnoe.api.ProductsApi

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/find-best-farmer") {
            //todo Configure baseUrl
            //todo Inject client
            val productsApi = ProductsApi("https://svoe-rodnoe.ru/fresh")
            call.respond(DigitalCookService.findBestFarmer(call.application.environment.log, productsApi))
        }
    }
}
