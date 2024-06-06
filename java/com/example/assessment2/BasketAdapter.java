package com.example.assessment2;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.BasketViewHolder> {

    private List<BasketItem> basketItems;
    private OnDeleteItemClickListener onDeleteItemClickListener;
    private OnItemClickListener onItemClickListener;

    // Constructor pentru a inițializa lista de elemente din coș
    public BasketAdapter(List<BasketItem> basketItems) {
        this.basketItems = basketItems;
    }

    @NonNull
    @Override
    public BasketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflatarea layout-ului pentru fiecare rând din RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row_basket, parent, false);
        return new BasketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Obținerea elementului din coș la poziția curentă
        BasketItem basketItem = basketItems.get(position);
        // Binding-ul datelor elementului curent la ViewHolder
        holder.bind(basketItem);

        // Setarea unui click listener pe întregul itemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gestionarea click-ului pe element
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(Integer.toString(basketItems.get(position).getId()));
                }
            }
        });

        // Setarea unui click listener pe butonul de ștergere
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gestionarea click-ului pe butonul de ștergere
                if (onDeleteItemClickListener != null) {
                    onDeleteItemClickListener.onDeleteItemClick(position);
                }
            }
        });
    }

    // Interfață pentru a gestiona click-urile pe elemente
    public interface OnItemClickListener {
        void onItemClick(String productId);
    }

    // Metodă pentru a seta listener-ul de click pe elemente
    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        // Returnarea numărului de elemente din coș
        return basketItems.size();
    }

    // Interfață pentru a gestiona click-urile pe butonul de ștergere
    public interface OnDeleteItemClickListener {
        void onDeleteItemClick(int position);
    }

    // Metodă pentru a seta listener-ul de click pe butonul de ștergere
    public void setOnDeleteItemClickListener(OnDeleteItemClickListener listener) {
        onDeleteItemClickListener = listener;
    }

    // Clasa ViewHolder pentru a gestiona vizualizarea fiecărui element din RecyclerView
    public static class BasketViewHolder extends RecyclerView.ViewHolder {
        private TextView itemName, itemArtist, itemPrice;
        private EditText itemQuantity;
        private ImageView itemImage;
        private ImageButton deleteBtn;

        // Constructor pentru ViewHolder
        public BasketViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inițializarea referințelor la elementele UI
            itemName = itemView.findViewById(R.id.nameProd);
            itemArtist = itemView.findViewById(R.id.artistName);
            itemPrice = itemView.findViewById(R.id.priceProd);
            itemQuantity = itemView.findViewById(R.id.quantityBasket);
            itemImage = itemView.findViewById(R.id.imageProdHP);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }

        // Metodă pentru a lega datele unui BasketItem la elementele UI
        public void bind(BasketItem basketItem) {
            itemName.setText(basketItem.getName());
            itemArtist.setText(basketItem.getArtistName());
            itemPrice.setText("Price: £" + basketItem.getPrice());
            itemQuantity.setText("Quantity: " + basketItem.getQuantity());
            // Utilizarea bibliotecii Picasso pentru a încărca imaginea
            Picasso.get().load(basketItem.getImage()).into(itemImage);
        }
    }
}
