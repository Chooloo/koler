package com.chooloo.www.koler.interactor.dialog

import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.chooloo.www.koler.R
import com.chooloo.www.koler.interactor.callaudio.CallAudioInteractor
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor.Companion.Page
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.prompt.PromptFragment
import com.chooloo.www.koler.util.baseobservable.BaseObservable
import dev.sasikanth.colorsheet.ColorSheet

class DialogInteractorImpl(
    private val activity: BaseActivity
) : BaseObservable<DialogInteractor.Listener>(), DialogInteractor {
    override fun askForBoolean(titleRes: Int, callback: (result: Boolean) -> Unit) {
        BottomFragment(
            PromptFragment.newInstance(
                activity.getString(R.string.prompt_yes_or_no),
                activity.getString(titleRes)
            ).apply {
                controller.setOnClickListener(callback::invoke)
            }).show(activity.supportFragmentManager, PromptFragment.TAG)
    }

    override fun askForValidation(titleRes: Int, callback: (result: Boolean) -> Unit) {
        BottomFragment(
            PromptFragment.newInstance(
                activity.getString(R.string.prompt_are_you_sure),
                activity.getString(titleRes)
            ).apply {
                controller.setOnClickListener(callback::invoke)
            }).show(activity.supportFragmentManager, PromptFragment.TAG)
    }


    override fun askForChoice(
        choices: List<String>,
        iconRes: Int,
        titleRes: Int,
        choiceCallback: (String?, Int) -> Unit,
        cancelCallback: (() -> Unit?)?
    ) {
        val choicesAdapter =
            ArrayAdapter(activity, R.layout.dialog_choice, choices)
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

    override fun askForRoute(callback: (CallAudioInteractor.AudioRoute) -> Unit) {
        val audioRoutes = activity.component.callAudios.supportedAudioRoutes
        askForChoice(
            audioRoutes.map { activity.getString(it.stringRes) },
            R.drawable.ic_volume_up_black_24dp,
            R.string.action_choose_audio_route,
            { _, index -> callback.invoke(audioRoutes[index]) }
        )
    }

    override fun askForDefaultPage(callback: (Page) -> Unit) {
        askForChoice(
            choices = Page.values().map { activity.getString(it.titleRes) },
            iconRes = R.drawable.round_view_carousel_24,
            titleRes = R.string.hint_default_page,
            choiceCallback = { _, index -> callback.invoke(Page.values()[index]) }
        )
    }

    override fun askForCompact(callback: (isCompact: Boolean) -> Unit) {
        askForBoolean(R.string.hint_compact_mode, callback)
    }

    override fun askForAnimations(callback: (isAnimations: Boolean) -> Unit) {
        askForBoolean(R.string.hint_animations, callback)
    }

    override fun askForShouldAskSim(callback: (shouldAskSim: Boolean) -> Unit) {
        askForBoolean(R.string.hint_should_ask_sim, callback)
    }
}
