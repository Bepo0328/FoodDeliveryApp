package kr.co.bepo.fooddeliveryapp.presentation.my

import kr.co.bepo.fooddeliveryapp.databinding.FragmentMyBinding
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

class MyFragment : BaseFragment<MyViewModel, FragmentMyBinding>() {

    override val viewModel: MyViewModel by viewModel()

    override fun getViewBinding(): FragmentMyBinding =
        FragmentMyBinding.inflate(layoutInflater)

    override fun observeData() {}
}