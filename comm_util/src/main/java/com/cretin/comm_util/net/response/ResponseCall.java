package com.cretin.comm_util.net.response;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.cretin.comm_util.net.callback.HttpCallbackBytesListener;
import com.cretin.comm_util.net.callback.HttpCallbackModelListener;
import com.cretin.comm_util.net.callback.HttpCallbackStringListener;

/**
 * Created by cretin on 2017/3/20.
 */

public class ResponseCall<T> {
    //用于在子线程和主线程的数据交换
    Handler mHandler;

    public ResponseCall(Context context, final HttpCallbackModelListener listener) {
        Looper looper = context.getMainLooper();
        mHandler = new Handler(looper) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if ( msg.what == 0 ) {
                    //成功
                    listener.onFinish(msg.obj);
                } else if ( msg.what == 1 ) {
                    //失败
                    listener.onError(( Exception ) msg.obj);
                }
            }
        };
    }

    public ResponseCall(Context context, final HttpCallbackBytesListener listener) {
        Looper looper = context.getMainLooper();
        mHandler = new Handler(looper) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if ( msg.what == 0 ) {
                    //成功
                    listener.onFinish(( byte[] ) msg.obj);
                } else if ( msg.what == 1 ) {
                    //失败
                    listener.onError(( Exception ) msg.obj);
                }
            }
        };
    }

    public ResponseCall(Context context, final HttpCallbackStringListener listener) {
        Looper looper = context.getMainLooper();
        mHandler = new Handler(looper) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if ( msg.what == 0 ) {
                    //成功
                    listener.onFinish(msg.obj.toString());
                } else if ( msg.what == 1 ) {
                    //失败
                    listener.onError(( Exception ) msg.obj);
                }
            }
        };
    }

    public void doSuccess(T response) {
        Message message = Message.obtain();
        message.obj = response;
        message.what = 0;
        mHandler.sendMessage(message);
    }

    public void doFail(Exception e) {
        Message message = Message.obtain();
        message.obj = e;
        message.what = 1;
        mHandler.sendMessage(message);
    }
}
