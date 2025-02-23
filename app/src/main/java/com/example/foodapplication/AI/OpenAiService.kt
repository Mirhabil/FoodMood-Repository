package com.example.openai.AI

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

data class GeminiRequest(val contents: List<Message>)
data class Message(val role: String, val parts: List<TextPart>)
data class TextPart(val text: String)
data class GeminiResponse(val candidates: List<Candidate>?)
data class Candidate(val content: Content?)
data class Content(val parts: List<TextPart>?)

// Define Retrofit API interface
interface GeminiService {
    @POST("v1/models/gemini-pro:generateContent")
    suspend fun getAiResponse(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

// Retrofit object
object RetrofitInstance {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    val api: GeminiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiService::class.java)
    }
}