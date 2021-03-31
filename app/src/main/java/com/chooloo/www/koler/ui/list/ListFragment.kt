package com.chooloo.www.koler.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.koler.R
import com.chooloo.www.koler.databinding.FragmentItemsBinding
import com.chooloo.www.koler.ui.base.BaseFragment
import com.chooloo.www.koler.ui.widgets.ListItemHolder
import com.chooloo.www.koler.util.AnimationManager
import com.chooloo.www.koler.util.permissions.runWithPermissions

abstract class ListFragment<A : RecyclerView.Adapter<ListItemHolder>> : BaseFragment(),
    ListContract.View {
    private val _presenter by lazy { ListPresenter<ListContract.View>() }
    private val _binding by lazy { FragmentItemsBinding.inflate(layoutInflater) }
    private var _onScrollStateChangedListener: ((newState: Int) -> Unit?)? = null

    //region list args
    override val requiredPermissions: Array<String>? = null
    override val noResultsMessage by lazy { getString(R.string.error_no_results) }
    override val noPermissionsMessage by lazy { getString(R.string.error_no_permissions) }
    override val itemCount get() = adapter.itemCount
    abstract val adapter: A
    //endregion

    override var emptyStateText: String?
        get() = _binding.itemsEmptyText.text.toString()
        set(value) {
            _binding.itemsEmptyText.text = value

        }

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
            adapter = this@ListFragment.adapter.apply {
                registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                    override fun onChanged() {
                        super.onChanged()
                        if (itemCount == 0) {
                            _presenter.onNoResults()
                        } else {
                            _presenter.onResults()
                        }
                    }
                })
            }
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    _onScrollStateChangedListener?.invoke(newState)
                }
            })
        }

        requiredPermissions?.let {
            runWithPermissions(
                permissions = it,
                grantedCallback = _presenter::onPermissionsGranted,
                blockedCallback = _presenter::onPermissionsBlocked
            )
        } ?: _presenter.onPermissionsGranted()
    }

    override fun attachData() {
        onAttachData()
    }

    override fun animateListView() {
        AnimationManager(_activity).runLayoutAnimation(_binding.itemsRecyclerView)
    }

    override fun showEmptyPage(isShow: Boolean) {
        _binding.apply {
            itemsEmptyText.visibility = if (isShow) VISIBLE else GONE
            itemsRecyclerView.visibility = if (isShow) GONE else VISIBLE
        }
    }

    fun setOnScrollStateChangedListener(onScrollStateChangedListener: ((newState: Int) -> Unit?)?) {
        _onScrollStateChangedListener = onScrollStateChangedListener
    }

    abstract fun onAttachData()
}