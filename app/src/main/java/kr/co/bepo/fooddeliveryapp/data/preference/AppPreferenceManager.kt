package kr.co.bepo.fooddeliveryapp.data.preference

import android.content.Context
import android.content.SharedPreferences

class AppPreferenceManager(
    private val context: Context
) {
    companion object {
        const val PREFERENCE_NAME = "food-delivery-app"
        private const val DEFAULT_VALUE_STRING = ""
        private const val DEFAULT_VALUE_BOOLEAN = false
        private const val DEFAULT_VALUE_INT = -1
        private const val DEFAULT_VALUE_LONG = -1L
        private const val DEFAULT_VALUE_FLOAT = -1f

        const val KEY_ID_TOKEN = "ID_TOKEN"
    }

    private fun getPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    private val prefs by lazy { getPreferences(context) }
    private val editor by lazy { prefs.edit() }

    fun putIdToken(idToken: String) {
        editor.putString(KEY_ID_TOKEN, idToken)
        editor.apply()
    }

    fun getIdToken(): String? =
        prefs.getString(KEY_ID_TOKEN, null)


    fun removeIdToken() {
        editor.putString(KEY_ID_TOKEN, null)
        editor.apply()
    }

}