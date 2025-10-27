package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.routes.tokenRoutes

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("JWT Signing Service is running! ✅")
        }

        tokenRoutes()
    }
}