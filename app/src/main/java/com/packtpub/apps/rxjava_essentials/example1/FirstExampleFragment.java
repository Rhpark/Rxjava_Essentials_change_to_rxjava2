package com.packtpub.apps.rxjava_essentials.example1;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.packtpub.apps.rxjava_essentials.App;
import com.packtpub.apps.rxjava_essentials.R;
import com.packtpub.apps.rxjava_essentials.Utils;
import com.packtpub.apps.rxjava_essentials.apps.AppInfo;
import com.packtpub.apps.rxjava_essentials.apps.AppInfoRich;
import com.packtpub.apps.rxjava_essentials.apps.ApplicationAdapter;
import com.packtpub.apps.rxjava_essentials.apps.ApplicationsList;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;
import rx.Subscription;
import rx.subjects.TestSubject;

public class FirstExampleFragment extends Fragment {

    private final String TAG = FirstExampleFragment.class.getSimpleName();

    @InjectView(R.id.fragment_first_example_list)
    RecyclerView mRecyclerView;

    @InjectView(R.id.fragment_first_example_swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private ApplicationAdapter mAdapter;

    private File mFilesDir; //icon을 가져오기위한경로

    public FirstExampleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_example, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mAdapter = new ApplicationAdapter(new ArrayList<>(), R.layout.applications_list_item);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.myPrimaryColor));
        mSwipeRefreshLayout.setProgressViewOffset(false, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        // Progress
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setRefreshing(true);
        mRecyclerView.setVisibility(View.GONE);

        //RX 설정. File 경로 설정,
        getFileDir()
                .subscribeOn(Schedulers.io())//작업스레드 설정
                .observeOn(AndroidSchedulers.mainThread())//결과 스레드 설정
                .subscribe(new Observer<File>() {
                    @Override
                    public void onComplete() {
                        Log.d(TAG,"file onCompleted" );
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG,"file onError" );
                    }

                    @Override
                    public void onNext(File file) {
                        mFilesDir = file;
                        Log.d(TAG,"file onNext" );
                        refreshTheList();
                    }
                });
//                .subscribe(file -> {
//                    mFilesDir = file;
//                    if ( file == null)
//                        Log.d(TAG,"file null" );
//
//                    else {
//                        Log.d(TAG, "file " + file.getPath());
//                        Log.d(TAG, "file " + file.getName());
//                    }
//
//                    refreshTheList();
//                });
//

//        test();
//        fromTest();
//        publishTest();
//        behaviorTest();
//        replayTest();//궁금 1 버퍼에 저장 후 옵저버에게 재생 Error1
//        asyncSubject();//Error2
//        justTest();
//        rangeTest();
//        repeateTest();
//        intervalTest();
//        tackLast();
//        distinct();
//        distinctUntilChanged();
//        timeout();    //TT
//        tt();
//        first();
//        last();
//        sample();
//        Observable<Integer> dt = Observable.defer(this::deferTest); //::??
//        dt.subscribe(new Observer<Integer>() {
//            @Override
//            public void onCompleted() {
//                Log.d(TAG,"deferTest onCompleted");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d(TAG,"deferTest onError");
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                Log.d(TAG,"deferTest onNext " + integer);
//            }
//        });
//        timerTest();
    }

    private void test()
    {

        final Observable<Integer> t = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                //onNext를 여러번호출해도되고,onNext안에서 여러번 작업해도 같다.

                for( int i = 0; i < 5 ; i++)
                {
                    Log.d(TAG,"test onNext" + i );
//                    subscriber.onNext(i);
                }
                Log.d(TAG,"test onCompleted");
            }
        });
        t.subscribe(new Observer<Integer>() {
            @Override
            public void onComplete() {
                Log.d(TAG,"test onCompleted ==");
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,"test onError == " + e.getMessage().toString());
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG,"test onNext == " + integer);
                if ( integer == 4)
                    Log.d(TAG,"test onNext == " + Integer.parseInt("ddd"));
            }
        });
    }

    private void fromTest(){

        ArrayList<Integer> ary = new ArrayList<>();
        ary.add(1);
        ary.add(0);
        ary.add(2);
        ary.add(5);
        ary.add(3);
        Log.d(TAG,"fromTest from ");
        Observable<Integer> t = Observable.fromIterable(ary);

        t.subscribe(new Observer<Integer>() {
            @Override
            public void onComplete() {
                Log.d(TAG,"fromTest onCompleted ");
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,"fromTest onError ");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG,"fromTest onNext " + integer);
                if ( integer == 5)
                    Log.d(TAG,"test onNext == " + Integer.parseInt("ddd"));
            }
        });
    }

    private void justTest() {
        Observable<Integer> t = Observable.just(5,1,2);

        t.subscribe(new Observer<Integer>() {
            @Override
            public void onComplete() {
                Log.d(TAG,"fromTest onCompleted ");
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,"fromTest onError ");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG,"fromTest onNext " + integer);
                if ( integer == 2)
                    Log.d(TAG,"test onNext == " + Integer.parseInt("ddd"));
            }
        });
    }

    private void publishTest()
    {
        final PublishSubject<Integer> t = PublishSubject.create();//인자없음

        t.subscribe(new Observer<Integer>() {
            @Override
            public void onComplete() {
                Log.d(TAG,"publishTest onCompleted ");
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,"publishTest onError ");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG,"publishTest onNext " + integer);
//                if ( integer == 3)
//                    Log.d(TAG,"test onNext == " + Integer.parseInt("ddd"));
            }
        });

