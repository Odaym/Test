package com.example.myapplication

import android.view.ViewGroup
import kotlinx.android.synthetic.main.two_buttons.view.*

class TwoImagesHolder(parent: ViewGroup) : DataViewHolder(
    R.layout.two_buttons, parent
) {
    override fun bind(data: DataProvider) {
        when (data) {
            is TextProvider -> {
                itemView.button1.setText(data.textResId)
                itemView.button2.setText(data.textResId.toString().capitalize())
            }
        }
    }

    class Factory : DataViewHolder.Factory {
        val twoImagesID = 200

        override val identifier: Int
            get() = twoImagesID

        override fun create(parent: ViewGroup): DataViewHolder {
            return TwoImagesHolder(parent)
        }
    }
}