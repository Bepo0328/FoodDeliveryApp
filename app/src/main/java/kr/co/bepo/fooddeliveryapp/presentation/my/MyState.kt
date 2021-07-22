package kr.co.bepo.fooddeliveryapp.presentation.my

import android.net.Uri
import androidx.annotation.StringRes
import kr.co.bepo.fooddeliveryapp.domain.model.order.OrderModel

sealed class MyState {

    object UnInitialized : MyState()

    object Loading : MyState()

    data class Login(
        val idToken: String
    ) : MyState()

    sealed class Success : MyState() {

        data class Registered(
            val userName: String,
            val profileImageUri: Uri?,
            val orderList: List<OrderModel>
        ) : Success()

        object NotRegistered : Success()
    }

    data class Error(
        @StringRes val messageId: Int,
        val e: Throwable
    ) : MyState()
}