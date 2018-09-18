package com.pandu.base.domain;

import android.support.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import polanski.option.Option;

/**
 * Interfaces for Interactors. This interfaces represent use cases
 * (this means any use case in the application should implement this contract).
 *
 * @implNote SendInteractor, DeleteInteractor and RequestInteractor, they all return a Single since the
 * nature of all this operations is to provide a result and complete.
 * <p>
 * The ReactiveInteractors can be combined and nested. It can be seen as different level of
 * interactors. The lowest level interactor is the one that accesses the repository directly.
 * A higher level interactor could use one or more of this lower level interactors and map the
 * results. The idea is that each interactor does one thing and one thing only, this way is like
 * having pieces that you can put together to build something bigger
 */
public interface ReactiveInteractor {

    /**
     * Sends changes to data layer.
     * It returns a {@link Single} that will emit the result of the send operation.
     *
     * @param <R> the type of the send operation result.
     * @param <P> required parameters for the send.
     */
    interface SendInteractor<P, R> extends ReactiveInteractor {

        @NonNull
        Single<R> getSingle(@NonNull final Option<P> params);
    }

    /**
     * Retrieves changes from the data layer.
     * It returns an {@link Flowable} that emits updates for the retrieved object.
     * The returned {@link Flowable} will never complete, but it can
     * error if there are any problems performing the required actions to serve the data.
     *
     * @param <O> the type of the retrieved object.
     * @param <P> required parameters for the retrieve operation.
     * @implNote RetrieveInteractor returns a {@link Flowable} designed to never complete. This one
     * can be seen as the domain version of the data layer’s Get operation, although there are some
     * important differences.
     * The data layer’s Get can be seen as a “pipe” to some form of storage. It doesn’t guarantee
     * there’s going to be an actual value in the store, in which case it will notify this state by
     * emitting NONE as we saw before. RetrieveInteractor is different since it has to guarantee the
     * value. Meaning that it has to do whatever is possible to get this value or error in case it
     * wasn’t possible. This usually means triggering a fetch if the {@link Flowable} from the data
     * layer’s Get emits NONE.
     */
    interface RetrieveInteractor<P, O> extends ReactiveInteractor {

        @NonNull
        Observable<O> getBehaviorStream(@NonNull final Option<P> params);
    }

    /**
     * The request interactor is used to request some result once. The returned observable is a
     * single, emits once and then completes or errors.
     *
     * @param <P> the type of the returned data.
     * @param <R> required parameters for the request.
     */
    interface RequestInteractor<P, R> extends ReactiveInteractor {

        @NonNull
        Single<R> getSingle(@NonNull final Option<P> params);
    }

    /**
     * The delete interactor is used to delete entities from data layer. The response for the delete
     * operation comes as onNext event in the returned observable.
     *
     * @param <R> the type of the delete response.
     * @param <P> required parameters for the delete.
     */
    interface DeleteInteractor<P, R> extends ReactiveInteractor {

        @NonNull
        Single<R> getSingle(@NonNull final Option<P> params);
    }

    /**
     * The refresh interactor is used to refresh the reactive store with new data. Typically calling
     * this interactor will trigger events in its get interactor counterpart. The returned observable
     * will complete when the refresh is finished or error if there was any problem in the process.
     *
     * @param <P> required parameters for the refresh.
     * @implNote This is in charge of performing the necessary actions to update the data layer.
     * This operation will cause the {@link Flowable} of the RetrieveInteractor to emit due to the
     * nature of the data layer.
     */
    interface RefreshInteractor<P> extends ReactiveInteractor {

        @NonNull
        Completable getRefreshSingle(@NonNull final Option<P> params);
    }
}