//        //인자
//        t.onNext(1);
//        t.onNext(2);
//        t.onNext(3);
//
//        t.onCompleted();
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG,"publishTest create onNext ");

                e.onNext(0);
                e.onNext(-1);
                e.onNext(-2);
                e.onNext(-3);

                e.onComplete();
            }
        }).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                Log.d(TAG,"publishTest create call ");

                t.onNext(1);
                t.onNext(2);
//                t.onNext(3);
                t.onNext(4);

                t.onComplete();
//                t.onNext(5);안됨
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onComplete() {
                Log.d(TAG,"publishTest subscribe onCompleted ");
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,"publishTest subscribe onError ");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG,"publishTest subscribe onNext " + integer);
//                if ( integer == -3)
//                    Log.d(TAG,"test onNext == " + Integer.parseInt("ddd"));
            }
        });//subscribe는 Observable 시작하도록 호출만되는함수

    }

    private void behaviorTest()
    {
        final BehaviorSubject<Integer> t = BehaviorSubject.createDefault(9);

        t.subscribe(new Observer<Integer>() {
            @Override
            public void onComplete() {
                Log.d(TAG,"behaviorTest onCompleted ");
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,"behaviorTest onError ");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG,"behaviorTest onNext " + integer);
//                if ( integer == 9)
//                    Log.d(TAG,"behaviorTest onNext == " + Integer.parseInt("ddd"));
            }
        });

        t.onNext(1);
        t.onNext(2);
        t.onNext(3);
        t.onComplete();
//        Observable.create(new Observable.OnSubscribe<Integer>() {
//            @Override
//            public void call(Subscriber<? super Integer> subscriber) {
//
//                Log.d(TAG,"behaviorTest create onNext ");
//
//                subscriber.onNext(0);
//                subscriber.onNext(-1);
//                subscriber.onNext(-2);
//                subscriber.onNext(-3);
//
//                subscriber.onCompleted();
//            }
//        }).doOnCompleted(new Action0() {
//            @Override
//            public void call() {
//
//                Log.d(TAG,"behaviorTest create call ");
//
//                t.onNext(1);
//                t.onNext(2);
////                t.onNext(3);
//                t.onNext(4);
//
//                t.onCompleted();
////                t.onNext(5);안됨
//            }
//        }).subscribe(new Observer<Integer>() {
//            @Override
//            public void onCompleted() {
//                Log.d(TAG,"behaviorTest subscribe onCompleted ");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d(TAG,"behaviorTest subscribe onError ");
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                Log.d(TAG,"behaviorTest subscribe onNext " + integer);
//            }
//        });//subscribe는 Observable 시작하도록 호출만되는함수
    }

    private void replayTest(){

        final ReplaySubject<Integer> t = ReplaySubject.create();

        t.subscribe(new Observer<Integer>() {
            @Override
            public void onComplete() {
                Log.d(TAG,"replayTest onCompleted ");
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,"replayTest onError ");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG,"replayTest onNext " + integer);
            }
        });
        t.onNext(1);
        t.onNext(2);
        t.onNext(3);
        t.onComplete();//Error
