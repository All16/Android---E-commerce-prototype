package com.example.assessment2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BasketFragment extends Fragment {

    private List<BasketItem> basketItems = new ArrayList<>();
    private BasketAdapter basketAdapter;
    private Button contBtn, checkOutBtn;
    private RecyclerView recyclerView;
    private TextView basketSubTotal;
    private float totalAmount = 0.0f;
    private String productId;
    private int quantity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflatarea layout-ului pentru fragmentul de coș
        View view = inflater.inflate(R.layout.fragment_basket, container, false);

        // Inițializarea referințelor la elementele UI
        contBtn = view.findViewById(R.id.contBtn);
        checkOutBtn = view.findViewById(R.id.checkOutBtn);
        recyclerView = view.findViewById(R.id.basketRecyclerView);
        basketSubTotal = view.findViewById(R.id.basketSubTotal);

        // Obținerea argumentelor transmise fragmentului
        Bundle args = getArguments();
        if (args != null) {
            productId = args.getString("productId", "");
            quantity = args.getInt("quantity", 0);
        }

        // Setarea click listener-ului pentru butonul de continuare a cumpărăturilor
        contBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contShopping();
            }
        });

        // Setarea click listener-ului pentru butonul de checkout
        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCheckOut();
            }
        });

        // Inițializarea RecyclerView și a Adapter-ului
        basketAdapter = new BasketAdapter(basketItems);
        recyclerView.setAdapter(basketAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Setarea listener-ului pentru click-uri pe elementele din coș
        basketAdapter.setOnItemClickListener(new BasketAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String productId) {
                // Gestionarea click-ului pe element, navigând la fragmentul SpecItem
                navigateToSpecItemFragment(productId);
            }
        });

        // Setarea listener-ului pentru click-uri pe butonul de ștergere
        basketAdapter.setOnDeleteItemClickListener(new BasketAdapter.OnDeleteItemClickListener() {
            @Override
            public void onDeleteItemClick(int position) {
                // Gestionarea logicii de ștergere a elementului din coș
                deleteItemFromBasket(position);
            }
        });

        // Recuperarea datelor din Firebase
        DatabaseReference basketReference = FirebaseDatabase.getInstance("https://assessment2-4390a-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Basket");
        basketReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Curățarea elementelor existente înainte de a adăuga altele noi
                basketItems.clear();
                // Resetarea totalAmount înainte de recalculare
                totalAmount = 0.0f;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BasketItem basketItem = snapshot.getValue(BasketItem.class);
                    if (basketItem != null) {
                        basketItems.add(basketItem);
                        // Adăugarea totalului fiecărui element la totalAmount
                        totalAmount += basketItem.getTotal();
                    }
                }

                // Actualizarea TextView-ului cu totalul calculat
                basketSubTotal.setText("Subtotal: £" + String.format("%.2f", totalAmount));

                // Notificarea adapter-ului că datele s-au schimbat
                basketAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Gestionarea erorilor la recuperarea datelor
            }
        });

        return view;
    }

    // Metodă pentru a continua cumpărăturile, navigând la fragmentul home
    private void contShopping() {
        FragmentHome fragmentHome  = new FragmentHome();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentHome, fragmentHome)
                .addToBackStack(null)
                .commit();
    }

    // Metodă pentru a merge la checkout
    private void goCheckOut() {
        CheckOutFragment checkOutFragment  = new CheckOutFragment();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentHome, checkOutFragment)
                .addToBackStack(null)
                .commit();

        // Transmiterea argumentelor la fragmentul de checkout pentru a actualiza baza de date
        Bundle args = new Bundle();
        args.putFloat("totalAmount", totalAmount);
        args.putString("productId", productId);
        args.putInt("quantity", quantity);
        checkOutFragment.setArguments(args);
    }

    // Metodă pentru a șterge un element din coș
    private void deleteItemFromBasket(int position) {
        // Înlăturarea elementului din dataset
        BasketItem deletedItem = basketItems.get(position);
        float deletedItemTotal = deletedItem.getTotal();
        basketItems.remove(position);

        // Actualizarea totalAmount
        totalAmount -= deletedItemTotal;

        // Actualizarea UI-ului cu noul totalAmount
        basketSubTotal.setText("Subtotal: £" + String.format("%.2f", totalAmount));

        // Notificarea adapter-ului despre ștergerea elementului
        basketAdapter.notifyItemRemoved(position);

        // Ștergerea elementului din Firebase
        DatabaseReference basketReference = FirebaseDatabase.getInstance("https://assessment2-4390a-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Basket");
        basketReference.orderByChild("id").equalTo(deletedItem.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Eliminat cu succes din Firebase
                        } else {
                            // Gestionarea erorilor la ștergerea din Firebase
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Gestionarea erorilor la recuperarea datelor
            }
        });
    }

    // Gestionarea click-ului pe un element, navigând la fragmentul SpecItem
    public void onItemClick(String productId) {
        navigateToSpecItemFragment(productId);
    }

    // Metodă pentru a naviga la fragmentul SpecItem
    private void navigateToSpecItemFragment(String productId) {
        // Crearea unei noi instanțe a fragmentului SpecItem
        SpecItemFragment specItemFragment = new SpecItemFragment();

        // Transmiterea productId la fragmentul SpecItem folosind un Bundle
        Bundle args = new Bundle();
        args.putString("productId", productId);
        specItemFragment.setArguments(args);

        // Înlocuirea fragmentului curent cu fragmentul SpecItem
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentHome, specItemFragment)
                .addToBackStack(null)
                .commit();
    }
}
