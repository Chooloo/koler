package com.chooloo.www.chooloolib.interactor.dialog

import android.content.Context
import androidx.annotation.StringRes
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.di.factory.fragment.FragmentFactory
import com.chooloo.www.chooloolib.interactor.callaudio.CallAudiosInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor.Companion.Page
import com.chooloo.www.chooloolib.interactor.prompt.PromptsInteractor
import com.chooloo.www.chooloolib.interactor.sim.SimsInteractor
import com.chooloo.www.chooloolib.interactor.string.StringsInteractor
import com.chooloo.www.chooloolib.model.SimAccount
import com.chooloo.www.chooloolib.ui.base.BaseActivity
import com.chooloo.www.chooloolib.util.baseobservable.BaseObservable
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import dev.sasikanth.colorsheet.ColorSheet
import javax.inject.Inject

@ActivityScoped
class DialogsInteractorImpl @Inject constructor(
    @ActivityContext context: Context,
    private val sims: SimsInteractor,
    private val strings: StringsInteractor,
    private val prompts: PromptsInteractor,
    private val callAudios: CallAudiosInteractor,
    private val fragmentsFactory: FragmentFactory
) : BaseObservable<DialogsInteractor.Listener>(), DialogsInteractor {

    private val activity = context as BaseActivity<*>


    override fun askForBoolean(titleRes: Int, callback: (result: Boolean) -> Unit) {
        prompts.showFragment(fragmentsFactory.getPromptFragment(
            strings.getString(R.string.prompt_yes_or_no),
            strings.getString(titleRes)
        ).apply {
            setOnItemClickListener(callback::invoke)
        })
    }

    override fun askForValidation(@StringRes titleRes: Int, callback: (result: Boolean) -> Unit) {
        prompts.showFragment(fragmentsFactory.getPromptFragment(
            strings.getString(R.string.prompt_are_you_sure),
            strings.getString(titleRes)
        ).apply {
            setOnItemClickListener(callback::invoke)
        })
    }


    override fun askForChoice(
        choices: List<String>,
        @StringRes titleRes: Int,
        @StringRes subtitleRes: Int?,
        choiceCallback: (String) -> Unit
    ) {
        prompts.showFragment(
            fragmentsFactory.getChoicesFragment(titleRes, subtitleRes, choices).apply {
                setOnChoiceClickListener {
                    choiceCallback.invoke(it)
                    this@apply.finish()
                }
            })
    }

    override fun <T> askForChoice(
        choices: List<T>,
        choiceToString: (T) -> String,
        @StringRes titleRes: Int,
        @StringRes subtitleRes: Int?,
        choiceCallback: (T) -> Unit
    ) {
        val objectsToStringMap = choices.associateBy({ choiceToString.invoke(it) }, { it })
        askForChoice(
            objectsToStringMap.keys.toList(),
            titleRes,
            subtitleRes
        ) {
            choiceCallback.invoke(objectsToStringMap[it]!!)
        }
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

    override fun askForSim(callback: (SimAccount?) -> Unit) {
        sims.getSimAccounts { simAccounts ->
            askForChoice(
                choices = simAccounts,
                choiceCallback = callback::invoke,
                choiceToString = SimAccount::label,
                titleRes = R.string.hint_sim_account,
                subtitleRes = R.string.explain_choose_sim_account
            )
        }
    }

    override fun askForDefaultPage(callback: (Page) -> Unit) {
        askForChoice(
            choices = Page.values().toList(),
            titleRes = R.string.hint_default_page,
            choiceCallback = { callback.invoke(it) },
            subtitleRes = R.string.explain_choose_default_page,
            choiceToString = { strings.getString(it.titleRes) }
        )
    }

    override fun askForCompact(callback: (isCompact: Boolean) -> Unit) {
        askForBoolean(R.string.hint_compact_mode, callback)
    }

    override fun askForShowBlock(callback: (isShowBlock: Boolean) -> Unit) {
        askForBoolean(R.string.hint_show_blocked, callback)
    }

    override fun askForAnimations(callback: (isAnimations: Boolean) -> Unit) {
        askForBoolean(R.string.hint_animations, callback)
    }

    override fun askForShouldAskSim(callback: (shouldAskSim: Boolean) -> Unit) {
        askForBoolean(R.string.hint_should_ask_sim, callback)
    }

    override fun askForRoute(callback: (CallAudiosInteractor.AudioRoute) -> Unit) {
        askForChoice(
            choiceCallback = { callback.invoke(it) },
            titleRes = R.string.action_choose_audio_route,
            subtitleRes = R.string.explain_choose_audio_route,
            choiceToString = { strings.getString(it.stringRes) },
            choices = callAudios.supportedAudioRoutes.toList()
        )
    }
}
