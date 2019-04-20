package com.juno.avoidance.utils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Juno.
 * Date : 2019/4/20.
 * Time : 21:08.
 * When I met you in the summer.
 * Description :
 */
public class RxUtil {

    public abstract static class SimpleObserver<T> implements Observer<T> {

        private Disposable disposable;

        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @Override
        public void onError(Throwable e) {
            close();
        }

        @Override
        public void onComplete() {
            close();
        }

        private void close() {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
                disposable = null;
            }
        }
    }

}
