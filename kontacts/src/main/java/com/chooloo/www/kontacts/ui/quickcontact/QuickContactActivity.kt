package com.chooloo.www.kontacts.ui.quickcontact

import androidx.activity.viewModels
import com.chooloo.www.chooloolib.interactor.prompt.PromptsInteractor
import com.chooloo.www.chooloolib.ui.base.BaseActivity
import com.chooloo.www.kontacts.databinding.QuickContactBinding
import com.chooloo.www.kontacts.di.factory.fragment.FragmentFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class QuickContactActivity : BaseActivity<QuickContactViewState>() {
    override val contentView by lazy { binding.root }
    override val viewState: QuickContactViewState by viewModels()

    private val binding by lazy { QuickContactBinding.inflate(layoutInflater) }

    @Inject lateinit var prompts: PromptsInteractor
    @Inject lateinit var fragmentFactory: FragmentFactory


    override fun onSetup() {
        viewState.apply {
            showContactEvent.observe(this@QuickContactActivity) {
                it.ifNew?.let { contactId ->
                    prompts.showFragment(fragmentFactory.getContactFragment(contactId).apply {
                        setOnFinishListener { this@QuickContactActivity.viewState.onFragmentFinish() }
                    })
                }
            }
        }

        viewState.onIntent(intent)
    }
}