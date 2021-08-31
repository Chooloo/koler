package com.chooloo.www.koler.interactor.dialog

import android.content.DialogInterface
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.util.baseobservable.BaseObservable

class DialogInteractorImpl(
    private val activity: BaseActivity
) : BaseObservable<DialogInteractor.Listener>(), DialogInteractor {
    override fun askForChoice(
        choices: Array<String>,
        iconRes: Int,
        titleRes: Int,
        choiceCallback: (String?, Int) -> Unit,
        cancelCallback: () -> Unit?
    ) {
        val choicesAdapter = ArrayAdapter(activity, R.layout.dialog_choice, choices)
        val alertDialog: AlertDialog = AlertDialog.Builder(activity)
            .setIcon(iconRes)
            .setTitle(titleRes)
            .setNegativeButton(R.string.action_cancel) { dialogInterface, i ->
                cancelCallback.invoke()
                dialogInterface.dismiss()
            }
            .setAdapter(choicesAdapter) { dialog, index ->
                choiceCallback.invoke(choicesAdapter.getItem(index), index)
                dialog.dismiss()
            }
            .show()
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).visibility = View.GONE
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).visibility = View.GONE
    }
}