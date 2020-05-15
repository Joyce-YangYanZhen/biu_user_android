package com.noplugins.keepfit.userplatform.util.net.progress;

/**
 * @author nanchen
 * @fileName RetrofitRxDemoo
 * @packageName com.nanchen.retrofitrxdemoo
 * @date 2016/12/12  14:48
 */

public interface SubscriberOnNextListener<T> {
    void onNext(T t);


    void onError(String error);


}
