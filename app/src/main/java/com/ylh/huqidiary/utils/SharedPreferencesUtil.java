package com.ylh.huqidiary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.*;

public class SharedPreferencesUtil {

    private static SharedPreferences getSharePreferences(Context context, String spName){
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    public void setVal(Context context, String spName, String key, String value){
        getSharePreferences(context,spName).edit().putString(key, value).apply();
    }

    public String getVal(Context context,String spName, String key){
        return getSharePreferences(context, spName).getString(key, "");
    }

    /**
     * 存储图片到SharedPreferences
     * @param spName
     * @param key
     * @param bitmap
     */
    public static void saveBitmapToSp(Context context, String spName, String key, Bitmap bitmap){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,bos);
        String base64 = new String(Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT));
        getSharePreferences(context,spName).edit().putString(key, base64).apply();
    }

    /**
     * 获取图片从SharedPreferences
     * @param spName
     * @param key
     * @return
     */
    public static Bitmap getBitmapFromSp(Context context,String spName, String key){
        SharedPreferences sharedPreferences = getSharePreferences(context,spName);
        String imageString=sharedPreferences.getString(key, "");
        byte[] byteArray= Base64.decode(imageString, Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(byteArray);
        Bitmap bitmap= BitmapFactory.decodeStream(byteArrayInputStream);
        return bitmap;
    }

    /**
     * 存储对象到SharedPreferences
     * @param spName
     * @param key
     * @param object
     * @throws Exception
     */
    public static void saveObjToSp(Context context,String spName,String key,Object object) throws Exception {
        if(object instanceof Serializable) {
            SharedPreferences sharedPreferences = getSharePreferences(context,spName);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(object);//把对象写到流里
                String temp = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
                editor.putString(key, temp);
                editor.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            throw new Exception("Object must implements Serializable");
        }
    }

    /**
     * 读取序列化的对象从SharedPreferences
     * @param spName
     * @param key
     * @return
     */
    public static Object getObjFromSp(Context context, String spName,String key) {
        SharedPreferences sharedPreferences = getSharePreferences(context,spName);
        String temp = sharedPreferences.getString(key, "");
        ByteArrayInputStream bais =  new ByteArrayInputStream(Base64.decode(temp.getBytes(), Base64.DEFAULT));
        Object obj = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            obj = (Object) ois.readObject();
        } catch (IOException e) {
        }catch(ClassNotFoundException e1) {

        }
        return obj;
    }

}

