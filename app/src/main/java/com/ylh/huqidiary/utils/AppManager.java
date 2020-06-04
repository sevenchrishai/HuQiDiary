package com.ylh.huqidiary.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * Created by Haoz on 2017/3/12 0012.
 */

public class AppManager {
    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager(){}

    public static AppManager getAppManager(){
        if(instance == null){
            synchronized (AppManager.class){
                if(instance == null){
                    instance = new AppManager();
                }
            }
        }
        return instance;
    }
    
    public void addActivity(Activity activity){
        if(activityStack == null){
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获得当前Activity(栈顶Activity)
     * 
     * @return
     */
    public Activity currentActivity(){
        
        if(activityStack == null || activityStack.isEmpty()){
            return null;
        }
        Activity acitivity = activityStack.lastElement();
        return acitivity;
    }
    
    public Activity findActivity(Class<?> cls){
        Activity activity = null;
        for (Activity Activity : activityStack) {
            if(Activity.getClass().equals(cls)){
                activity = Activity;
                break;
            }
        }
        return activity;
    }
    
    public void finishActivity(){
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }
    
    public void finishActivity(Activity activity){
        if(activity != null){
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }
    
    public void finishActivity(Class<?> cls){
        for (Activity activity : activityStack) {
            if(activity.getClass().equals(cls)){
                finishActivity(activity);
            }
        }
    }
    
    public void finishAllActivity(){
            for (int i = 0, size = activityStack.size(); i < size; i++) {
                if (null != activityStack.get(i)) {
                    activityStack.get(i).finish();
                }
            }
            activityStack.clear();
    }

    public void AppExit(Context context){
        try{
            finishAllActivity();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}




















