package com.example.swoosh.ui.base

import android.app.Dialog
import android.os.Bundle
import com.example.swoosh.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class RoundedBottomDialog : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.CustomBottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)
}