package com.example.assessment2;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CheckOutFragment extends Fragment {

    private TextView orderTotalTextView, address, orderTotalText2;
    private TextView paymentMethodTextView;
    private float total;
    private String paymentMethod,addressDelivery, deliveryOption, productId;
    private RadioGroup paymentRadioGroup, addressRadioGroup, deliveryRadioButton;
    private ImageButton cvvInfo, pcInfo;
    private Button cancelBtn, placeOrder;
    private  String new_addressDelivery;
    private EditText nameCardField, cardNoField, expiryDateField;
    private int quantity;
    private String new_payment_method = "";


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_out, container, false);

        Bundle args = getArguments();
        if (args != null) {
            total = args.getFloat("totalAmount", 0.0f);
            productId = args.getString("productId", "");
            quantity = args.getInt("quantity", 0);
        }



        orderTotalTextView = view.findViewById(R.id.orderTotal);
        paymentMethodTextView = view.findViewById(R.id.paymentMethod);
        paymentRadioGroup = view.findViewById(R.id.paymentRadioGroup);
        addressRadioGroup = view.findViewById(R.id.addressRadioGroup);
        cvvInfo = view.findViewById(R.id.cvvInfo);
        pcInfo = view.findViewById(R.id.pcInfo);
        address = view.findViewById(R.id.address);
        deliveryRadioButton = view.findViewById(R.id.deliveryRadioGroup);
        orderTotalText2 = view.findViewById(R.id.orderTotalText2);
        nameCardField = view.findViewById(R.id.nameCardField);
        cardNoField = view.findViewById(R.id.cardNoField);
        expiryDateField = view.findViewById(R.id.expiryDateField);



        cancelBtn = view.findViewById(R.id.cancelBtn);
        placeOrder = view.findViewById(R.id.placeOrder);

        //EditText are made invisible at first
        view.findViewById(R.id.nameCardField).setVisibility(View.GONE);
        view.findViewById(R.id.cardNoField).setVisibility(View.GONE);
        view.findViewById(R.id.expiryDateField).setVisibility(View.GONE);
        view.findViewById(R.id.cvvField).setVisibility(View.GONE);
        view.findViewById(R.id.addressField).setVisibility(View.GONE);
        view.findViewById(R.id.cityField).setVisibility(View.GONE);
        view.findViewById(R.id.postCodeField).setVisibility(View.GONE);
        view.findViewById(R.id.countryField).setVisibility(View.GONE);
        view.findViewById(R.id.cvvInfo).setVisibility(View.GONE);
        view.findViewById(R.id.pcInfo).setVisibility(View.GONE);

        pcInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastMessage(2);
            }
        });

        cvvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastMessage(1);
            }
        });



        // Retrieve user details from Firebase
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userReference = FirebaseDatabase.getInstance("https://assessment2-4390a-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Registered Users").child(userId);

            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ReadWriteUserDetails userDetails = dataSnapshot.getValue(ReadWriteUserDetails.class);

                    if (userDetails != null) {
                        // Display order total
                        orderTotalTextView.setText("£"+ Float.toString(total));
                        orderTotalText2.setText("Order Total: £"+Float.toString(total));


                        // Display payment method
                        paymentMethod = String.format(
                                "Card ending in %s,\n Exp: %s",
                                userDetails.acc_num.substring(userDetails.acc_num.length() - 4),
                                userDetails.acc_exp_date
                        );
                        paymentMethodTextView.setText(paymentMethod);

                        addressDelivery = userDetails.address + ",\n" +
                                userDetails.postcode + ",\n" +
                                userDetails.city + ",\n" +
                                userDetails.country;

                        address.setText(addressDelivery);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors in data retrieval
                }
            });
        }

        // OnCheckedChangeListener for Payment RadioGroup
        paymentRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.otherPayment) {
                    // Show relevant EditText views for payment
                    view.findViewById(R.id.nameCardField).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.cardNoField).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.expiryDateField).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.cvvField).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.cvvInfo).setVisibility(View.VISIBLE);

                    String userName = nameCardField.getText().toString();
                    String userCardNum = cardNoField.getText().toString();
                    String userExpDate = expiryDateField.getText().toString();


                    // Combine the user-input address details
                    new_payment_method = userName + ",\n" + userCardNum + ",\n" + userExpDate;



                } else {
                    // Hide relevant EditText views for payment
                    view.findViewById(R.id.nameCardField).setVisibility(View.GONE);
                    view.findViewById(R.id.cardNoField).setVisibility(View.GONE);
                    view.findViewById(R.id.expiryDateField).setVisibility(View.GONE);
                    view.findViewById(R.id.cvvField).setVisibility(View.GONE);
                    view.findViewById(R.id.cvvInfo).setVisibility(View.GONE);
                    new_payment_method = paymentMethod;
                }
            }
        });

