package kr.co.bepo.fooddeliveryapp.presentation.main

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kr.co.bepo.fooddeliveryapp.R
import kr.co.bepo.fooddeliveryapp.databinding.ActivityMainBinding
import kr.co.bepo.fooddeliveryapp.utility.event.MenuChangeEventBus
import org.koin.android.ext.android.inject
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val navigationController by lazy {
        (supportFragmentManager.findFragmentById(R.id.mainNavigationHostContainer) as NavHostFragment).navController
    }
    private val menuChangeEventBus: MenuChangeEventBus by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observeData()
        initViews()
    }

    private fun initViews() = with(binding) {
        bottomNavigationView.setupWithNavController(navigationController)
    }

    fun goToTab(mainTabMenu: MainTabMenu) = with(binding) {
        bottomNavigationView.selectedItemId = mainTabMenu.menuId
    }

    private fun observeData() {
        lifecycleScope.launch {
            menuChangeEventBus.mainTabMenuFlow.collect {
                goToTab(it)
            }
        }
    }
}

enum class MainTabMenu(@IdRes val menuId: Int) {
    HOME(R.id.menu_home_dest), LIKE(R.id.menu_like_dest), MY(R.id.menu_my_dest)
}