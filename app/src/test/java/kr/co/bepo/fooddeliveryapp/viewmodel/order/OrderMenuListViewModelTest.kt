package kr.co.bepo.fooddeliveryapp.viewmodel.order

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kr.co.bepo.fooddeliveryapp.data.entity.OrderEntity
import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantFoodEntity
import kr.co.bepo.fooddeliveryapp.data.repository.order.DefaultOrderRepository
import kr.co.bepo.fooddeliveryapp.data.repository.order.OrderRepository
import kr.co.bepo.fooddeliveryapp.data.repository.restaurant.food.RestaurantFoodRepository
import kr.co.bepo.fooddeliveryapp.domain.model.CellType
import kr.co.bepo.fooddeliveryapp.domain.model.food.FoodModel
import kr.co.bepo.fooddeliveryapp.presentation.order.OrderMenuListState
import kr.co.bepo.fooddeliveryapp.presentation.order.OrderMenuListViewModel
import kr.co.bepo.fooddeliveryapp.viewmodel.ViewModelTest
import org.junit.Test
import org.koin.core.parameter.parametersOf
import org.koin.test.inject
import org.mockito.Mock
import org.mockito.Mockito

@ExperimentalCoroutinesApi
internal class OrderMenuListViewModelTest : ViewModelTest() {

    @Mock
    lateinit var firebaseAuth: FirebaseAuth

    @Mock
    lateinit var firebaseUser: FirebaseUser

    private val orderMenuListViewModel by inject<OrderMenuListViewModel> {
        parametersOf(firebaseAuth)
    }

    private val restaurantFoodRepository by inject<RestaurantFoodRepository>()

    private val orderRepository by inject<OrderRepository>()

    private val restaurantId = 0L

    private val restaurantTitle = "식당명"

    /**
     * 1. 장바구니에 메뉴를 담는다.
     * 2. 장바구니에 담은 메뉴를 리스트로 뿌려준다.
     * 3. 장바구니 목록에 있는 데이터를 갖고 주문을한다.
     */

    @Test
    fun `insert food menus in basket`() = runBlockingTest {
        (0 until 10).forEach {
            restaurantFoodRepository.insertFoodMenuInBasket(
                RestaurantFoodEntity(
                    id = it.toString(),
                    title = "메뉴 $it",
                    description = "소개 $it",
                    price = it,
                    imageUrl = "",
                    restaurantId = restaurantId,
                    restaurantTitle = restaurantTitle
                )
            )
        }
        assert(restaurantFoodRepository.getAllFoodMenuListInBasket().size == 10)
    }

    @Test
    fun `test load order menu list`() = runBlockingTest {
        `insert food menus in basket`()
        val testObservable = orderMenuListViewModel.orderMenuStateLiveData.test()
        orderMenuListViewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                OrderMenuListState.UnInitialized,
                OrderMenuListState.Loading,
                OrderMenuListState.Success(
                    restaurantFoodModelList = restaurantFoodRepository.getAllFoodMenuListInBasket()
                        .map {
                            FoodModel(
                                id = it.hashCode().toLong(),
                                type = CellType.ORDER_FOOD_CELL,
                                title = it.title,
                                description = it.description,
                                price = it.price,
                                restaurantId = it.restaurantId,
                                imageUrl = it.imageUrl,
                                foodId = it.id,
                                restaurantTitle = it.restaurantTitle
                            )
                        }
                )
            )
        )
    }

    @Test
    fun `test do order menu list`() = runBlockingTest {
        `insert food menus in basket`()

        val userId = "abcd"
        Mockito.`when`(firebaseAuth.currentUser).then { firebaseUser }
        Mockito.`when`(firebaseUser.uid).then { userId }

        val testObservable = orderMenuListViewModel.orderMenuStateLiveData.test()
        orderMenuListViewModel.fetchData()

        val menuListInBasket =
            restaurantFoodRepository.getAllFoodMenuListInBasket().map { it.copy() }
        val menuListInBasketModelList = menuListInBasket.map {
            FoodModel(
                id = it.hashCode().toLong(),
                type = CellType.ORDER_FOOD_CELL,
                title = it.title,
                description = it.description,
                price = it.price,
                restaurantId = it.restaurantId,
                imageUrl = it.imageUrl,
                foodId = it.id,
                restaurantTitle = it.restaurantTitle
            )
        }

        orderMenuListViewModel.orderMenu()

        testObservable.assertValueSequence(
            listOf(
                OrderMenuListState.UnInitialized,
                OrderMenuListState.Loading,
                OrderMenuListState.Success(
                    restaurantFoodModelList = menuListInBasketModelList
                ),
                OrderMenuListState.Order
            )
        )

        assert(orderRepository.getAllOrderMenus(userId) is DefaultOrderRepository.Result.Success<*>)
        val result = (orderRepository.getAllOrderMenus(userId) as DefaultOrderRepository.Result.Success<*>).data

        assert(
            result?.equals(
                listOf(
                    OrderEntity(
                        id = "0",
                        userId = userId,
                        restaurantId = restaurantId,
                        foodMenuList = menuListInBasket,
                        restaurantTitle = restaurantTitle
                    )
                )
            ) ?: false
        )
    }

}