//        t.bu
//
//        Observable.create(new Observable.OnSubscribe<Integer>() {
//            @Override
//            public void call(Subscriber<? super Integer> subscriber) {
//
//                Log.d(TAG,"replayTest create onNext ");
//
//                subscriber.onNext(0);
//                subscriber.onNext(-1);
//                subscriber.onNext(-2);
//                subscriber.onNext(-3);
//
//                subscriber.onCompleted();
//            }
//        }).doOnCompleted(new Action0() {
//            @Override
//            public void call() {
//
//                Log.d(TAG,"replayTest create call ");
//
//                t.onNext(1);
//                t.onNext(2);
////                t.onNext(3);
//                t.onNext(4);
//                t.onCompleted();    //Error
////                t.onNext(5);안됨
//            }
//        }).subscribe(new Observer<Integer>() {
//            @Override
//            public void onCompleted() {
//                Log.d(TAG,"replayTest subscribe onCompleted ");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d(TAG,"replayTest subscribe onError ");
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                Log.d(TAG,"replayTest subscribe onNext " + integer);
//            }
//        });//subscribe는 Observable 시작하도록 호출만되는함수

    }

    private void asyncSubject(){

        final AsyncSubject<Integer> t = AsyncSubject.create();

        t.subscribe(new Observer<Integer>() {
            @Override
            public void onComplete() {
                Log.d(TAG,"AsyncSubject onCompleted ");
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,"AsyncSubject onError ");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG,"AsyncSubject onNext " + integer);
            }
        });

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG,"AsyncSubject create onNext ");

                e.onNext(0);
                e.onNext(-1);
                e.onNext(-2);
                e.onNext(-3);

                e.onComplete();
            }
        }).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                Log.d(TAG,"AsyncSubject create call ");

                t.onNext(1);
                t.onNext(2);
//                t.onNext(3);
                t.onNext(4);
                t.onComplete();    //Error
