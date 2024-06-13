package com.javatechie.api;
import com.javatechie.util.constain.ConstVariable;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class ApiCheckConnect {
    private static final String username = ConstVariable.KEY_BROKER;
    private static final String password = ConstVariable.SECRET_KEY_BROKER;

    public static Boolean checkExistClient(String clientId) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://localhost:18083/api/v5/clients/" + clientId)
                    .header("Content-Type", "application/json")
                    .header("Authorization", Credentials.basic(username, password))
                    .build();
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(responseBody);
            try {
                Object data = jsonObject.get("connected");
                if (data.equals(true)) {
                    return true;
                }
            } catch (Exception e) {
                Object messageCode = jsonObject.get("code");
                if (messageCode.equals("CLIENTID_NOT_FOUND")) {
                    return false;
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}