package org.unitymind.vk.sdk.api;

import android.content.Intent;
import android.os.*;
import android.util.Log;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.unitymind.vk.sdk.Account;
import org.unitymind.vk.sdk.Constants;
import org.unitymind.vk.sdk.Utils;

import java.util.HashMap;
import java.util.Map;

public class Service extends android.app.Service {
    private static final String TAG = Service.class.getName();

    public static final String STATE = "org.unitymind.vk.sdk.api.SERVICE_STATE";

    private Account account;

    class ApiCallHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Bundle msgData = msg.getData();
            final Messenger replyTo = msg.replyTo;

            switch (msg.what) {
                case ServiceAction.SET_CREDENTIALS:
                    Map<String, Object> params = extractParams(msgData);

                    account.user_id = (String) params.get("user_id");
                    account.access_token = (String) params.get("access_token");
                    account.save();

                    Log.i(TAG, "Account credentials is saved!");
                    Log.d(TAG, account.toString());

                    broadcastState();

                    break;

                case ServiceAction.CLEAR_CREDENTIALS:
                    account.clear();
                    Log.w(TAG, "Account credentials is cleared!");
                    broadcastState();
                    break;

                case ServiceAction.GET_STATE:
                    Bundle payload = new Bundle();
                    payload.putBoolean("registered", account.access_token != null);
                    payload.putBoolean("hasNetwork", true);
                    Log.d(TAG, "Get state is called!");
                    sendServiceReply(replyTo, payload);
                    break;

                case ServiceAction.CLIENT_API_CALL:
                    RequestParams request_params = Utils.paramsFromJson(msgData.getString("params"));

                    request_params.put("client_id", Constants.CLIENT_ID);
                    request_params.put("client_secret", Constants.CLIENT_SECRET);

                    RestClient.get(msgData.getString("method"), request_params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String body) {
                            Log.d(TAG, "response=" + body);
                            sendApiReply(replyTo, true, body);
                        }

                        @Override
                        public void onFailure(Throwable ex, String body) {
                            Log.e(TAG, ex.getMessage());
                            sendApiReply(replyTo, false, body);
                        }
                    });

                    break;
                case ServiceAction.TOKEN_API_CALL:
                    break;
                default:
                    super.handleMessage(msg);
            }
        }

        private Map<String, Object> extractParams(Bundle mBundle) {
            return Utils.parseJson(mBundle.getString("params"));
        }

        private void sendApiReply(Messenger replyTo, boolean success, String body) {
            if (replyTo != null) {
                Message replyMsg = Message.obtain();
                Bundle replyData = new Bundle();

                replyData.putBoolean("success", success);
                replyData.putString("body", body);

                replyMsg.setData(replyData);

                try {
                    replyTo.send(replyMsg);
                } catch (RemoteException ex) {
                    Log.e(TAG, "ApiCallHandler error: " + ex.getMessage());
                }
            }
        }

        private void sendServiceReply(Messenger replyTo, Bundle payload) {
            if (replyTo != null) {
                Message replyMsg = Message.obtain();
                replyMsg.setData(payload);
                try {
                    replyTo.send(replyMsg);
                } catch (RemoteException ex) {
                    Log.e(TAG, "ServiceCallHandler error: " + ex.getMessage());
                }
            }
        }
    }

    private final Messenger messenger = new Messenger(new ApiCallHandler());

    @Override
    public void onCreate() {
        super.onCreate();
        account = new Account(Service.this);
        account.restore();
        broadcastState();
        Log.d(TAG, account.toString());
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                broadcastState();
//            }
//        }, 0, 60000);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    private void broadcastState() {
        Intent intent = new Intent(STATE);
        intent.putExtra("registered", account.access_token != null);
        intent.putExtra("hasNetwork", true);
        sendBroadcast(intent);
        Log.d(TAG, "Service state broadcasted!");
    }
}