package com.noteslist.app.common.arch

class ViewState<out T>(
    val status: Status,
    val data: T?
) {
    /**
     * Common case of consuming [ViewState].
     *
     * Callbacks behavior:
     * + [dataCallback] is called when [data] is not `null` and [status] is not [Status.Loading].
     * + [loadingCallback] is called with `true` on [Status.Loading] and with `false` on other statuses.
     * + [errorCallback] is called on [Status.Error].
     * + [completeCallback] is called on [Status.Complete]
     *
     * I.e. when [ViewState] has [Status.Error] and it's [data] is not `null`, then
     * [errorCallback] and [dataCallback] will be called.
     */
    fun either(
        dataCallback: ((T) -> Unit)? = null,
        errorCallback: ((Throwable) -> Unit)? = null,
        completeCallback: (() -> Unit)? = null,
        loadingCallback: ((Boolean) -> Unit)? = null
    ) {
        fun notifyData() {
            data?.let { dataCallback?.invoke(it) }
        }

        when (status) {
            Status.Success -> {
                loadingCallback?.invoke(false)
                notifyData()
            }
            is Status.Complete -> {
                loadingCallback?.invoke(false)
                completeCallback?.invoke()
            }
            Status.Loading -> {
                loadingCallback?.invoke(true)
            }
            is Status.Error -> {
                loadingCallback?.invoke(false)
                notifyData()
                errorCallback?.invoke(status.e)
            }
        }
    }

    companion object {
        fun <T> loading(data: T? = null): ViewState<T> =
            ViewState(Status.Loading, data)
        fun <T> success(data: T?): ViewState<T> =
            ViewState(Status.Success, data)
        fun <T> error(e: Throwable, data: T? = null): ViewState<T> =
            ViewState(Status.Error(e), data)
        fun <T> complete(data: T? = null): ViewState<T> =
            ViewState(Status.Complete, data)
    }
}