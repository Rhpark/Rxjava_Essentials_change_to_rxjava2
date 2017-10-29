package com.packtpub.apps.rxjava_essentials.apps;

import java.util.ArrayList;
import java.util.List;


public class ApplicationsList {

    private static ApplicationsList ourInstance = new ApplicationsList();

    private List<AppInfo> mList;

    private ApplicationsList() {
    }

    public static ApplicationsList getInstance() {
        return ourInstance;
    }

    public void setList(List<AppInfo> mList) {
        this.mList = mList;
    }

    public List<AppInfo> getList() {
        return mList;
    }

    public void addList(AppInfo list){
        if ( mList == null)
            mList = new ArrayList<>();
        mList.add(list);
    }
}
