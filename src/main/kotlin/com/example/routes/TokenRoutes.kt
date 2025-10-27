package com.example.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.services.GoogleAuthService
import kotlinx.serialization.Serializable

@Serializable
data class SignRequest(
    val email: String,
    val privateKey: String,
    val scopes: List<String>
)

@Serializable
data class SignResponse(val token: String)

@Serializable
data class ErrorResponse(val error: String)

fun Route.tokenRoutes() {
    val authService = GoogleAuthService()

    post("/api/sign") {
        try {
            val request = call.receive<SignRequest>()

            val token = authService.generateJWT(
                email = request.email,
                privateKey = request.privateKey,
                scopes = request.scopes
            )


            call.respond(HttpStatusCode.OK, SignResponse(token))

        } catch (e: Exception) {
            call.application.environment.log.error("Token generation failed", e)
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse("Failed to generate token: ${e.message}")
            )
        }
    }
}