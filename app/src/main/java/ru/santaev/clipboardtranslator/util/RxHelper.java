package ru.santaev.clipboardtranslator.util;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class RxHelper {

    private static <T> Observable<T> getObservable(Callable<T> function) {
        return Observable.create(
                (subscriber -> {
                    T resp = function.call();
                    if (resp == null) {
                        subscriber.onError(new Exception("Error while call function " + function));
                    } else {
                        subscriber.onNext(resp);
                    }
                })
        );
    }

    public static <T> Disposable runOnIoThread(Callable<T> function,
                                               Consumer<Throwable> handler, Consumer<T> onNext,
                                               final Action onCompleted) {
        return getObservable(function)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> {
                            if (onNext != null) {
                                onNext.accept(t);
                            }
                        }, handler,
                        () -> {
                            if (onCompleted != null) {
                                onCompleted.run();
                            }
                        });
    }

}
