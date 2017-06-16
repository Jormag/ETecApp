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
import android.widget.TextView;
import android.widget.Toast;

public class TransitionEditActivity extends AppCompatActivity {
    private TextView priceTextView;
    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView shopsTextView;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_edit);

        priceTextView = (TextView) findViewById(R.id.price);
        nameTextView = (TextView) findViewById(R.id.name);
        descriptionTextView = (TextView) findViewById(R.id.description);
        shopsTextView = (TextView) findViewById(R.id.shops);

        Button update_button = (Button) findViewById(R.id.update_button);
        Button delete_button = (Button) findViewById(R.id.delete_button);

        intent = getIntent();
        String priceExtra = intent.getStringExtra(SampleMaterialActivity.EXTRA_PRICE);
        String nameExtra = intent.getStringExtra(SampleMaterialActivity.EXTRA_NAME);
        String descriptionExtra = intent.getStringExtra(SampleMaterialActivity.EXTRA_DESCRIPTION);
        String shopsExtra = intent.getStringExtra(SampleMaterialActivity.EXTRA_SHOPS);
        int colorExtra = intent.getIntExtra(SampleMaterialActivity.EXTRA_COLOR, 0);

        priceTextView.setText(priceExtra);
        nameTextView.setText(nameExtra);
        nameTextView.setBackgroundColor(colorExtra);
        descriptionTextView.setText(descriptionExtra);
        shopsTextView.setText(shopsExtra);

        priceTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    // update nameTextView
                    nameTextView.setText("");
                } else if (s.length() >= 1) {
                    // nameTextView set to first letter of priceTextView and update name stringExtra
                    nameTextView.setText(String.valueOf(s.charAt(0)));
                    intent.putExtra(SampleMaterialActivity.EXTRA_UPDATE, true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // must not be zero otherwise do not finish activity and report Toast message
                String text = nameTextView.getText().toString().trim();
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(getApplicationContext(), "Enter a valid name", Toast.LENGTH_SHORT).show();
                } else {
                    intent.putExtra(SampleMaterialActivity.EXTRA_UPDATE, true);
                    intent.putExtra(SampleMaterialActivity.EXTRA_NAME, String.valueOf(priceTextView.getText()));
                    intent.putExtra(SampleMaterialActivity.EXTRA_PRICE, String.valueOf(priceTextView.getText().charAt(0)));
                    setResult(RESULT_OK, intent);
                    supportFinishAfterTransition();
                }
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(SampleMaterialActivity.EXTRA_DELETE, true);
                setResult(RESULT_OK, intent);
                supportFinishAfterTransition();
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