//  OnCheckedChangeListener for Address RadioGroup
        addressRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.otherAddress) {
                    // Show relevant EditText views for address
                    view.findViewById(R.id.addressField).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.cityField).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.postCodeField).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.countryField).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.pcInfo).setVisibility(View.VISIBLE);

                    // Get the text from the EditText views for the user-input address
                    String userAddress = ((EditText) view.findViewById(R.id.addressField)).getText().toString();
                    String userCity = ((EditText) view.findViewById(R.id.cityField)).getText().toString();
                    String userPostCode = ((EditText) view.findViewById(R.id.postCodeField)).getText().toString();
                    String userCountry = ((EditText) view.findViewById(R.id.countryField)).getText().toString();

                    // Combine the user-input address details
                    new_addressDelivery = userAddress + ",\n" + userPostCode + ",\n" + userCity + ",\n" + userCountry;
                } else {
                    // Hide relevant EditText views for address
                    view.findViewById(R.id.addressField).setVisibility(View.GONE);
                    view.findViewById(R.id.cityField).setVisibility(View.GONE);
                    view.findViewById(R.id.postCodeField).setVisibility(View.GONE);
                    view.findViewById(R.id.countryField).setVisibility(View.GONE);
                    view.findViewById(R.id.pcInfo).setVisibility(View.GONE);
                    new_addressDelivery = addressDelivery;

                    // Use the existing address details (retrieved from Firebase)

                }
            }
        });



        deliveryRadioButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                if (checkedId == R.id.expressDelivery){
                    total +=5.00f;
                    orderTotalTextView.setText("£"+Float.toString(total));
                    orderTotalTextView.requestFocus();
                    orderTotalText2.setText("Order Total: £"+Float.toString(total));


                }
                else{
                    orderTotalTextView.setText("£"+ Float.toString(total));
                    orderTotalText2.setText("Order Total: £"+Float.toString(total));
                }
                deliveryOption = selectedRadioButton.getText().toString();

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderConfirmed();
                placeOrder();

            }
        });
        return view;


    }



    private void toastMessage(int x){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, null);
        Toast toast = new Toast(getContext());
        TextView text = (TextView) layout.findViewById(R.id.toastMessage);
        text.setGravity(Gravity.RIGHT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 100, 100);
        if(x == 1){
            text.setText("A card security code is a series of numbers that, in addition to the bank card number," +
                    " is printed on a credit or debit card" );


        } else if (x == 2) {
            text.setText("A postal code is a series of letters or digits or both, sometimes including spaces or punctuation, " +
                    "included in a postal address for the purpose of sorting mail. " );

        }
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

    }

    private void cancel(){

        BasketFragment basketFragment  = new BasketFragment();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentHome, basketFragment)
                .addToBackStack(null)
                .commit();


    }

    private void orderConfirmed(){


        OrderPlacedFragment orderPlacedFragment  = new OrderPlacedFragment();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentHome, orderPlacedFragment)
                .addToBackStack(null)
                .commit();
        Bundle args = new Bundle();
        args.putString("deliveryOption", deliveryOption);
        args.putString("deliveryAddress", new_addressDelivery);
        args.putString("payment", new_payment_method);
        args.putString("total_value", String.valueOf(total));

        orderPlacedFragment.setArguments(args);


    }

    private void placeOrder() {

        // Reference to the "Basket" node
        DatabaseReference basketReference = FirebaseDatabase.getInstance("https://assessment2-4390a-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Basket");

        // Remove the entire "Basket" node
        basketReference.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Basket node removed successfully
                        Toast.makeText(getContext(), "Basket cleared", Toast.LENGTH_SHORT).show();
                        updateProductQuantities(productId, quantity);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure to remove the basket
                        Toast.makeText(getContext(), "Failed to clear basket", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void updateProductQuantities(String productId, int quantity) {
        DatabaseReference productsReference = FirebaseDatabase.getInstance("https://assessment2-4390a-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Products");

        ValueEventListener productsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String productIdString = productId.trim();
                if (!productIdString.isEmpty()) {
                    try {
                        int productIdValue = Integer.parseInt(productIdString);

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Products product = snapshot.getValue(Products.class);
                            if (product != null && productIdValue == product.getId()) {
                                // Update the product quantity
                                int updatedQuantity = product.getQuantity() - quantity;
                                snapshot.getRef().child("quantity").setValue(updatedQuantity);
                                break;  // Break out of the loop once the product is found
                            }
                        }
                    } catch (NumberFormatException e) {
                        // Handle the case where productId is not a valid integer
                        Log.e("CheckOutFragment", "Invalid productId format: " + productIdString);
                    }
                } else {
                    // Handle the case where productId is empty
                    Log.e("CheckOutFragment", "Empty productId");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors in data retrieval
            }
        };

        // Attach ValueEventListener to the "Products" node
        productsReference.addListenerForSingleValueEvent(productsListener);
    }

}
