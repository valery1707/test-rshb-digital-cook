package name.valery1707.problem.rshb.plugins

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import name.valery1707.problem.rshb.service.DigitalCookService

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/find-best-farmer") {
            call.respond(DigitalCookService.findBestFarmer())
        }
    }
}
