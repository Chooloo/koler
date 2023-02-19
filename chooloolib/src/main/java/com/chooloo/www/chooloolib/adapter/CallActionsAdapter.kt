package com.chooloo.www.chooloolib.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.chooloolib.databinding.CallActionBinding
import com.chooloo.www.chooloolib.ui.callactions.CallAction

class CallActionsAdapter : RecyclerView.Adapter<CallActionsAdapter.ViewHolder>() {
    private var _callActions: MutableList<CallAction> = mutableListOf()
    private var _onCallActionClickListener: (CallAction) -> Unit = {}


    override fun getItemCount() = _callActions.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(CallActionBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(_callActions[position], _onCallActionClickListener::invoke)


    private fun getCallAction(@StringRes idRes: Int): CallAction? {
        val index = _callActions.indexOfFirst { it.idRes == idRes }
        if (index == -1) return null
        return _callActions[index]
    }

    private fun changeCallAction(@StringRes idRes: Int, change: (CallAction) -> Unit) {
        val index = _callActions.indexOfFirst { it.idRes == idRes }
        if (index == -1) return
        change.invoke(_callActions[index])
        notifyItemChanged(index)
    }

    fun isCallActionEnabled(@StringRes idRes: Int) = getCallAction(idRes)?.isEnabled ?: false

    fun isCallActionActivated(@StringRes idRes: Int) = getCallAction(idRes)?.isActivated ?: false

    fun setCallActionEnabled(@StringRes idRes: Int, isEnabled: Boolean) {
        changeCallAction(idRes) { it.isEnabled = isEnabled }
    }

    fun setCallActionActivated(@StringRes idRes: Int, isActivated: Boolean) {
        changeCallAction(idRes) { it.isActivated = isActivated }
    }

    fun setCallActionIcon(@StringRes idRes: Int, @DrawableRes iconRes: Int) {
        changeCallAction(idRes) { it.tempIconRes = iconRes }
    }

    fun addCallActions(callActions: List<CallAction>) {
        for (callAction in callActions) {
            if (_callActions.contains(callAction)) continue
            _callActions.add(callAction)
            notifyItemChanged(_callActions.indexOf(callAction))
        }
    }

    fun removeCallActions(callActions: List<CallAction>) {
        for (callAction in callActions) {
            if (!_callActions.contains(callAction)) continue
            val removedIndex = _callActions.indexOf(callAction)
            _callActions.remove(callAction)
            notifyItemRemoved(removedIndex)
        }
    }

    fun setOnCallActionClickListener(onCallActionClickListener: (CallAction) -> Unit = {}) {
        _onCallActionClickListener = onCallActionClickListener
    }


    class ViewHolder(private val binding: CallActionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(callAction: CallAction, onClickListener: (CallAction) -> Unit = {}) {
            binding.callActionRoot.apply {
                setDefaultIcon(callAction.tempIconRes ?: callAction.iconRes)
                callAction.checkedIconRes?.let(::setCheckedIcon)
                isEnabled = callAction.isEnabled
                isActivated = callAction.isActivated
                setOnClickListener { onClickListener.invoke(callAction) }
            }
        }
    }
}
