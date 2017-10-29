package com.packtpub.apps.rxjava_essentials.apps;

public class AppInfo implements Comparable<Object> {

    long mLastUpdateTime;

    public String mName;

    String mIcon;


    public AppInfo(String name, String icon, long lastUpdateTime) {
        mName = name;
        mIcon = icon;
        mLastUpdateTime = lastUpdateTime;
    }

    @Override
    public int compareTo(Object another) {
        AppInfo f = (AppInfo) another;
        return getName().compareTo(f.getName());
    }

    public String getName() {
        return mName;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setIcon(String mIcon) {
        this.mIcon = mIcon;
    }

    public long getLastUpdateTime() {
        return mLastUpdateTime;
    }
}
