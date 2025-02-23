import java.util.Calendar

object TimeHelper {
    fun getTimeOfDay(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when {
            hour in 5..10 -> "Breakfast"
            hour in 11..16 -> "Lunch"
            hour in 17..21 -> "Dinner"
            else -> "Snack"
        }
    }
}