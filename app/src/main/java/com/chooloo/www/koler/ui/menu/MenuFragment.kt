package com.chooloo.www.koler.ui.menu

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import com.chooloo.www.koler.adapter.MenuAdapter
import com.chooloo.www.koler.databinding.FragmentMenuBinding
import com.chooloo.www.koler.ui.base.BaseBottomSheetDialogFragment

class MenuFragment : BaseBottomSheetDialogFragment(), MenuMvpView {

    private lateinit var _binding: FragmentMenuBinding
    private var _onMenuItemClickListener: ((MenuItem) -> Unit?)? = null
    protected lateinit var adapter: MenuAdapter
    protected lateinit var menu: Menu

    companion object {
        private const val ARG_MENU_LAYOUT = "menu_layout"

        @JvmStatic
        fun newInstance(@MenuRes menuLayout: Int): MenuFragment {
            return MenuFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_MENU_LAYOUT, menuLayout)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMenuBinding.inflate(layoutInflater)
        return _binding.root
    }

    override fun onSetup() {
        menu = PopupMenu(activity, null).menu

        arguments?.getInt(ARG_MENU_LAYOUT)?.let { activity.menuInflater.inflate(it, menu) }

        adapter = MenuAdapter(activity, menu)
        adapter.setOnMenuItemClickListener(_onMenuItemClickListener)

        _binding.menuRecyclerView.adapter = adapter
    }

    fun setOnMenuItemClickListener(onMenuItemClickListener: ((MenuItem) -> Unit?)?) {
        _onMenuItemClickListener = onMenuItemClickListener
    }
}