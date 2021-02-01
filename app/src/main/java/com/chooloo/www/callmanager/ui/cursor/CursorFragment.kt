package com.chooloo.www.callmanager.ui.cursor

import android.Manifest.permission
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.callmanager.R
import com.chooloo.www.callmanager.adapter.CursorAdapter
import com.chooloo.www.callmanager.databinding.FragmentItemsBinding
import com.chooloo.www.callmanager.ui.base.BaseFragment
import com.chooloo.www.callmanager.util.AnimationUtils.runLayoutAnimation

abstract class CursorFragment<A : CursorAdapter<*>> : BaseFragment(), CursorMvpView {

    private var _onScrollListener: RecyclerView.OnScrollListener? = null
    private lateinit var _presenter: CursorMvpPresenter<CursorMvpView>
    protected lateinit var adapter: A
    protected lateinit var binding: FragmentItemsBinding

    override val itemCount: Int
        get() = adapter.itemCount

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentItemsBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _presenter.detach()
    }

    override fun onSetup() {
        _presenter = CursorPresenter()
        _presenter.attach(this)

        adapter = onGetAdapter()

        binding.run {
            itemsRecyclerView.adapter = adapter
            itemsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    _onScrollListener?.onScrollStateChanged(recyclerView, newState)
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    _onScrollListener?.onScrolled(recyclerView, dx, dy)
                }
            })
        }

        // TODO make this no permission handling better
        if (!hasPermission(permission.READ_CONTACTS)) {
            _presenter.onNoPermissions()
        }
    }

    override fun updateData(cursor: Cursor) {
        adapter.setCursor(cursor)
        adapter.notifyDataSetChanged()
    }

    override fun showEmptyPage(isShow: Boolean) {
        binding.emptyState.emptyTitle.text = getString(R.string.empty_list_no_results)
        binding.emptyState.emptyState.visibility = if (isShow) View.VISIBLE else View.GONE
        binding.itemsRecyclerView.visibility = if (isShow) View.GONE else View.VISIBLE
    }

    override fun showNoPermissions(isShow: Boolean) {
        binding.emptyState.emptyTitle.text = getString(R.string.empty_list_no_permissions)
        binding.emptyState.emptyState.visibility = if (isShow) View.VISIBLE else View.GONE
        binding.itemsRecyclerView.visibility = if (isShow) View.GONE else View.VISIBLE
    }

    override fun animateListView() {
        runLayoutAnimation(binding.itemsRecyclerView)
    }

    override fun setOnScrollListener(onScrollListener: RecyclerView.OnScrollListener) {
        _onScrollListener = onScrollListener
    }

    abstract fun onGetAdapter(): A
}