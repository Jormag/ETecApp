package com.example.cristian.etecapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SampleMaterialAdapter extends RecyclerView.Adapter<SampleMaterialAdapter.ViewHolder> {
    private static final String DEBUG_TAG = "SampleMaterialAdapter";

    public Context context;
    public ArrayList<Card> cardsList;

    public SampleMaterialAdapter(Context context, ArrayList<Card> cardsList) {
        this.context = context;
        this.cardsList = cardsList;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        String name = cardsList.get(position).getName();
        String price = cardsList.get(position).getPrice();
        int color = cardsList.get(position).getColorResource();
        TextView nameTextView = viewHolder.name;
        TextView priceTextView = viewHolder.price;
        priceTextView.setText(price);
        nameTextView.setBackgroundColor(color);
        nameTextView.setText(name);
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder viewHolder) {
        super.onViewDetachedFromWindow(viewHolder);
        viewHolder.itemView.clearAnimation();
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder viewHolder) {
        super.onViewAttachedToWindow(viewHolder);
        animateCircularReveal(viewHolder.itemView);
    }

    public void animateCircularReveal(View view) {
        int centerX = 0;
        int centerY = 0;
        int startRadius = 0;
        int endRadius = Math.max(view.getWidth(), view.getHeight());
        Animator animation = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius);
        view.setVisibility(View.VISIBLE);
        animation.start();
    }

    public void animateCircularDelete(final View view, final int list_position) {
        int centerX = view.getWidth();
        int centerY = view.getHeight();
        int startRadius = view.getWidth();
        int endRadius = 0;
        Animator animation = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius);

        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                Log.d(DEBUG_TAG, "SampleMaterialAdapter onAnimationEnd for Edit adapter position " + list_position);
                Log.d(DEBUG_TAG, "SampleMaterialAdapter onAnimationEnd for Edit cardId " + getItemId(list_position));

                view.setVisibility(View.INVISIBLE);
                cardsList.remove(list_position);
                notifyItemRemoved(list_position);
            }
        });
        animation.start();
    }

    public void addCard(String name, String price, String description, ArrayList<String> shopArray, int color) {
        Card card = new Card();
        card.setName(name);
        card.setPrice(price);
        card.setDescription(description);
        card.setShopArray(shopArray);
        card.setColorResource(color);
        card.setId(getItemCount());
        cardsList.add(card);
        ((SampleMaterialActivity) context).doSmoothScroll(getItemCount());
        notifyItemInserted(getItemCount());
    }

    public void updateCard(String name, int list_position) {
        cardsList.get(list_position).setName(name);
        Log.d(DEBUG_TAG, "list_position is " + list_position);
        notifyItemChanged(list_position);
    }

    public void deleteCard(View view, int list_position) {
        animateCircularDelete(view, list_position);
    }

    @Override
    public int getItemCount() {
        if (cardsList.isEmpty()) {
            return 0;
        } else {
            return cardsList.size();
        }
    }

    @Override
    public long getItemId(int position) {
        return cardsList.get(position).getId();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater li = LayoutInflater.from(viewGroup.getContext());
        View v = li.inflate(R.layout.card_view_holder, viewGroup, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView price;
        private ImageButton addCartButton;
        private LinearLayout linearLayout;
        private Button deleteButton;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            price = (TextView) v.findViewById(R.id.price);
            addCartButton = (ImageButton) v.findViewById(R.id.add_cart_button);
            deleteButton = (Button)v.findViewById(R.id.delete_button);
            linearLayout = (LinearLayout)v.findViewById(R.id.sampleLayout);

            linearLayout.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.GONE);

            addCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    animateCircularDelete(itemView, getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pair<View, String> p1 = Pair.create((View) name, SampleMaterialActivity.TRANSITION_INITIAL);
                    Pair<View, String> p2 = Pair.create((View) price, SampleMaterialActivity.TRANSITION_NAME);
                    Pair<View, String> p3 = Pair.create((View) addCartButton, SampleMaterialActivity.TRANSITION_DELETE_BUTTON);

                    ActivityOptionsCompat options;
                    Activity act = (AppCompatActivity) context;
                    options = ActivityOptionsCompat.makeSceneTransitionAnimation(act, p1, p2, p3);

                    int requestCode = getAdapterPosition();
                    String name = cardsList.get(requestCode).getName();
                    String price = cardsList.get(requestCode).getPrice();
                    String description = cardsList.get(requestCode).getDescription();
                    ArrayList<String> shopArray = cardsList.get(requestCode).getShopArray();
                    int color = cardsList.get(requestCode).getColorResource();

                    Log.d(DEBUG_TAG, "SampleMaterialAdapter itemView listener for Edit adapter position " + requestCode);

                    //Este modifica lo que sale al hacer click en una card
                    Intent transitionIntent = new Intent(context, TransitionEditActivity.class);
                    transitionIntent.putExtra(SampleMaterialActivity.EXTRA_NAME, name);
                    transitionIntent.putExtra(SampleMaterialActivity.EXTRA_PRICE, price);
                    transitionIntent.putExtra(SampleMaterialActivity.EXTRA_DESCRIPTION, description);
                    transitionIntent.putExtra(SampleMaterialActivity.EXTRA_SHOPS, shopArray);
                    transitionIntent.putExtra(SampleMaterialActivity.EXTRA_COLOR, color);
                    transitionIntent.putExtra(SampleMaterialActivity.EXTRA_UPDATE, false);
                    transitionIntent.putExtra(SampleMaterialActivity.EXTRA_DELETE, false);
                    ((AppCompatActivity) context).startActivityForResult(transitionIntent, requestCode, options.toBundle());
                }
            });
        }
    }
}


