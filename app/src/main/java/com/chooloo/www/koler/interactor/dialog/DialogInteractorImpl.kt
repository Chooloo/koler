package com.chooloo.www.koler.interactor.dialog

import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.util.baseobservable.BaseObservable
import dev.sasikanth.colorsheet.ColorSheet

class DialogInteractorImpl(
    private val activity: BaseActivity
) : BaseObservable<DialogInteractor.Listener>(), DialogInteractor {
    override fun askForChoice(
        choices: Array<String>,
        iconRes: Int,
        titleRes: Int,
        choiceCallback: (String?, Int) -> Unit,
        cancelCallback: (() -> Unit?)?
    ) {
        val choicesAdapter = ArrayAdapter(activity, R.layout.dialog_choice, choices)
        AlertDialog.Builder(activity)
            .setIcon(iconRes)
            .setTitle(titleRes)
            .setAdapter(choicesAdapter) { dialog, index ->
                choiceCallback.invoke(choicesAdapter.getItem(index), index)
                dialog.dismiss()
            }.create().show()
    }

    override fun askForColor(
        colorsArrayRes: Int,
        callback: (selectedColor: Int) -> Unit,
        noColorOption: Boolean,
        selectedColor: Int?
    ) {
        ColorSheet().colorPicker(
            colors = activity.resources.getIntArray(R.array.accent_colors),
            listener = callback::invoke,
            noColorOption = true,
            selectedColor = selectedColor
        ).show(activity.supportFragmentManager)
    }
}
