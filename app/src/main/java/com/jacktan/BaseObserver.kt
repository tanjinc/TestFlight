package com.jacktan

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class BaseObserver<T>: Observer<T> {
    val TAG = "BaseObserver"
    override fun onComplete() {
    }

    override fun onNext(it: T) {
    }
    override fun onError(t: Throwable) {
        onComplete()
    }

    override fun onSubscribe(d: Disposable) {
    }
}