package com.example.cristian.etecapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private TextView name;
    private TextView email;
    private TextView gender;
    public ProfilePictureView profilePictureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        name = (TextView)findViewById(R.id.price);
        gender = (TextView)findViewById(R.id.gender);
        email = (TextView)findViewById(R.id.email);
        profilePictureView = (ProfilePictureView) findViewById(R.id.image);

        if (AccessToken.getCurrentAccessToken() == null) {
            goFacebookLogin();
        } else {
            requestUserProfile(AccessToken.getCurrentAccessToken());
        }
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
        Intent intent  = new Intent(this, SampleMaterialActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
