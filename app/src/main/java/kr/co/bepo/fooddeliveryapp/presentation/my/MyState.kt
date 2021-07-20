package kr.co.bepo.fooddeliveryapp.presentation.my

import android.net.Uri
import androidx.annotation.StringRes

sealed class MyState {

    object UnInitialized : MyState()

    object Loading : MyState()

    data class Login(
        val idToken: String
    ) : MyState()

    sealed class Success : MyState() {

        data class Registered(
            val userName: String,
            val profileImageUri: Uri?
        ) : Success()

        object NotRegistered : Success()
    }

    data class Error(
        @StringRes val messageId: Int,
        val e: Throwable
    ) : MyState()
}