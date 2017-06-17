package com.example.cristian.etecapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.ArrayList;

import static com.example.cristian.etecapp.SampleMaterialActivity.TRANSITION_FAB;

public class TransitionAddActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "AppCompatActivity";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();

    private RecyclerView recyclerView;
    private ImageButton imageButton;

    private CartAdapter adapter;
    private ArrayList<Card> cardsList = SampleMaterialActivity.cardsSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_add);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (adapter == null) {
            if (cardsList.isEmpty()) {
                adapter = new CartAdapter(this, new ArrayList<Card>() );
            }else{
                adapter = new CartAdapter(this, cardsList);
            }
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imageButton = (ImageButton)findViewById(R.id.checkOut);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardsList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.empty_list, Toast.LENGTH_SHORT).show();
                } else {
                    Pair<View, String> pair = Pair.create(v.findViewById(R.id.checkOut), TRANSITION_FAB);

                    ActivityOptionsCompat options;
                    Activity act = TransitionAddActivity.this;
                    options = ActivityOptionsCompat.makeSceneTransitionAnimation(act, pair);

                    Intent transitionIntent = new Intent(act, PagosActivity.class);
                    act.startActivityForResult(transitionIntent, adapter.getItemCount(), options.toBundle());
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    private String bowlingJson(String player1, String player2) {
        return "{'winCondition':'HIGH_SCORE',"
                + "'name':'Bowling',"
                + "'round':4,"
                + "'lastSaved':1367702411696,"
                + "'dateStarted':1367702378785,"
                + "'players':["
                + "{'name':'" + player1 + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
                + "{'name':'" + player2 + "','history':[6,10,5,10,10],'color':-48060,'total':41}"
                + "]}";
    }
}

