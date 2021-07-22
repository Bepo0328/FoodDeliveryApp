package kr.co.bepo.fooddeliveryapp.presentation.my

import android.app.Activity
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kr.co.bepo.fooddeliveryapp.R
import kr.co.bepo.fooddeliveryapp.databinding.FragmentMyBinding
import kr.co.bepo.fooddeliveryapp.domain.model.order.OrderModel
import kr.co.bepo.fooddeliveryapp.extensions.load
import kr.co.bepo.fooddeliveryapp.extensions.toGone
import kr.co.bepo.fooddeliveryapp.extensions.toVisible
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseFragment
import kr.co.bepo.fooddeliveryapp.utility.provider.ResourcesProvider
import kr.co.bepo.fooddeliveryapp.widget.adapter.ModelRecyclerAdapter
import kr.co.bepo.fooddeliveryapp.widget.adapter.listener.AdapterListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MyFragment : BaseFragment<MyViewModel, FragmentMyBinding>() {

    override val viewModel: MyViewModel by viewModel()

    override fun getViewBinding(): FragmentMyBinding =
        FragmentMyBinding.inflate(layoutInflater)


    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
    private val gsc by lazy { GoogleSignIn.getClient(requireContext(), gso) }
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val loginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    task.getResult(ApiException::class.java)?.let { account ->
                        viewModel.saveToken(account.idToken ?: throw Exception())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    private val resourcesProvider: ResourcesProvider by inject()

    private val adapter by lazy {
        ModelRecyclerAdapter<OrderModel, MyViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : AdapterListener {}
        )
    }

    override fun initViews() = with(binding) {
        loginButton.setOnClickListener {
            signInGoogle()
        }

        logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            viewModel.signOut()
        }

        recyclerView.adapter = adapter
    }

    override fun observeData() = viewModel.myStateLiveData.observe(viewLifecycleOwner) {
        when (it) {
            is MyState.Loading -> handleLoadingState()
            is MyState.Success -> handleSuccessState(it)
            is MyState.Login -> handleLoginState(it)
            is MyState.Error -> Unit
            else -> Unit
        }
    }

    private fun signInGoogle() {
        val signInIntent = gsc.signInIntent
        loginLauncher.launch(signInIntent)
    }

    private fun handleLoadingState() = with(binding) {
        loginRequiredGroup.toGone()
        progressBar.toVisible()
    }

    private fun handleSuccessState(state: MyState.Success) = with(binding) {
        progressBar.toGone()
        when (state) {
            is MyState.Success.Registered -> handleRegisteredState(state)
            is MyState.Success.NotRegistered -> {
                profileGroup.toGone()
                loginRequiredGroup.toVisible()
            }
        }
    }

    private fun handleRegisteredState(state: MyState.Success.Registered) = with(binding) {
        profileGroup.toVisible()
        loginRequiredGroup.toGone()
        profileImageView.load(state.profileImageUri.toString(), 60f)
        userNameTextView.text = state.userName
        adapter.submitList(state.orderList)
    }

    private fun handleLoginState(state: MyState.Login) = with(binding) {
        progressBar.toVisible()
        val credential = GoogleAuthProvider.getCredential(state.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    viewModel.setUserInfo(user)
                } else {
                    firebaseAuth.signOut()
                    viewModel.setUserInfo(null)
                }
            }
    }

}