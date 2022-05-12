package com.example.schoolie.manager;

import android.util.Log;

import com.example.schoolie.utilities.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.rxjava3.annotations.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class FcmNotificationBuilder {
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = "FcmNotificationBuilder";
    private static final String SERVER_API_KEY = AppConstants.API_KEY;
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String PROJECTID = "project_id";
    private static final String SENDER_ID = "160785647340";
    private static final String AUTHORIZATION = "Authorization";
    private static final String AUTH_KEY = "key=" + SERVER_API_KEY;
    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    // json related keys
    private static final String KEY_TO = "to";
    private static final String KEY_REG_IDS = "registration_ids";
    private static final String KEY_NOTIFICATION = "notification";
    private static final String KEY_BODY = "body";
    private static final String KEY_SOUND = "sound";
    private static final String KEY_DEFAULT = "default";
    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_TYPE = "LinkTableName";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TEXT = "text";
    private static final String KEY_DATA = "data";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USERIMAGE = "userImage";
    private static final String KEY_UID = "uid";
    private static final String KEY_FCM_TOKEN = "fcm_token";
    private static final String KEY_receiver_ID = "receiverId";

    private String mType;

    public String getmOS() {
        return mOS;
    }

    public void setmOS(String mOS) {
        this.mOS = mOS;
    }

    private String mOS;
    private String mTitle;

    public String getmReceiverId() {
        return mReceiverId;
    }

    public void setmReceiverId(String mReceiverId) {
        this.mReceiverId = mReceiverId;
    }

    private String mReceiverId;
    private String mMessage;
    private String mUsername;
    private String mUserImage;
    private String mUid;
    private String mFirebaseToken;
    private String mReceiverFirebaseToken;
    private ArrayList<String> mReceiverUsersFirebaseToken;

    public FcmNotificationBuilder() {

    }

    public static FcmNotificationBuilder initialize() {
        return new FcmNotificationBuilder();
    }

    public FcmNotificationBuilder type(String type) {
        mType = type;
        return this;
    }

    public FcmNotificationBuilder os(String OS) {
        mOS = OS;
        return this;
    }

    public FcmNotificationBuilder receiverId(String receiverID) {
        mReceiverId = receiverID;
        return this;
    }

    public FcmNotificationBuilder title(String title) {
        mTitle = title;
        return this;
    }

    public FcmNotificationBuilder message(String message) {
        mMessage = message;
        return this;
    }

    public FcmNotificationBuilder username(String username) {
        mUsername = username;
        return this;
    }

    public FcmNotificationBuilder userImage(String userImage) {
        mUserImage = userImage;
        return this;
    }

    public FcmNotificationBuilder uid(String uid) {
        mUid = uid;
        return this;
    }

    public FcmNotificationBuilder firebaseToken(String firebaseToken) {
        mFirebaseToken = firebaseToken;
        return this;
    }

    public FcmNotificationBuilder receiverFirebaseToken(String receiverFirebaseToken) {
        mReceiverFirebaseToken = receiverFirebaseToken;
        return this;
    }

    public FcmNotificationBuilder receiverUsersFirebaseToken(ArrayList<String> receiverUsersFirebaseToken) {
        mReceiverUsersFirebaseToken = receiverUsersFirebaseToken;
        return this;
    }

    public void send() {
        RequestBody requestBody = null;
        try {
            if (mType.equals("TYPE_CHAT")) {
                requestBody = RequestBody.create(MEDIA_TYPE_JSON, getValidJsonBody().toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                //.addHeader(PROJECTID, SENDER_ID)
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .addHeader(AUTHORIZATION, AUTH_KEY)
                .url(FCM_URL)
                .post(requestBody)
                .build();

        Call call = new OkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "onGetAllUsersFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.e(TAG, "onResponse: " + response.body().string());
            }
        });
    }

    private JSONObject getValidJsonBody() throws JSONException {
        JSONObject jsonObjectBody = new JSONObject();
        jsonObjectBody.put(KEY_TO, mReceiverFirebaseToken);
        jsonObjectBody.put(KEY_PRIORITY, "high");
//        if (mOS.equalsIgnoreCase("ios") || mOS.equalsIgnoreCase("web")) {
            JSONObject jsonObjectNotification = new JSONObject();
            jsonObjectNotification.put(KEY_BODY, mMessage);
            jsonObjectNotification.put(KEY_SOUND, KEY_DEFAULT);
            jsonObjectNotification.put(KEY_TITLE, mTitle);
            jsonObjectNotification.put(KEY_TYPE, mType);
            jsonObjectBody.put(KEY_NOTIFICATION, jsonObjectNotification);
//        }
        JSONObject jsonObjectData = new JSONObject();
        jsonObjectData.put(KEY_TYPE, mType);
        jsonObjectData.put(KEY_TITLE, mTitle);
        jsonObjectData.put(KEY_TEXT, mMessage);
        jsonObjectData.put(KEY_UID, mUid);
        jsonObjectData.put(KEY_FCM_TOKEN, mFirebaseToken);
        jsonObjectBody.put(KEY_DATA, jsonObjectData);
        Log.e("//*:", jsonObjectBody.toString());
        return jsonObjectBody;
    }

}
