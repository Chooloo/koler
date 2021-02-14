package com.chooloo.www.koler.ui.list

import android.Manifest.permission
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

abstract class ListFragment<A : RecyclerView.Adapter<ListItemHolder>> : BaseFragment(), ListMvpView {

    private var _onScrollStateChangedListener: ((newState: Int) -> Unit?)? = null
    private lateinit var _presenter: ListMvpPresenter<ListMvpView>
    private lateinit var _binding: FragmentItemsBinding
    protected lateinit var _adapter: A

    override val itemCount: Int
        get() = _adapter.itemCount

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentItemsBinding.inflate(inflater)
        return _binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _presenter.detach()
    }

    override fun onSetup() {
        _presenter = ListPresenter()
        _presenter.attach(this)

        _adapter = onGetAdapter()

        _binding.run {
            itemsRecyclerView.apply {
                adapter = _adapter
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        _onScrollStateChangedListener?.invoke(newState)
                    }
                })
            }
        }

        // TODO make this no permission handling better
        if (!hasPermission(permission.READ_CONTACTS)) {
            _presenter.onNoPermissions()
        }
    }

    override fun showEmptyPage(isShow: Boolean) {
        _binding.emptyState.emptyTitle.text = getString(R.string.empty_list_no_results)
        _binding.emptyState.emptyState.visibility = if (isShow) View.VISIBLE else View.GONE
        _binding.itemsRecyclerView.visibility = if (isShow) View.GONE else View.VISIBLE
    }

    override fun showNoPermissions(isShow: Boolean) {
        _binding.emptyState.emptyTitle.text = getString(R.string.empty_list_no_permissions)
        _binding.emptyState.emptyState.visibility = if (isShow) View.VISIBLE else View.GONE
        _binding.itemsRecyclerView.visibility = if (isShow) View.GONE else View.VISIBLE
    }

    override fun animateListView() {
        runLayoutAnimation(_binding.itemsRecyclerView)
    }

    fun setOnScrollStateChangedListener(onScrollStateChangedListener: ((newState: Int) -> Unit?)?) {
        _onScrollStateChangedListener = onScrollStateChangedListener
    }

    abstract fun onGetAdapter(): A
}