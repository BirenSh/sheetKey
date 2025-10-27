package com.example.services

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

class GoogleAuthService {

    fun generateJWT(
        email: String,
        privateKey: String,
        scopes: List<String>
    ): String {
        // Clean the private key
        val cleanedKey = privateKey
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replace("-----BEGIN RSA PRIVATE KEY-----", "")
            .replace("-----END RSA PRIVATE KEY-----", "")
            .replace(Regex("\\s"), "")

        // Decode the key
        val keyBytes = Base64.getDecoder().decode(cleanedKey)
        val keySpec = PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        val privateKeyObj = keyFactory.generatePrivate(keySpec)

        // Create JWT
        val now = System.currentTimeMillis() / 1000
        return Jwts.builder()
            .setIssuer(email)
            .claim("scope", scopes.joinToString(" "))
            .setAudience("https://oauth2.googleapis.com/token")
            .setExpiration(Date((now + 3600) * 1000))
            .setIssuedAt(Date(now * 1000))
            .signWith(privateKeyObj, SignatureAlgorithm.RS256)
            .compact()
    }
}