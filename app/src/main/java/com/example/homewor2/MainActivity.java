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
    private CardView addShoppingItemCard; // CardView chứa popup
    private Button btnOpenAddItem; // Nút "Add Item" ở trên cùng

    private RecyclerView recyclerView;
    private ShoppingItemAdapter adapter;
    private List<ShoppingItem> shoppingList; // Danh sách các ShoppingItem

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ các View từ layout popup
        etItemName = findViewById(R.id.etItemName);
        etQuantity = findViewById(R.id.etQuantity);
        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);
        addShoppingItemCard = findViewById(R.id.addShoppingItemCard);
        btnOpenAddItem = findViewById(R.id.btnOpenAddItem);

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recycler_view_shopping_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        shoppingList = new ArrayList<>(); // Khởi tạo danh sách rỗng

        // Khởi tạo Adapter và thiết lập cho RecyclerView
        adapter = new ShoppingItemAdapter(shoppingList, this); // 'this' vì MainActivity implements OnItemActionListener
        recyclerView.setAdapter(adapter);


        // Ẩn popup "Add Shopping Item" ban đầu
        addShoppingItemCard.setVisibility(View.GONE);

        // Xử lý sự kiện click cho nút "Add Item" ở trên cùng
        btnOpenAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị popup khi nhấn nút "Add Item"
                addShoppingItemCard.setVisibility(View.VISIBLE);
                // Xóa dữ liệu cũ trong EditText khi mở lại popup
                etItemName.setText("");
                etQuantity.setText("");
            }
        });

        // Xử lý sự kiện click cho nút "Add" trong popup
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToList();
            }
        });

        // Xử lý sự kiện click cho nút "Cancel" trong popup
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

        // Kiểm tra xem sản phẩm đã tồn tại trong danh sách chưa
        boolean itemExists = false;
        for (ShoppingItem item : shoppingList) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                // Nếu sản phẩm đã tồn tại, tăng số lượng
                item.setQuantity(item.getQuantity() + quantity);
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            // Nếu sản phẩm chưa tồn tại, thêm mới vào danh sách
            ShoppingItem newItem = new ShoppingItem(itemName, quantity);
            shoppingList.add(newItem);
        }

        // Cập nhật RecyclerView
        adapter.notifyDataSetChanged(); // Hoặc adapter.updateList(shoppingList);

        // Hiển thị thông báo thành công
        Toast.makeText(this, "Added: " + itemName + " (Qty: " + quantity + ")", Toast.LENGTH_SHORT).show();

        // Ẩn popup sau khi thêm
        addShoppingItemCard.setVisibility(View.GONE);
    }

    private void cancelAddItem() {
        // Xóa nội dung trong các trường input
        etItemName.setText("");
        etQuantity.setText("");
        // Ẩn popup
        addShoppingItemCard.setVisibility(View.GONE);
        Toast.makeText(this, "Add item cancelled", Toast.LENGTH_SHORT).show();
    }

    // Triển khai các phương thức từ interface OnItemActionListener
    @Override
    public void onEditClick(int position) {
        // TODO: Xử lý khi nhấn nút Edit cho item tại 'position'
        // Bạn có thể mở lại popup với thông tin của item này để chỉnh sửa
        ShoppingItem itemToEdit = shoppingList.get(position);
        Toast.makeText(this, "Edit: " + itemToEdit.getName(), Toast.LENGTH_SHORT).show();

        // Ví dụ: Mở lại popup và điền thông tin của item vào EditText
        etItemName.setText(itemToEdit.getName());
        etQuantity.setText(String.valueOf(itemToEdit.getQuantity()));
        addShoppingItemCard.setVisibility(View.VISIBLE);

        // Để tránh thêm item mới khi chỉnh sửa, bạn có thể cần một biến trạng thái
        // hoặc logic phức tạp hơn để xác định đang thêm mới hay chỉnh sửa.
        // Hiện tại, nhấn Add sẽ thêm một item mới hoặc tăng số lượng của item nếu tên trùng.
    }

    @Override
    public void onDeleteClick(int position) {
        // Xử lý khi nhấn nút Delete cho item tại 'position'
        if (position != RecyclerView.NO_POSITION) {
            String itemName = shoppingList.get(position).getName();
            shoppingList.remove(position); // Xóa item khỏi danh sách
            adapter.notifyItemRemoved(position); // Thông báo cho adapter biết item đã bị xóa
            Toast.makeText(this, "Deleted: " + itemName, Toast.LENGTH_SHORT).show();
        }
    }
}