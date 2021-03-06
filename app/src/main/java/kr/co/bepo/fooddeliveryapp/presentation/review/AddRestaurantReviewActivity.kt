package kr.co.bepo.fooddeliveryapp.presentation.review

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import kr.co.bepo.fooddeliveryapp.data.entity.ReviewEntity
import kr.co.bepo.fooddeliveryapp.databinding.ActivityAddRestaurantReviewBinding
import kr.co.bepo.fooddeliveryapp.extensions.toGone
import kr.co.bepo.fooddeliveryapp.extensions.toVisible
import kr.co.bepo.fooddeliveryapp.presentation.review.gallery.GalleryActivity
import kr.co.bepo.fooddeliveryapp.presentation.review.photo.CameraActivity
import kr.co.bepo.fooddeliveryapp.widget.adapter.PhotoListAdapter
import org.koin.android.ext.android.inject


class AddRestaurantReviewActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1000
        private const val URI_LIST_KEY = "uriList"

        const val RESTAURANT_TITLE_KEY = "restaurantTitle"
        const val ORDER_ID_KEY = "orderId"

        fun newIntent(
            context: Context,
            orderId: String,
            restaurantTitle: String
        ) = Intent(context, AddRestaurantReviewActivity::class.java).apply {
            putExtra(ORDER_ID_KEY, orderId)
            putExtra(RESTAURANT_TITLE_KEY, restaurantTitle)
        }
    }

    private var imageUriList: ArrayList<Uri> = arrayListOf()

    private val auth: FirebaseAuth by inject()
    private val storage: FirebaseStorage by inject()
    private val firestore: FirebaseFirestore by inject()

    private val photoListAdapter = PhotoListAdapter { uri -> removePhoto(uri) }

    private val restaurantTitle by lazy { intent.getStringExtra(RESTAURANT_TITLE_KEY)!! }
    private val orderId by lazy { intent.getStringExtra(ORDER_ID_KEY)!! }

    private lateinit var binding: ActivityAddRestaurantReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRestaurantReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() = with(binding) {
        toolbar.setNavigationOnClickListener {
            finish()
        }

        photoRecyclerView.adapter = photoListAdapter

        titleTextView.text = restaurantTitle

        imageAddButton.setOnClickListener {
            showPictureUploadDialog()
        }

        submitButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()
            val userId = auth.currentUser?.uid.orEmpty()
            val rating = binding.ratingBar.rating

            showProgress()

            if (imageUriList.isNotEmpty()) {
                lifecycleScope.launch {
                    val results = uploadPhoto(imageUriList)
                    afterUploadPhoto(userId, title, content, rating, results)
                }
            } else {
                uploadArticle(userId, title, content, rating, listOf())
            }
        }
    }

    private suspend fun uploadPhoto(uriList: List<Uri>) = withContext(Dispatchers.IO) {
        val uploadDeferred: List<Deferred<Any>> = uriList.mapIndexed { index, uri ->
            val timestamp = System.currentTimeMillis()
            lifecycleScope.async {
                try {
                    val fileName = "image_${timestamp}_$index.png"
                    return@async storage
                        .reference
                        .child("article/photo")
                        .child(fileName)
                        .putFile(uri)
                        .await()
                        .storage
                        .downloadUrl
                        .await()
                        .toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@async Pair(uri, e)
                }
            }
        }
        return@withContext uploadDeferred.awaitAll()
    }

    private fun afterUploadPhoto(
        userId: String,
        title: String,
        content: String,
        rating: Float,
        results: List<Any>
    ) {
        val errorResults = results.filterIsInstance<Pair<Uri, Exception>>()
        val successResults = results.filterIsInstance<String>()

        when {
            errorResults.isNotEmpty() && successResults.isNotEmpty() -> {
                photoUploadErrorButContinueDialog(
                    userId,
                    title,
                    content,
                    rating,
                    errorResults,
                    successResults
                )
            }

            errorResults.isNotEmpty() && successResults.isEmpty() -> {
                uploadError()
            }

            else -> {
                uploadArticle(userId, title, content, rating, results.filterIsInstance<String>())
            }
        }
    }

    private fun uploadArticle(
        userId: String,
        title: String,
        content: String,
        rating: Float,
        imageUrlList: List<String>
    ) {
        val review =
            ReviewEntity(
                userId = userId,
                title = title,
                createdAt = System.currentTimeMillis(),
                content = content,
                rating = rating,
                imageUrlList = imageUrlList,
                orderId = orderId,
                restaurantTitle = restaurantTitle
            )

        firestore.collection("review").add(review)

        hideProgress()
        Toast.makeText(this, "????????? ??????????????? ????????? ???????????????.", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startGalleryScreen()
                } else {
                    Toast.makeText(this, "????????? ?????????????????????.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startCameraScreen() {
        val intent = CameraActivity.newIntent(this)
        cameraContent.launch(intent)
    }

    private val cameraContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { intent ->
                    val uriList = intent.getParcelableArrayListExtra<Uri>(URI_LIST_KEY)
                    uriList?.let { list ->
                        imageUriList.addAll(list)
                        photoListAdapter.setPhotoList(imageUriList)

                    }
                } ?: kotlin.run {
                    Toast.makeText(this, "????????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "????????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show()
            }
        }

    private fun startGalleryScreen() {
        val intent = GalleryActivity.newIntent(this)
        galleryContent.launch(intent)
    }

    private val galleryContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { intent ->
                    val uriList = intent.getParcelableArrayListExtra<Uri>(URI_LIST_KEY)
                    uriList?.let { list ->
                        imageUriList.addAll(list)
                        photoListAdapter.setPhotoList(imageUriList)

                    }
                } ?: kotlin.run {
                    Toast.makeText(this, "????????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun showProgress() = with(binding) {
        progressBar.toVisible()
    }

    private fun hideProgress() = with(binding) {
        progressBar.toGone()
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("????????? ???????????????.")
            .setMessage("????????? ???????????? ?????? ????????? ???????????????.")
            .setPositiveButton("????????????") { _, _ ->
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
            .setNegativeButton("????????????") { _, _ -> }
            .create()
            .show()
    }

    private fun checkExternalStoragePermission(uploadAction: () -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                this@AddRestaurantReviewActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                uploadAction()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                showPermissionContextPopup()
            }

            else -> {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun showPictureUploadDialog() {
        AlertDialog.Builder(this)
            .setTitle("????????????")
            .setMessage("??????????????? ????????? ???????????????")
            .setPositiveButton("?????????") { _, _ ->
                checkExternalStoragePermission {
                    startCameraScreen()
                }
            }
            .setNegativeButton("?????????") { _, _ ->
                checkExternalStoragePermission {
                    startGalleryScreen()
                }
            }
            .create()
            .show()
    }

    private fun photoUploadErrorButContinueDialog(
        userId: String,
        title: String,
        content: String,
        rating: Float,
        errorResults: List<Pair<Uri, Exception>>,
        successResults: List<String>
    ) {
        AlertDialog.Builder(this)
            .setTitle("?????? ????????? ????????? ??????")
            .setMessage("???????????? ????????? ???????????? ????????????." + errorResults.map { (uri, _) ->
                "$uri\n"
            } + "???????????? ???????????? ????????? ???????????????????")
            .setPositiveButton("?????????") { _, _ ->
                uploadArticle(userId, title, content, rating, successResults)
            }
            .create()
            .show()
    }

    private fun uploadError() {
        Toast.makeText(this, "?????? ???????????? ??????????????????.", Toast.LENGTH_SHORT).show()
        hideProgress()
    }


    private fun removePhoto(uri: Uri) {
        imageUriList.remove(uri)
        photoListAdapter.setPhotoList(imageUriList)
    }
}