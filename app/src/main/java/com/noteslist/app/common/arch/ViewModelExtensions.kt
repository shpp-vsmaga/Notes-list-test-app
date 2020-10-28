package com.noteslist.app.common.arch

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.disposables.Disposable

fun <T> Flowable<T>.postToViewStateLiveData(targetLiveData: ViewStateLiveData<T>): Disposable =
    doOnSubscribe { targetLiveData.onStart() }
        .subscribe(
            { result -> targetLiveData.onNext(result) },
            { error -> targetLiveData.onError(error) },
            { targetLiveData.onComplete() }
        )

fun <T> Single<T>.postToViewStateLiveData(targetLiveData: ViewStateLiveData<T>): Disposable =
    doOnSubscribe { targetLiveData.onStart() }
        .subscribe(
            { result -> targetLiveData.onNext(result) },
            { error -> targetLiveData.onError(error) }
        )

fun <T> Maybe<T>.postToViewStateLiveData(targetLiveData: ViewStateLiveData<T>): Disposable =
    doOnSubscribe { targetLiveData.onStart() }
        .subscribe(
            { result -> targetLiveData.onNext(result) },
            { error -> targetLiveData.onError(error) }
        )

fun Completable.postToViewStateLiveData(targetLiveData: ViewStateLiveData<Nothing>): Disposable =
    doOnSubscribe { targetLiveData.onStart() }
        .subscribe(
            { targetLiveData.onComplete() },
            { error -> targetLiveData.onError(error) }
        )

/**
 * Common way of consuming [LiveData] with [ViewState].
 *
 * Passes provided callbacks to [ViewState.either] under the hood,
 * so for callbacks behavior title take a look at [ViewState.either] docs.
 */
fun <DataType> LiveData<ViewState<DataType>>.observeViewState(
    owner: LifecycleOwner,
    dataCallback: ((DataType) -> Unit)? = null,
    errorCallback: ((Throwable) -> Unit)? = null,
    completeCallback: (() -> Unit)? = null,
    loadingCallback: ((Boolean) -> Unit)? = null
) {
    this.observe(owner,
        Observer { viewState ->
            viewState?.either(dataCallback, errorCallback, completeCallback, loadingCallback)
        })
}

/**
 * General api to handle [ViewState] manually.
 * It is the case, when standard [ViewState.either] does not satisfy your needs.
 */
fun <DataType> LiveData<ViewState<DataType>>.observeViewState(
    owner: LifecycleOwner,
    stateCallback: (ViewState<DataType>) -> Unit
) {
    this.observe(owner, Observer { stateCallback(it) })
}