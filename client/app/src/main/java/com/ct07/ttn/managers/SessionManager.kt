import android.content.Context
import android.content.SharedPreferences
import java.util.concurrent.TimeUnit

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    fun saveAuthenticationInfo(username: String) {
        val editor = prefs.edit()
        editor.putString("username", username)
        editor.putLong("lastLoginTime", System.currentTimeMillis())
        editor.apply()
    }

    fun shouldRequireLogin(): Boolean {
        val lastLoginTime = prefs.getLong("lastLoginTime", 0)
        val oneWeekInMillis = TimeUnit.DAYS.toMillis(7)
        return System.currentTimeMillis() - lastLoginTime > oneWeekInMillis
    }

    fun getUsername(): String? {
        return prefs.getString("username", null)
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}