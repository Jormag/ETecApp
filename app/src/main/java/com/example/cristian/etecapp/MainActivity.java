package com.example.cristian.etecapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String URL_REGISTRO = "http://192.168.0.24:9080/eTECServer/clientes";
    private TextView name;
    private TextView email;
    private TextView gender;
    public ProfilePictureView profilePictureView;
    private boolean register = true;
    private ArrayAdapter <String> adapter;
    private Spinner spinner;

    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private static String url = "http://192.168.0.24:9080/eTECServer/prueba/centros";
    public ArrayList<String> arrayList;

    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        name = (TextView)findViewById(R.id.price);
        gender = (TextView)findViewById(R.id.gender);
        email = (TextView)findViewById(R.id.email);
        profilePictureView = (ProfilePictureView) findViewById(R.id.image);
        spinner = (Spinner) findViewById(R.id.spinnerCenter);

        if (AccessToken.getCurrentAccessToken() == null) {
            goFacebookLogin();
        } else {
            requestUserProfile(AccessToken.getCurrentAccessToken());
        }

        new GetProducts().execute();

    }


    private void requestUserProfile(AccessToken accessToken){
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("Main", response.toString());
                        setProfileToView(object);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void Logout(View view) {
        LoginManager.getInstance().logOut();
        goFacebookLogin();
    }

    private void goFacebookLogin() {
        Intent intent  = new Intent(this, FacebookLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void setProfileToView(JSONObject jsonObject) {
        String genero;
        try {
            email.setText(jsonObject.getString("email"));
            genero = jsonObject.getString("gender");
            if (genero.equals("male")){
                gender.setText("Masculino");
            }else if (genero.equals("female")){
                gender.setText("Femenino");
            }
            name.setText(jsonObject.getString("name"));
            profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
            profilePictureView.setProfileId(jsonObject.getString("id"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void goMainPage(View view) {
        try {
            register();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent  = new Intent(this, SampleMaterialActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //json

    private class GetProducts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < jsonObj.getJSONArray("centros").length();i++){
                        arrayList.add(jsonObj.getJSONArray("centros").get(i));
                    }

                    MainActivity.this.arrayList = arrayList;


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */


            adapter = new ArrayAdapter<>(
                    MainActivity.this,
                    android.R.layout.simple_list_item_1,
                    arrayList);
            spinner.setAdapter(adapter);

        }

    }


    public class Feedback extends AsyncTask<String, Void,String>{

        @Override
        protected String doInBackground(String... params) {
            try {
                OkHttpClient client = new OkHttpClient();

                RequestBody body = RequestBody.create(JSON,json());

                Request request = new Request.Builder()
                        .url(URL_REGISTRO)
                        .post(body)
                        .build();
                Response response =  client.newCall(request).execute();
                String result = response.body().string();
                return result;
            }catch (Exception e){
                return  null;
            }
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

        }
    }

    private String json() {
        String x = "{\"nombre\":" +  "\""+name.getText()+"\""+ ",\"correo\": " + "\""+ email.getText()+ "\"" + ",\"ctroDistribucion\":" + "\""+spinner.getSelectedItem() +"\"" + "}";
        return x;
    }

    private void register() throws IOException {
        new Feedback().execute();
    }


}
