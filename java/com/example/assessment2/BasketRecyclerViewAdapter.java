package com.example.assessment2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BasketRecyclerViewAdapter extends RecyclerView.Adapter<BasketRecyclerViewAdapter.MyViewHolder> {

    // Contextul activității sau fragmentului în care este folosit RecyclerView
    Context context;
    // Lista de elemente din coș
    ArrayList<BasketItem> basketItems;

    // Constructorul adapterului care primește contextul și lista de elemente
    public BasketRecyclerViewAdapter(Context context, ArrayList<BasketItem> basketItems) {
        this.context = context;
        this.basketItems = basketItems;
    }

    @NonNull
    @Override
    // Crearea unui ViewHolder și inflatarea layout-ului pentru fiecare rând din RecyclerView
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row_basket, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    // Atribuirea valorilor pentru fiecare view din layout-ul recycler_view_row_basket
    // în funcție de poziția elementului în RecyclerView
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BasketItem basketItem = basketItems.get(position);
        holder.name.setText(basketItem.getName());
        holder.artistName.setText(basketItem.getArtistName());
        holder.price.setText("£" + basketItem.getPrice());
        holder.quantityBasket.setText(String.valueOf(basketItem.getQuantity()));
    }

    @Override
    // Returnează numărul total de elemente din lista de elemente din coș
    public int getItemCount() {
        return basketItems.size();
    }

    // Clasa ViewHolder care conține referințele la fiecare view din layout-ul unui rând din RecyclerView
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, artistName, price;
        EditText quantityBasket;

        // Constructorul ViewHolder-ului care inițializează referințele la view-urile din layout-ul recycler_view_row_basket
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageProdHP);
            name = itemView.findViewById(R.id.nameProd);
            artistName = itemView.findViewById(R.id.artistName);
            price = itemView.findViewById(R.id.priceProd);
            quantityBasket = itemView.findViewById(R.id.quantityBasket);
        }
    }
}
