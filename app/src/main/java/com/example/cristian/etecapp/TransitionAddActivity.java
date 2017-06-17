package com.example.cristian.etecapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TransitionAddActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "AppCompatActivity";


    private RecyclerView recyclerView;
    private ImageButton checkOutButton;

    private CartAdapter adapter;
    private ArrayList<Card> cardsList = SampleMaterialActivity.cardsSelected;
    /*
    private int[] colors;
    private String[] names;
    private String[] prices;
    private String[] descriptions;
    private String[] shopsArray;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_add);

        /*
        names = getResources().getStringArray(R.array.names_array);
        colors = getResources().getIntArray(R.array.initial_colors);
        prices = getResources().getStringArray(R.array.prices_value);
        descriptions = getResources().getStringArray(R.array.descriptions_array);
        shopsArray = getResources().getStringArray(R.array.shops_Array);

        initCards();
        */

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

        checkOutButton = (ImageButton) findViewById(R.id.checkOut);
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

    /*private void initCards() {
        for (int i = 0; i < 9; i++) {
            Card card = new Card();
            card.setId((long) i);
            card.setName(names[i]);
            card.setPrice(prices[i]);
            card.setDescription(descriptions[i]);
            card.setShopArray(shopsArray[i]);
            card.setColorResource(colors[i]);
            Log.d(DEBUG_TAG, "Card created with id " + card.getId() + ", name " + card.getName() + ", color " + card.getColorResource());
            cardsList.add(card);
        }
    }*/
}

