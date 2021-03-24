package com.example.swoosh.ui.nav

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import com.example.swoosh.MainActivity
import com.example.swoosh.R
import com.example.swoosh.data.model.DrawerItem
import com.example.swoosh.ui.base.UserEditDialog
import com.example.swoosh.utils.SandwichState
import com.example.swoosh.utils.themeColor
import com.example.swoosh.utils.themeInterpolator
import com.google.android.material.animation.AnimationUtils.DECELERATE_INTERPOLATOR
import com.google.android.material.animation.AnimationUtils.lerp
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.shape.MaterialShapeDrawable
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*
import kotlinx.android.synthetic.main.user_info.*
import kotlin.math.abs

class BottomSheet : Fragment() {

    private val tabs = arrayListOf<DrawerItem>(
            DrawerItem("Boards", R.drawable.ic_baseline_dashboard_24, R.id.nav_home),
            DrawerItem("Chats", R.drawable.ic_baseline_chat_24, R.id.nav_chat)
    )
    val behaviour: BottomSheetBehavior<FrameLayout> by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetBehavior.from(background_container)
    }
    private val foregroundShapeDrawable: MaterialShapeDrawable by lazy(LazyThreadSafetyMode.NONE) {
        val foregroundContext = foreground_container.context
        MaterialShapeDrawable(
            foregroundContext,
            null,
            R.attr.bottomSheetStyle,
            0
        ).apply {
            fillColor = ColorStateList.valueOf(
                foregroundContext.themeColor(R.attr.colorPrimary)
            )
            setCornerSize(12f)
            shadowCompatibilityMode = MaterialShapeDrawable.SHADOW_COMPAT_MODE_NEVER
            shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                .setTopEdge(
                    SemiCircleEdgeCutoutTreatment(
                        resources.getDimension(R.dimen.grid_1),
                        resources.getDimension(R.dimen.grid_3),
                        0F,
                        resources.getDimension(R.dimen.navigation_drawer_profile_image_size_padded)
                    )
                )
                .build()
        }
    }

    var sandwichState = SandwichState.CLOSED

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        close()

        scrim_view.setOnClickListener{
            if (sandwichState == SandwichState.OPEN) animateCloseSandwich()
            close()
        }

        foreground_container.background = foregroundShapeDrawable
        sheet_recycler.adapter = NavAdapter(tabs, requireActivity())

        profile_image.setOnClickListener{
            animateOpenSandwich()
        }

        user_edit_btn.setOnClickListener{
            UserEditDialog().show(childFragmentManager, UserEditDialog.TAG)
        }
    }

    fun toggle(){
        when{
            sandwichState == SandwichState.OPEN -> {
                animateCloseSandwich()
            }
            behaviour.state == BottomSheetBehavior.STATE_HIDDEN -> open()
            behaviour.state == BottomSheetBehavior.STATE_HIDDEN
                    || behaviour.state == BottomSheetBehavior.STATE_HALF_EXPANDED
                    || behaviour.state == BottomSheetBehavior.STATE_EXPANDED
                    || behaviour.state == BottomSheetBehavior.STATE_COLLAPSED -> close()
        }
    }

    fun open(){
        scrim_view.visibility = View.VISIBLE
        behaviour.state = BottomSheetBehavior.STATE_HALF_EXPANDED
    }
    fun close(){
        scrim_view.visibility = View.GONE
        behaviour.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun animateOpenSandwich(){
        foreground_container.animate().translationY(600f).setDuration(300).setInterpolator(DECELERATE_INTERPOLATOR)
        profile_image.animate().scaleX(0f).scaleY(0f).setDuration(150).setInterpolator(AccelerateInterpolator())
        user_info_container.animate().alpha(1f).setDuration(300).setInterpolator(AccelerateDecelerateInterpolator())
        (requireActivity() as MainActivity).rotateOpen()
        sandwichState = SandwichState.OPEN
    }

    fun animateCloseSandwich(){
        foreground_container.animate().translationY(0f).setDuration(300).setInterpolator(DECELERATE_INTERPOLATOR)
        profile_image.animate().scaleX(1f).scaleY(1f).setDuration(150).setInterpolator(AccelerateInterpolator())
        user_info_container.animate().alpha(0f).setDuration(300).setInterpolator(AccelerateDecelerateInterpolator())
        (requireActivity() as MainActivity).rotateClose()
        sandwichState = SandwichState.CLOSED
    }
}