package kr.co.bepo.fooddeliveryapp.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantEntity
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.RestaurantCategory
import kr.co.bepo.fooddeliveryapp.utility.provider.ResourcesProvider

class DefaultRestaurantRepository(
    private val resourcesProvider: ResourcesProvider,
    private val ioDispatcher: CoroutineDispatcher
) : RestaurantRepository {

    override suspend fun getList(
        restaurantCategory: RestaurantCategory
    ): List<RestaurantEntity> = withContext(ioDispatcher) {

        // TODO API를 통한 데이터 받아오기
        listOf(
            RestaurantEntity(
                id = 0,
                restaurantInfoId = 0,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantTitle = "소인수서울",
                restaurantImageUrl = "https://i.imgur.com/Ll0XMRk.jpg",
                grade = (1 until 5).random() + ((0..10).random() / 10f),
                reviewCount = (0 until 200).random(),
                deliveryTimeRange = Pair(0, 20),
                minPrice = 7900,
                deliveryTipRange = Pair(0, 2000)
            ),
            RestaurantEntity(
                id = 1,
                restaurantInfoId = 1,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantTitle = "건담돼지",
                restaurantImageUrl = "https://i.imgur.com/FNj34ga.jpg",
                grade = (1 until 5).random() + ((0..10).random() / 10f),
                reviewCount = (0 until 200).random(),
                deliveryTimeRange = Pair(0, 20),
                minPrice = 7900,
                deliveryTipRange = Pair(0, 2000)
            ),
            RestaurantEntity(
                id = 2,
                restaurantInfoId = 2,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantTitle = "계인전",
                restaurantImageUrl = "https://i.imgur.com/85mNY5f.jpg",
                grade = (1 until 5).random() + ((0..10).random() / 10f),
                reviewCount = (0 until 200).random(),
                deliveryTimeRange = Pair(0, 20),
                minPrice = 7900,
                deliveryTipRange = Pair(0, 2000)
            ),
            RestaurantEntity(
                id = 3,
                restaurantInfoId = 3,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantTitle = "꼼모아",
                restaurantImageUrl = "https://i.imgur.com/UgDzjtP.jpg",
                grade = (1 until 5).random() + ((0..10).random() / 10f),
                reviewCount = (0 until 200).random(),
                deliveryTimeRange = Pair(0, 20),
                minPrice = 7900,
                deliveryTipRange = Pair(0, 2000)
            ),
            RestaurantEntity(
                id = 4,
                restaurantInfoId = 4,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantTitle = "권숙수",
                restaurantImageUrl = "https://i.imgur.com/V8GnMiA.jpg",
                grade = (1 until 5).random() + ((0..10).random() / 10f),
                reviewCount = (0 until 200).random(),
                deliveryTimeRange = Pair(0, 20),
                minPrice = 7900,
                deliveryTipRange = Pair(0, 2000)
            ),
            RestaurantEntity(
                id = 5,
                restaurantInfoId = 5,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantTitle = "무탄",
                restaurantImageUrl = "https://i.imgur.com/z65Edca.jpg",
                grade = (1 until 5).random() + ((0..10).random() / 10f),
                reviewCount = (0 until 200).random(),
                deliveryTimeRange = Pair(0, 20),
                minPrice = 7900,
                deliveryTipRange = Pair(0, 2000)
            ),
            RestaurantEntity(
                id = 6,
                restaurantInfoId = 6,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantTitle = "다사랑 스테이크",
                restaurantImageUrl = "https://i.imgur.com/1ZDirfs.jpg",
                grade = (1 until 5).random() + ((0..10).random() / 10f),
                reviewCount = (0 until 200).random(),
                deliveryTimeRange = Pair(0, 20),
                minPrice = 7900,
                deliveryTipRange = Pair(0, 2000)
            ),
            RestaurantEntity(
                id = 7,
                restaurantInfoId = 7,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantTitle = "비너스본사건너편갈비살",
                restaurantImageUrl = "https://i.imgur.com/ar9ym6Z.jpg",
                grade = (1 until 5).random() + ((0..10).random() / 10f),
                reviewCount = (0 until 200).random(),
                deliveryTimeRange = Pair(0, 20),
                minPrice = 7900,
                deliveryTipRange = Pair(0, 2000)
            ),
            RestaurantEntity(
                id = 8,
                restaurantInfoId = 8,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantTitle = "비야게레로",
                restaurantImageUrl = "https://i.imgur.com/P0sj1QY.jpg",
                grade = (1 until 5).random() + ((0..10).random() / 10f),
                reviewCount = (0 until 200).random(),
                deliveryTimeRange = Pair(0, 20),
                minPrice = 7900,
                deliveryTipRange = Pair(0, 2000)
            ),
            RestaurantEntity(
                id = 9,
                restaurantInfoId = 0,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantTitle = "텐지몽",
                restaurantImageUrl = "https://i.imgur.com/45IxW41.jpg",
                grade = (1 until 5).random() + ((0..10).random() / 10f),
                reviewCount = (0 until 200).random(),
                deliveryTimeRange = Pair(0, 20),
                minPrice = 7900,
                deliveryTipRange = Pair(0, 2000)
            )
        )
    }
}