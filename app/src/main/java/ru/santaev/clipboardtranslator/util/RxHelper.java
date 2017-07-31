package ru.santaev.clipboardtranslator.util;

import android.support.annotation.Nullable;

import ru.santaev.clipboardtranslator.api.ApiService;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxHelper {

    public static <R, T> Subscription makeRequest(R request, Func1<R, T> function,
                                                  Action1<Throwable> handler, @Nullable Action1<T> onNext,
                                                  final Action0 onCompleted) {
        return getObservable(request, function)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> {
                            if (onNext != null) {
                                onNext.call(t);
                            }
                        }, handler,
                        () -> {
                            if (onCompleted != null) {
                                onCompleted.call();
                            }
                        });
    }

    public static <R, T> Observable<T> getObservable(R request, Func1<R, T> function) {
        return Observable.create(
                (subscriber -> {
                    T resp = function.call(request);
                    if (resp == null) {
                        subscriber.onError(new Exception("Error while call function " + function));
                    } else {
                        subscriber.onNext(resp);
                        subscriber.onCompleted();
                    }
                })
        );
    }

    private static <T> Observable<T> getObservable(Func0<T> function) {
        return Observable.create(
                (subscriber -> {
                    T resp = function.call();
                    if (resp == null) {
                        subscriber.onError(new Exception("Error while call function " + function));
                    } else {
                        subscriber.onNext(resp);
                        subscriber.onCompleted();
                    }
                })
        );
    }

    public static <T> Subscription make(Func0<T> function,
                                           Action1<Throwable> handler, @Nullable Action1<T> onNext,
                                           final Action0 onCompleted) {
        return getObservable(function)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> {
                            if (onNext != null) {
                                onNext.call(t);
                            }
                        }, handler,
                        () -> {
                            if (onCompleted != null) {
                                onCompleted.call();
                            }
                        });
    }

}
