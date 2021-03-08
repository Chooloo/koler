package com.chooloo.www.koler.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.koler.R
import com.chooloo.www.koler.databinding.FragmentItemsBinding
import com.chooloo.www.koler.ui.base.BaseFragment
import com.chooloo.www.koler.ui.widgets.ListItemHolder
import com.chooloo.www.koler.util.runLayoutAnimation

abstract class ListFragment<A : RecyclerView.Adapter<ListItemHolder>> : BaseFragment(),
    ListContract.View {
    protected val listAdapter by lazy { onGetAdapter() }
    private val _presenter by lazy { ListPresenter<ListContract.View>() }
    private val _binding by lazy { FragmentItemsBinding.inflate(layoutInflater) }
    private var _onScrollStateChangedListener: ((newState: Int) -> Unit?)? = null

    override val itemCount: Int
        get() = listAdapter.itemCount

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = _binding.root

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
    }

    override fun onSetup() {
        _presenter.attach(this)

        _binding.itemsRecyclerView.apply {
            adapter = listAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    _onScrollStateChangedListener?.invoke(newState)
                }
            })
        }

        showPermissionsPage(false)
        showEmptyPage(false)
    }

    override fun showEmptyPage(isShow: Boolean) {
        _binding.apply {
            emptyState.apply {
                emptyTitle.text = getString(R.string.empty_list_no_results)
                emptyState.visibility = if (isShow) View.VISIBLE else View.GONE
            }
            itemsRecyclerView.visibility = if (isShow) View.GONE else View.VISIBLE
        }
    }

    override fun showPermissionsPage(isShow: Boolean) {
        _binding.apply {
            emptyState.apply {
                emptyTitle.text = getString(R.string.empty_list_no_permissions)
                emptyState.visibility = if (isShow) View.VISIBLE else View.GONE
            }
            itemsRecyclerView.visibility = if (isShow) View.GONE else View.VISIBLE
        }
    }

    override fun animateListView() {
        runLayoutAnimation(_binding.itemsRecyclerView)
    }

    fun setOnScrollStateChangedListener(onScrollStateChangedListener: ((newState: Int) -> Unit?)?) {
        _onScrollStateChangedListener = onScrollStateChangedListener
    }

    abstract fun onGetAdapter(): A
}