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

import java.util.ArrayList;

import static com.example.cristian.etecapp.SampleMaterialActivity.TRANSITION_FAB;

public class TransitionAddActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "AppCompatActivity";


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

}

