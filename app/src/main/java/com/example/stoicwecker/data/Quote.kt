package com.example.stoicwecker.data

data class Quote(
    val text: String,
    val author: String
)

// Interface für die API-Kommunikation
interface QuoteApi {
    suspend fun getRandomQuote(): Quote
}
