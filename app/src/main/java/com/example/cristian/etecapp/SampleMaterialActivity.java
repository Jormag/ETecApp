package com.example.cristian.etecapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SampleMaterialActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "AppCompatActivity";

    public static final String EXTRA_UPDATE = "update";
    public static final String EXTRA_DELETE = "delete";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_PRICE = "price";
    public static final String EXTRA_DESCRIPTION = "description";
    public static final String EXTRA_SHOPS = "shopsArray";
    public static final String EXTRA_COLOR = "color";

    public static final String TRANSITION_FAB = "fab_transition";
    public static final String TRANSITION_INITIAL = "initial_transition";
    public static final String TRANSITION_NAME = "name_transition";
    public static final String TRANSITION_DELETE_BUTTON = "delete_button_transition";

    public static int id = 0;
    private int products;

    private RecyclerView recyclerView;
    private SampleMaterialAdapter adapter;
    private ArrayList<Card> cardsList = new ArrayList<>();
    public static ArrayList<Card> cardsSelected = new ArrayList<>();
    private int[] colors;

    private String TAG = SampleMaterialActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private static String url = "http://192.168.0.24:9080/eTECServer/prueba/productos";
    public ArrayList<String> names = new ArrayList<>();
    public ArrayList<String> prices = new ArrayList<>();
    public ArrayList<String> descriptions = new ArrayList<>();
    public ArrayList<ArrayList<String>> shopsArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_material);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        new GetProducts().execute();
        colors = getResources().getIntArray(R.array.initial_colors);

        if (adapter == null) {
            adapter = new SampleMaterialAdapter(this, cardsList);
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair<View, String> pair = Pair.create(v.findViewById(R.id.fab), TRANSITION_FAB);

                ActivityOptionsCompat options;
                Activity act = SampleMaterialActivity.this;
                options = ActivityOptionsCompat.makeSceneTransitionAnimation(act, pair);

                Intent transitionIntent = new Intent(act, TransitionAddActivity.class);
                act.startActivityForResult(transitionIntent, adapter.getItemCount(), options.toBundle());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(DEBUG_TAG, "requestCode is " + requestCode);
        // if adapter.getItemCount() is request code, that means we are adding a new position
        // anything less than adapter.getItemCount() means we are editing a particular position
        if (requestCode == adapter.getItemCount()) {
            if (resultCode == RESULT_OK) {
                // Make sure the Add request was successful
                // if add name, insert name in list
                String name = data.getStringExtra(EXTRA_NAME);
                String price = data.getStringExtra(EXTRA_PRICE);
                String description = data.getStringExtra(EXTRA_DESCRIPTION);
                ArrayList<String> shopsArray = data.getStringArrayListExtra(EXTRA_SHOPS);
                int color = data.getIntExtra(EXTRA_COLOR, 0);
                adapter.addCard(name,price,description,shopsArray, color);
            }
        } else {
            // Anything other than adapter.getItemCount() means editing a particular list item
            // the requestCode is the list item position
            if (resultCode == RESULT_OK) {
                // Make sure the Update request was successful
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(requestCode);
                if (data.getExtras().getBoolean(EXTRA_DELETE, false)) {
                    // if delete user delete
                    // The user deleted a contact
                    adapter.deleteCard(viewHolder.itemView, requestCode);
                } else if (data.getExtras().getBoolean(EXTRA_UPDATE)) {
                    // if name changed, update user
                    String name = data.getStringExtra(EXTRA_NAME);

                    //Falta en el update
                    String price = data.getStringExtra(EXTRA_PRICE);
                    viewHolder.itemView.setVisibility(View.INVISIBLE);
                    adapter.updateCard(name, requestCode);
                }
            }
        }
    }

    public void doSmoothScroll(int position) {
        recyclerView.smoothScrollToPosition(position);
    }

    private void initCards() {
        for (int i = 0; i < products; i++) {
            Card card = new Card();
            card.setId((long) i);
            card.setName(names.get(i));
            card.setPrice(prices.get(i));
            card.setDescription(descriptions.get(i));
            card.setShopArray(shopsArray.get(i));
            card.setColorResource(colors[i]);
            Log.d(DEBUG_TAG, "Card created with id " + card.getId() + ", name " + card.getName() + ", color " + card.getColorResource());
            cardsList.add(card);
        }
    }

    private class GetProducts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SampleMaterialActivity.this);
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

                    ArrayList nombres = new ArrayList();
                    ArrayList precios = new ArrayList();
                    ArrayList descripciones = new ArrayList();
                    ArrayList locales = new ArrayList();



                    for (int i = 0; i < jsonObj.getJSONArray("productos").length();i++){
                        nombres.add(jsonObj.getJSONArray("productos").getJSONObject(i).get("nombre").toString());
                        precios.add(jsonObj.getJSONArray("productos").getJSONObject(i).get("precio").toString());
                        descripciones.add(jsonObj.getJSONArray("productos").getJSONObject(i).get("descripcion").toString());
                        ArrayList localesAux = new ArrayList();
                        for (int j = 0; j < jsonObj.getJSONArray("productos").getJSONObject(i).getJSONArray("tiendas").length();j++){
                            localesAux.add(jsonObj.getJSONArray("productos").getJSONObject(i).getJSONArray("tiendas").getString(j));
                        }
                        locales.add(localesAux);
                    }

                    SampleMaterialActivity.this.products = jsonObj.getJSONArray("productos").length();



                    SampleMaterialActivity.this.names = nombres;
                    SampleMaterialActivity.this.prices = precios;
                    SampleMaterialActivity.this.descriptions = descripciones;
                    SampleMaterialActivity.this.shopsArray = locales;


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

            initCards();
        }
    }
}