//                t.onNext(5);안됨
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onComplete() {
                Log.d(TAG,"AsyncSubject subscribe onCompleted ");
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,"AsyncSubject subscribe onError ");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG,"AsyncSubject subscribe onNext " + integer);
            }
        });

    }

    private Observable<Integer> deferTest(){
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {

                Log.d(TAG,"deferTest isUnsubscribed Not");
                e.onNext(11);
                e.onComplete();
            }
        });
    }

    private Observable<File> getFileDir() {

        return Observable.create(subscriber -> {
            Log.d(TAG,"getFileDir onNext =====");
//            File e = null;
//            subscriber.onNext(e);
            subscriber.onNext(App.instance.getFilesDir());//app이 설치된 Dir
            Log.d(TAG,"getFileDir onCompleted =====");
            subscriber.onComplete();
        });
    }

    private void refreshTheList() {

//        getApps().toSortedList().subscribe(new Consumer<List<AppInfo>>()
//        {
//               @Override
//               public void accept(List<AppInfo> appInfos) throws Exception {
//                   Log.d(TAG, "refreshTheList Consumer ===== ");
//
//                   mRecyclerView.setVisibility(View.VISIBLE);
//                   mAdapter.addApplications(appInfos);
//                   mSwipeRefreshLayout.setRefreshing(false);
//                   storeList(appInfos);
//               }
//         });

        getApps().subscribe(new Consumer<AppInfo>() {
            @Override
            public void accept(AppInfo appInfo) throws Exception {
                mRecyclerView.setVisibility(View.VISIBLE);
                mAdapter.addApplication(appInfo);
                mSwipeRefreshLayout.setRefreshing(false);
                storeList_T(appInfo);
            }
        });

        if (true)return;

        getApps()
                .toSortedList() //list로 항목을 모으고 정렬
                .subscribe(new SingleObserver<List<AppInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG,"refreshTheList onSubscribe =====");
                    }

                    @Override
                    public void onSuccess(List<AppInfo> value) {

                        Log.d(TAG,"refreshTheList onSuccess ===== " + value.size());
                        for( int i = 0 ; i < value.size() ; i ++)
                        {
                            Log.d(TAG,"refreshTheList onSuccess value " + i + " " + value.get(i));
                        }
//                        for (AppInfo a: appInfos)
//                            Log.d(TAG,"refreshTheList onNext " + a.toString());


                        mRecyclerView.setVisibility(View.VISIBLE);
                        mAdapter.addApplications(value);
                        mSwipeRefreshLayout.setRefreshing(false);
                        storeList(value);

                        Log.d(TAG,"refreshTheList onCompleted =====");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG,"refreshTheList onError =====");
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void repeateTest() {

        Observable.just(10,3,5)
                .repeat(3)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onComplete() {
                        Log.d(TAG,"repeateTest onCompleted =====");
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG,"repeateTest onError =====");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG,"repeateTest onNext ===== " + integer);
                    }
                });

    }

    private void rangeTest() {

        Log.d(TAG,"rangeTest onNext start-======");
        Observable
                .interval(1,TimeUnit.SECONDS)
                .range(10,3)
                .subscribe(new Observer<Integer>() {
                @Override
                public void onComplete() {
                    Log.d(TAG,"rangeTest onCompleted =====");
                }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                public void onError(Throwable e) {
                    Log.d(TAG,"rangeTest onError =====");
                }

                @Override
                public void onNext(Integer integer) {
                    Log.d(TAG,"rangeTest onNext ===== " + integer);
                }
            });

    }

    Subscription tt;
    Observer<Long> tl;
    private void intervalTest() {

        //특정 초 마다 호출된다.TimerTask..?!
        //멈추고 싶을땐??

        tl = new Observer<Long>() {
            @Override
            public void onComplete() {
                Log.d(TAG,"intervalTest onCompleted =====");
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,"intervalTest onError =====");
            }

            @Override
            public void onNext(Long aLong) {
                Log.d(TAG,"intervalTest onNext ===== " + aLong);
            }
        };
        boolean ts = false;
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(tl);

    }

    private void timerTest() {
//
//        tl.
//
//        Observable.timer(3,TimeUnit.SECONDS)
//                .repeat(2)
//            .subscribe(new Observer<Long>() {
//                @Override
//                public void onCompleted() {
//                    Log.d(TAG,"timerTest onCompleted =====");
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    Log.d(TAG,"timerTest onError =====");
//                }
//
//                @Override
//                public void onNext(Long integer) {
//                    Log.d(TAG,"timerTest onNext ===== " + integer);
//                }
//            });

    }

    private void storeList(List<AppInfo> appInfos) {
        Log.d(TAG,"storeList =====");
        ApplicationsList.getInstance().setList(appInfos);

        Schedulers.io().createWorker().schedule(() -> {
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            Type appInfoType = new TypeToken<List<AppInfo>>() {}.getType();
            sharedPref.edit().putString("APPS", new Gson().toJson(appInfos, appInfoType)).apply();
        });
    }

    private void storeList_T(AppInfo appInfos) {
        Log.d(TAG,"storeList =====");
        ApplicationsList.getInstance().addList(appInfos);

        Schedulers.io().createWorker().schedule(() -> {
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            Type appInfoType = new TypeToken<AppInfo>() {}.getType();
            sharedPref.edit().putString("APPS", new Gson().toJson(appInfos, appInfoType)).apply();
        });
    }

    private Observable<AppInfo> getApps() {
        return Observable.create(subscriber -> {
            List<AppInfoRich> apps = new ArrayList<>();

            final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

            //현재 단말에 설치된 실행 가능한 단말 apps 언어오기
            List<ResolveInfo> infos = getActivity().getPackageManager().queryIntentActivities(mainIntent, 0);

            for (ResolveInfo info : infos) {
                apps.add(new AppInfoRich(getActivity(), info));
//                Log.d(TAG,"getApps " + info.toString());
            }

            for (AppInfoRich appInfo : apps) {
                Bitmap icon = Utils.drawableToBitmap(appInfo.getIcon());
                String name = appInfo.getName();
                String iconPath = mFilesDir + "/" + name;
                Utils.storeBitmap(App.instance, icon, name);
//
//                if (subscriber.isUnsubscribed()) {
//                    return;
//                }
                Log.d(TAG,"getApps onNext =====");
                subscriber.onNext(new AppInfo(name, iconPath, appInfo.getLastUpdateTime()));
            }
//            if (!subscriber.isUnsubscribed()) {
//                Log.d(TAG,"getApps onCompleted =====");
//                subscriber.onCompleted();
//            }
        });
    }

    private void tackLast(){

        ArrayList<Integer> t = new ArrayList<>();

        t.add(1);
        t.add(3);
        t.add(4);
        t.add(6);
        t.add(7);
        t.add(8);
        t.add(0);

        Observable.fromIterable(t)
            .take(13)
            .subscribe(new Observer<Integer>() {
                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onCompleted tackLast ");
                    }

                           @Override
                           public void onSubscribe(Disposable d) {

                           }

                           @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError tackLast ");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext tackLast " + integer);
                    }
                    }
                );
    }

    private void distinct(){

        ArrayList<Integer> t = new ArrayList<>();
        t.add(1);
        t.add(3);
        t.add(1);
        t.add(2);
        t.add(1);
        t.add(5);

        Observable.fromIterable(t).distinct().subscribe(new Observer<Integer>() {
            @Override
            public void onComplete() {
                Log.d(TAG,"onCompleted distinct ");
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,"onError distinct ");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG,"onNext distinct " + integer);
            }
        });
    }

    private void distinctUntilChanged(){

        ArrayList<Integer> t = new ArrayList<>();
        t.add(1);
        t.add(1);
        t.add(2);
        t.add(1);
        t.add(5);

        Observable.fromIterable(t).distinctUntilChanged().subscribe(new Observer<Integer>() {
            @Override
            public void onComplete() {
                Log.d(TAG,"onCompleted distinctUntilChanged ");
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,"onError distinctUntilChanged " + e.getMessage());
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG,"onNext distinctUntilChanged " + integer);
            }
        });
    }

    //사용범..?'
    ArrayList<Integer> ttt = new ArrayList<>();
    private void timeout() {

        ttt.add(0);
        ttt.add(3);
        ttt.add(1);
        ttt.add(2);
        ttt.add(1);
        ttt.add(5);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                ttt.add(8);
                ttt.add(8);
                ttt.add(9);
            }
        }).start();
        Log.d(TAG,"timeout start");
        Observable.fromIterable(ttt).timeout(1,TimeUnit.SECONDS)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onComplete() {
                        Log.d(TAG,"onCompleted timeout ");
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG,"onError timeout ");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG,"onNext timeout " + integer);
                    }
                });
    }

    private void tt()
    {
        final PublishSubject<Integer> t = PublishSubject.create();

        t.timeout(2,TimeUnit.SECONDS).subscribe(new Observer<Integer>() {
            @Override
            public void onComplete() {
                Log.d(TAG,"AsyncSubject onCompleted ");
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,"AsyncSubject onError ");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG,"AsyncSubject onNext " + integer);
            }
        });

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(0);
                e.onNext(-1);
                e.onNext(-2);
                e.onNext(-3);

                e.onComplete();
            }

        }).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                Log.d(TAG,"AsyncSubject create call ");

                t.onNext(1);
                t.onNext(2);
