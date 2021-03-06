package com.packtpub.apps.rxjava_essentials.chapter8.api.stackexchange;

import com.packtpub.apps.rxjava_essentials.chapter8.api.stackexchange.models.User;
import com.packtpub.apps.rxjava_essentials.chapter8.api.stackexchange.models.UsersResponse;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import lombok.experimental.Accessors;
import retrofit.RestAdapter;
import io.reactivex.Observable;

@Accessors(prefix = "m")
public class SeApiManager {

    private final StackExchangeService mStackExchangeService;

    public SeApiManager() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.stackexchange.com")
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .build();

        mStackExchangeService = restAdapter.create(StackExchangeService.class);
    }

    public Observable<List<User>> getTenMostPopularSOusers() {
        return mStackExchangeService
                .getTenMostPopularSOusers()
                .map(UsersResponse::getUsers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<User>> getMostPopularSOusers(int howmany) {
        return mStackExchangeService
                .getMostPopularSOusers(howmany)
                .map(UsersResponse::getUsers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
