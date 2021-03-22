package com.example.swoosh.ui.nav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.swoosh.R
import com.example.swoosh.data.model.DrawerItem
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*

class BottomSheet : Fragment() {

    private val tabs = arrayListOf<DrawerItem>(
            DrawerItem("Boards", R.drawable.ic_baseline_dashboard_24, R.id.nav_home),
            DrawerItem("Chats", R.drawable.ic_baseline_chat_24, R.id.nav_chat)
    )
    val behaviour: BottomSheetBehavior<FrameLayout> by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetBehavior.from(background_container)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        close()

        scrim_view.setOnClickListener{close()}

        sheet_recycler.adapter = NavAdapter(tabs, requireActivity())
    }

    fun toggle(){
        when{
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

}