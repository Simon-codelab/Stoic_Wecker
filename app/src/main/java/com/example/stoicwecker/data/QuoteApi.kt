package com.example.stoicwecker.data

interface QuoteApi {
    suspend fun getRandomQuote(): Quote
}

class QuoteApiImpl : QuoteApi {
    private val quotes = listOf(
        Quote("The happiness of your life depends upon the quality of your thoughts.", "Marcus Aurelius"),
        Quote("Waste no more time arguing what a good man should be. Be one.", "Marcus Aurelius"),
        Quote("You have power over your mind - not outside events. Realize this, and you will find strength.", "Marcus Aurelius"),
        Quote("The best revenge is not to be like your enemy.", "Marcus Aurelius"),
        Quote("Very little is needed to make a happy life; it is all within yourself, in your way of thinking.", "Marcus Aurelius")
    )

    override suspend fun getRandomQuote(): Quote {
        return quotes.random()
    }
}
