package id.ac.itera.tebengankekampus.data.remote

import io.ktor.client.HttpClient

/**
 * Pabrik [HttpClient] Ktor. Karena engine berbeda tiap platform
 * (OkHttp di Android/Desktop, Darwin di iOS), fungsi ini dibuat
 * dengan mekanisme expect/actual.
 */
expect fun createHttpClient(): HttpClient

/** Base URL backend. Bisa diarahkan ke REST API pada Sprint 3. */
const val BASE_URL: String = "https://example.com/api/"
