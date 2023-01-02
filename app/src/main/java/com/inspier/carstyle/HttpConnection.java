package com.inspier.carstyle;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

//Http 통신하는 Class
public class HttpConnection {

    private OkHttpClient client;
    private static HttpConnection instance = new HttpConnection();
    public static HttpConnection getInstance() {
        return instance;
    }

    private HttpConnection() {
        this.client = new OkHttpClient();
    }

    //웹 서버로 요청하는 function
    public void requestWebServer(String token, String id, Callback callback) {
        RequestBody body = new FormBody.Builder()
                .add("Token", token)
                .add("ID", id)
                .build();

        Request request = new Request.Builder()
                .url("http://pdbx12.cafe24.com:80/register.php")
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }
}
