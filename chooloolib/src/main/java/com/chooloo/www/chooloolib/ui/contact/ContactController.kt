package com.chooloo.www.chooloolib.ui.contact

import com.chooloo.www.chooloolib.ui.base.BaseController
import javax.inject.Inject

class ContactController @Inject constructor(
    view: ContactContract.View
) :
    BaseController<ContactContract.View>(view),
    ContactContract.Controller {
}