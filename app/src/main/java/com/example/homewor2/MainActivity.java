package com.example.homewor2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ShoppingItemAdapter.OnItemActionListener {

    private EditText etItemName;
    private EditText etQuantity;
    private Button btnAdd;
    private Button btnCancel;
    private CardView addShoppingItemCard;
    private Button btnOpenAddItem;

    private RecyclerView recyclerView;
    private ShoppingItemAdapter adapter;
    private List<ShoppingItem> shoppingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etItemName = findViewById(R.id.etItemName);
        etQuantity = findViewById(R.id.etQuantity);
        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);
        addShoppingItemCard = findViewById(R.id.addShoppingItemCard);
        btnOpenAddItem = findViewById(R.id.btnOpenAddItem);

        recyclerView = findViewById(R.id.recycler_view_shopping_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        shoppingList = new ArrayList<>();

        adapter = new ShoppingItemAdapter(shoppingList, this);
        recyclerView.setAdapter(adapter);


        addShoppingItemCard.setVisibility(View.GONE);

        btnOpenAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addShoppingItemCard.setVisibility(View.VISIBLE);
                etItemName.setText("");
                etQuantity.setText("");
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToList();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAddItem();
            }
        });
    }

    private void addItemToList() {
        String itemName = etItemName.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();

        if (itemName.isEmpty()) {
            etItemName.setError("Item name cannot be empty");
            return;
        }

        if (quantityStr.isEmpty()) {
            etQuantity.setError("Quantity cannot be empty");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            etQuantity.setError("Invalid quantity. Please enter a number.");
            return;
        }

        boolean itemExists = false;
        for (ShoppingItem item : shoppingList) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                item.setQuantity(item.getQuantity() + quantity);
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            ShoppingItem newItem = new ShoppingItem(itemName, quantity);
            shoppingList.add(newItem);
        }

        adapter.notifyDataSetChanged();

        Toast.makeText(this, "Added: " + itemName + " (Qty: " + quantity + ")", Toast.LENGTH_SHORT).show();

        addShoppingItemCard.setVisibility(View.GONE);
    }

    private void cancelAddItem() {
        etItemName.setText("");
        etQuantity.setText("");
        // Ẩn popup
        addShoppingItemCard.setVisibility(View.GONE);
        Toast.makeText(this, "Add item cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditClick(int position) {
        ShoppingItem itemToEdit = shoppingList.get(position);
        Toast.makeText(this, "Edit: " + itemToEdit.getName(), Toast.LENGTH_SHORT).show();

        etItemName.setText(itemToEdit.getName());
        etQuantity.setText(String.valueOf(itemToEdit.getQuantity()));
        addShoppingItemCard.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDeleteClick(int position) {
        // Xử lý khi nhấn nút Delete cho item tại 'position'
        if (position != RecyclerView.NO_POSITION) {
            String itemName = shoppingList.get(position).getName();
            shoppingList.remove(position);
            adapter.notifyItemRemoved(position);
            Toast.makeText(this, "Deleted: " + itemName, Toast.LENGTH_SHORT).show();
        }
    }
}