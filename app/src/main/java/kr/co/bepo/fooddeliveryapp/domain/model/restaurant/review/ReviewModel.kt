package kr.co.bepo.fooddeliveryapp.domain.model.restaurant.review

import android.net.Uri
import kr.co.bepo.fooddeliveryapp.domain.model.CellType
import kr.co.bepo.fooddeliveryapp.domain.model.Model

data class ReviewModel(
    override val id: Long,
    override val type: CellType = CellType.REVIEW_CELL,
    val title: String,
    val description: String,
    val grade: Int,
    val thumbnailImageUri: Uri? = null
) : Model(id, type)