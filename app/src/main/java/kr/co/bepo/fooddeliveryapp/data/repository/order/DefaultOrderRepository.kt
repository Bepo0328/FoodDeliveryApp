package kr.co.bepo.fooddeliveryapp.data.repository.order

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantFoodEntity

class DefaultOrderRepository(
    private val firestore: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher
) : OrderRepository {

    override suspend fun orderMenu(
        userId: String,
        restaurantId: Long,
        foodMenuList: List<RestaurantFoodEntity>
    ): Result = withContext(ioDispatcher) {
        val result: Result
        val orderMenuData = hashMapOf(
            "restaurantId" to restaurantId,
            "userId" to userId,
            "orderMenuList" to foodMenuList
        )
        result = try {
            firestore.collection("order")
                .add(orderMenuData)
            Result.Success<Any>()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }
        return@withContext result
    }

    sealed class Result {

        data class Success<T>(
            val data: T? = null
        ) : Result()

        data class Error(
            val e: Throwable
        ) : Result()
    }
}