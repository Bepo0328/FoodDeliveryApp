package kr.co.bepo.fooddeliveryapp.widget.adapter.viewholder.review

import kr.co.bepo.fooddeliveryapp.databinding.ViewholderReviewBinding
import kr.co.bepo.fooddeliveryapp.domain.model.review.ReviewModel
import kr.co.bepo.fooddeliveryapp.extensions.clear
import kr.co.bepo.fooddeliveryapp.extensions.load
import kr.co.bepo.fooddeliveryapp.extensions.toGone
import kr.co.bepo.fooddeliveryapp.extensions.toVisible
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseViewModel
import kr.co.bepo.fooddeliveryapp.utility.provider.ResourcesProvider
import kr.co.bepo.fooddeliveryapp.widget.adapter.listener.AdapterListener
import kr.co.bepo.fooddeliveryapp.widget.adapter.viewholder.ModelViewHolder

class ReviewViewHolder(
    private val binding: ViewholderReviewBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<ReviewModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        reviewThumbnailImageView.clear()
        reviewThumbnailImageView.toGone()
    }

    override fun bindData(model: ReviewModel) = with(binding) {
        super.bindData(model)

        if (model.thumbnailImageUri != null) {
            reviewThumbnailImageView.toVisible()
            reviewThumbnailImageView.load(model.thumbnailImageUri.toString(), 24f)
        } else {
            reviewThumbnailImageView.toGone()
        }
        reviewUserTextView.text = model.title
        ratingBar.rating = model.grade
        reviewTextView.text = model.description
    }

    override fun bindViews(model: ReviewModel, adapterListener: AdapterListener) = Unit

}