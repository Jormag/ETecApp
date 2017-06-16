package com.example.cristian.etecapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class TransitionAddActivity extends AppCompatActivity {
    private TextView priceTextView;
    private TextView nameTextView;
    private int color;
    private Intent intent;
    private Random randomGenerator = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_add);

        priceTextView = (TextView) findViewById(R.id.price);
        nameTextView = (TextView) findViewById(R.id.name);
        Button addButton = (Button) findViewById(R.id.add_button);

        intent = getIntent();
        int[] colors = getResources().getIntArray(R.array.initial_colors);
        color = colors[randomGenerator.nextInt(50)];

        nameTextView.setText("");
        nameTextView.setBackgroundColor(color);

        priceTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    // add nameTextView
                    nameTextView.setText("");
                } else if (count == 1) {
                    // nameTextView set to first letter of priceTextView and add name stringExtra
                    nameTextView.setText(String.valueOf(s.charAt(0)));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // must not be zero otherwise do not finish activity and report Toast message
                String text = nameTextView.getText().toString().trim();
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(getApplicationContext(), "Enter a valid name", Toast.LENGTH_SHORT).show();
                } else {
                    intent.putExtra(SampleMaterialActivity.EXTRA_NAME, String.valueOf(priceTextView.getText()));
                    intent.putExtra(SampleMaterialActivity.EXTRA_PRICE, String.valueOf(priceTextView.getText().charAt(0)));
                    intent.putExtra(SampleMaterialActivity.EXTRA_COLOR, color);
                    setResult(RESULT_OK, intent);
                    supportFinishAfterTransition();
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

