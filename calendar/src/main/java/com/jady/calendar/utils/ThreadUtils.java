package com.jady.calendar.utils;


import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description: Created by jadyli on 2017/4/27.
 */
public class ThreadUtils {
    public static void startChildThread(final ThreadCallback callback) {
        Flowable.create(new FlowableOnSubscribe<Void>() {
            @Override
            public void subscribe(FlowableEmitter<Void> e) throws Exception {
                callback.runOperate();
                callback.onCompleted();
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onSubscribe(Subscription s) {

                    }

                    @Override
                    public void onNext(Void aVoid) {
                    }

                    @Override
                    public void onError(Throwable t) {
                        callback.onError(t);
                    }

                    @Override
                    public void onComplete() {
                        callback.onCompleted();
                    }
                });
    }

}