//                t.onNext(3);
                t.onNext(4);
                t.onComplete();    //Error
//                t.onNext(5);안됨
            }
        }).timeout(2,TimeUnit.SECONDS).subscribe(new Observer<Integer>() {
            @Override
            public void onComplete() {
                Log.d(TAG,"AsyncSubject subscribe onCompleted ");
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,"AsyncSubject subscribe onError ");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG,"AsyncSubject subscribe onNext " + integer);
            }
        });
    }

    private void first()
    {
        ArrayList<Integer> t = new ArrayList<>();
        t.add(0);
        t.add(3);
        t.add(1);
        t.add(2);
        t.add(1);
        t.add(5);
        Log.d(TAG," " + Observable.fromIterable(t).first(1) );
//                .subscribe(new Observable<>())
//
//
//                        subscribe(new Observer<Integer>()
//                {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Integer value) {
//                        Log.d(TAG,"onNext first " + value);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.d(TAG,"onError first ");
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d(TAG,"onCompleted first ");
//                    }
//                });
    }
    private void last()
    {
        ArrayList<Integer> t = new ArrayList<>();
        t.add(0);
        t.add(3);
        t.add(1);
        t.add(2);
        t.add(1);
        t.add(5);

        Observable.fromIterable(t).takeLast(0)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onComplete() {
                        Log.d(TAG,"onCompleted last ");
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG,"onError last ");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG,"onNext last " + integer);
                    }
                });
    }
    ArrayList<Integer> t = new ArrayList<>();
    private void sample()
    {

        t.add(0);
        t.add(3);
        t.add(1);
        t.add(2);
        t.add(1);
        t.add(5);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                t.add(8);
                t.add(8);
                t.add(9);
            }
        }).start();
        Log.d(TAG,"sample start ");

        Observable.fromIterable(t).sample(1,TimeUnit.SECONDS)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onComplete() {
                        Log.d(TAG,"onCompleted sample ");
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG,"onError sample ");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG,"onNext sample " + integer);
                    }
                });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
