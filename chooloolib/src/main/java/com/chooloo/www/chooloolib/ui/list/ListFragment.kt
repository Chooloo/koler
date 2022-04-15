package com.chooloo.www.chooloolib.ui.list

import androidx.core.view.isVisible
import com.chooloo.www.chooloolib.adapter.ListAdapter
import com.chooloo.www.chooloolib.databinding.ItemsBinding
import com.chooloo.www.chooloolib.ui.base.BaseFragment

abstract class ListFragment<ItemType, VS : ListViewState<ItemType>> : BaseFragment<VS>() {
    override val contentView by lazy { binding.root }
    protected val binding by lazy { ItemsBinding.inflate(layoutInflater) }


    override fun onSetup() {
        viewState.apply {
            isEmpty.observe(this@ListFragment, this@ListFragment::showEmpty)
            emptyMessage.observe(this@ListFragment, binding.empty.emptyText::setText)
            emptyIcon.observe(this@ListFragment, binding.empty.emptyIcon::setImageResource)

            filter.observe(this@ListFragment) {
                adapter.titleFilter = it
            }

            isLoading.observe(this@ListFragment) {
                if (it) showEmpty(false)
            }

            isScrollerVisible.observe(this@ListFragment) {
                binding.itemsScrollView.fastScroller.isVisible = it
            }

            itemsChangedEvent.observe(this@ListFragment) { ev ->
                ev.ifNew?.let { adapter.items = it }
            }

            if (args.getBoolean(ARG_OBSERVE, true)) {
                getItemsObservable { it.observe(this@ListFragment, viewState::onItemsChanged) }
            }
        }

        adapter.apply {
            setOnItemClickListener(viewState::onItemClick)
            setOnItemLongClickListener(viewState::onItemLongClick)
            setOnItemLeftButtonClickListener(viewState::onItemLeftClick)
            setOnItemRightButtonClickListener(viewState::onItemRightClick)
            binding.itemsScrollView.setAdapter(this)
        }

        binding.itemsScrollView.fastScroller.setPadding(0, 0, 30, 0)
        args.getString(ARG_FILTER)?.let(viewState::onFilterChanged)
    }

    protected open fun showEmpty(isShow: Boolean) {
        binding.apply {
            empty.emptyIcon.isVisible = isShow
            empty.emptyText.isVisible = isShow
            itemsScrollView.isVisible = !isShow
        }
    }


    abstract val adapter: ListAdapter<ItemType>

    companion object {
        const val ARG_FILTER = "filter"
        const val ARG_OBSERVE = "is_observe"
    }
}