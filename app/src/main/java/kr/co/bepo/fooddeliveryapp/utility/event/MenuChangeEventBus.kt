package kr.co.bepo.fooddeliveryapp.utility.event

import kotlinx.coroutines.flow.MutableSharedFlow
import kr.co.bepo.fooddeliveryapp.presentation.main.MainTabMenu

class MenuChangeEventBus {

    val mainTabMenuFlow = MutableSharedFlow<MainTabMenu>()

    suspend fun changeMenu(menu: MainTabMenu) {
        mainTabMenuFlow.emit(menu)
    }
}