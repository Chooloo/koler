package com.chooloo.www.koler.interactor.base

import com.chooloo.www.koler.util.BaseObservable


open class BaseInteractorImpl<Listener> : BaseObservable<Listener>(), BaseInteractor<Listener>
