package org.unitymind.vk.sdk;

import android.util.Log;
import java.net.URLEncoder;

public class WebAuth {
    
    private static final String TAG = WebAuth.class.getName();
    public static String redirect_url = "http://oauth.vk.com/blank.html";
    
    public static String getUrl(String api_id, String settings){
        String url = "http://oauth.vk.com/authorize?client_id="+api_id+"&display=touch&scope="+settings+"&redirect_uri="+URLEncoder.encode(redirect_url)+"&response_type=token";
        return url;
    }
    
    public static String getSettings() {
        int settings = 1+2+4+8+16+32+64+128+1024+2048+4096+8192+65536+131072+262144;
        return Integer.toString(settings);
        //return "friends,photos,audio,video,docs,notes,pages,wall,groups,messages,offline";
    }
    
    public static String[] parseRedirectUrl(String url) throws Exception {
        //url is something like http://api.vkontakte.ru/blank.html#access_token=66e8f7a266af0dd477fcd3916366b17436e66af77ac352aeb270be99df7deeb&expires_in=0&user_id=7657164
        String access_token = Utils.extractPattern(url, "access_token=(.*?)&");
        Log.d(TAG, "access_token=" + access_token);
        String user_id=Utils.extractPattern(url, "user_id=(\\d*)");
        Log.d(TAG, "user_id=" + user_id);
        if(user_id==null || user_id.length() == 0 || access_token == null || access_token.length() == 0)
            throw new Exception("Failed to parse redirect url " + url);
        return new String[]{access_token, user_id};
    }
}