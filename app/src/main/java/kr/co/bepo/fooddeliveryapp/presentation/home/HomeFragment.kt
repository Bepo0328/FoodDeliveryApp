package kr.co.bepo.fooddeliveryapp.presentation.home

import kr.co.bepo.fooddeliveryapp.databinding.FragmentHomeBinding
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override val viewModel: HomeViewModel by viewModel()

    override fun getViewBinding(): FragmentHomeBinding =
        FragmentHomeBinding.inflate(layoutInflater)

    override fun observeData() {}
}