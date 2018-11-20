package com.cretin.comm_util.net.callback;

/**
 * HttpURLConnection网络请求返回监听器
 */
public interface HttpCallbackBytesListener {
    // 网络请求成功
    void onFinish(byte[] response);

    // 网络请求失败
    void onError(Exception e);
}
