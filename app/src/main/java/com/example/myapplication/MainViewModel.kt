package com.example.myapplication

import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class MainViewModel {
    val elements = BehaviorSubject.create<List<ViewHolderDataProvider>>()

    init {
        elements.onNext(
            listOf(
                TwoImagesDataProvider(R.string.app_name)
            )
        )
    }

    class TwoImagesDataProvider(override val textResId: Int) : ViewHolderDataProvider,
        TextProvider {
        override val identifier
            get() = 200
        override val identity = textResId
        override val hash = 0
    }
}
