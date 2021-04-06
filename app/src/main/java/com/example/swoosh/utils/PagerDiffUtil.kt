package com.example.swoosh.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.swoosh.data.model.BoardItem
import com.example.swoosh.ui.base.BoardItemFragment

class PagerDiffUtil(
        private val oldList: List<BoardItemFragment>,
        private val newList: List<BoardItemFragment>) : DiffUtil.Callback() {

    enum class PayloadKey {
        VALUE
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].theSame(newList[newItemPosition])
    }


}