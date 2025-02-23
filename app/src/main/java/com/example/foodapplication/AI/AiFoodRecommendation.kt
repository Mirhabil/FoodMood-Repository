import com.example.openai.AI.GeminiRequest
import com.example.openai.AI.Message
import com.example.openai.AI.RetrofitInstance
import com.example.openai.AI.TextPart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AiQuestionHandler {
    private const val API_KEY = "AIzaSyA8Oy2bKBE6d8wYRGVnCwj4h1mxzvGtLmY"

    suspend fun getAiAnswer(question: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val formattedQuestion = "Answer the following question in exactly 5 sentences:\n$question"

                val request = GeminiRequest(
                    contents = listOf(
                        Message(role = "user", parts = listOf(TextPart(text = formattedQuestion)))
                    )
                )

                val response = RetrofitInstance.api.getAiResponse(API_KEY, request)

                response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    ?: "I'm sorry, I couldn't understand the question."
            } catch (e: Exception) {
                "Error: ${e.message}"
            }
        }
    }
}