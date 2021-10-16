package com.chooloo.www.koler.interactor.base

import com.chooloo.www.koler.util.baseobservable.BaseObservable


open class BaseInteractorImpl<Listener> : BaseObservable<Listener>(), BaseInteractor<Listener>
