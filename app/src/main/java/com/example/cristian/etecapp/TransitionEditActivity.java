package com.example.cristian.etecapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TransitionEditActivity extends AppCompatActivity {
    private TextView priceTextView;
    private TextView nameTextView;
    private TextView descriptionTextView;
    private Spinner shops;
    private Intent intent;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_edit);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        priceTextView = (TextView) findViewById(R.id.price);
        nameTextView = (TextView) findViewById(R.id.name);
        descriptionTextView = (TextView) findViewById(R.id.description);
        shops = (Spinner) findViewById(R.id.shops);

        ImageButton add_cartButton = (ImageButton) findViewById(R.id.add_cart_button);

        intent = getIntent();
        String priceExtra = intent.getStringExtra(SampleMaterialActivity.EXTRA_PRICE);
        String nameExtra = intent.getStringExtra(SampleMaterialActivity.EXTRA_NAME);
        String descriptionExtra = intent.getStringExtra(SampleMaterialActivity.EXTRA_DESCRIPTION);
        ArrayList<String> shopsExtra = intent.getStringArrayListExtra(SampleMaterialActivity.EXTRA_SHOPS);
        int colorExtra = intent.getIntExtra(SampleMaterialActivity.EXTRA_COLOR, 0);

        //build the card
        final Card card = new Card();
        card.setId((long) SampleMaterialActivity.id);
        card.setName(nameExtra);
        card.setPrice(priceExtra);
        card.setDescription(descriptionExtra);
        card.setShopArray(shopsExtra);
        card.setColorResource(colorExtra);


        priceTextView.setText(priceExtra);
        nameTextView.setText(nameExtra);
        nameTextView.setBackgroundColor(colorExtra);
        descriptionTextView.setText(descriptionExtra);


        adapter = new ArrayAdapter<>(
                TransitionEditActivity.this,
                android.R.layout.simple_list_item_1,
                shopsExtra);
        shops.setAdapter(adapter);


        //Agregar a la lista del carrito
        add_cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SampleMaterialActivity.cardsSelected.add(card);
                Toast.makeText(getApplicationContext(),R.string.item_added,Toast.LENGTH_SHORT).show();
                SampleMaterialActivity.id++;
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

