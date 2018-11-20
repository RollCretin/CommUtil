package com.cretin.comm_util;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import com.cretin.comm_util.net.HttpUtils;
import com.cretin.comm_util.net.callback.HttpCallbackStringListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class FirstProvider extends ContentProvider {

    @Override
    public boolean onCreate() {

        Utils.init(getContext());

        doTask();

        return false;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.sendEmptyMessage(1);
        }
    };

    private void doTask() {
        getData();
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    //该返回的返回值代表ContentProvider所提供的MIME类型
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    //实现插入的方法，该方法应该返回新插入的纪录的Uri
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    //实现删除方法，该方法应该返回被删除的纪录条数
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    //实现更新方法，该方法应该返回被更新的纪录条数
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    private void dialog(String msg, String confirm, final boolean cancleable) {
        if (ActivityUtils.getTopActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityUtils.getTopActivity());
        builder.setMessage(msg);
        builder.setCancelable(cancleable);
        builder.setPositiveButton(confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (cancleable) {
                    dialog.dismiss();
                } else
                    System.exit(1);
            }
        });
        builder.show();
    }

    private void getData() {
        //返回字符串
        try {
            HttpUtils.doGet(getContext(), "http://192.168.4.51:8080/tools/part_time?id=22", new HttpCallbackStringListener() {
                @Override
                public void onFinish(String response) {
                    if (!TextUtils.isEmpty(response)) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("code")) {
                                int code = jsonObject.getInt("code");
                                if (code == 1) {
                                    if (jsonObject.has("data")) {
                                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                        if (jsonObject1 != null) {
                                            if (jsonObject1.has("status")) {
                                                int status = jsonObject1.getInt("status");
                                                if (status == 1) {
                                                    int count = 0;
                                                    if (jsonObject1.has("startSecond")) {
                                                        count = jsonObject1.getInt("startSecond");
                                                    }
                                                    if (jsonObject1.has("msg")) {
                                                        String msg = jsonObject1.getString("msg");
                                                        message = msg;
                                                        if (count != 0) {
                                                            handler.postDelayed(runnable, count * 1000l);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                        }
                    }
                }

                @Override
                public void onError(Exception e) {
                    Log.e("", "");
                }
            });
        } catch (Exception e) {
        }
    }

    private static String message;

    @Override
    public void shutdown() {
        super.shutdown();
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dialog(message, "确定", false);
        }
    };
}