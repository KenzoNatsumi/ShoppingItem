package com.example.homewor2; // Thay đổi package name của bạn

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ShoppingItemAdapter extends RecyclerView.Adapter<ShoppingItemAdapter.ShoppingItemViewHolder> {

    private List<ShoppingItem> shoppingList;
    private OnItemActionListener listener;

    // Interface để xử lý sự kiện click trên nút Edit/Delete
    public interface OnItemActionListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public ShoppingItemAdapter(List<ShoppingItem> shoppingList, OnItemActionListener listener) {
        this.shoppingList = shoppingList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShoppingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping, parent, false);
        return new ShoppingItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingItemViewHolder holder, int position) {
        ShoppingItem currentItem = shoppingList.get(position);
        holder.tvItemName.setText(currentItem.getName());
        holder.tvItemQuantity.setText("Qty: " + currentItem.getQuantity());

        // Xử lý sự kiện click cho nút Edit
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(holder.getAdapterPosition());
            }
        });

        // Xử lý sự kiện click cho nút Delete
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingList.size();
    }

    // Cập nhật danh sách và thông báo cho RecyclerView
    public void updateList(List<ShoppingItem> newList) {
        this.shoppingList = newList;
        notifyDataSetChanged(); // Thông báo rằng dữ liệu đã thay đổi
    }

    public static class ShoppingItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItemName;
        public TextView tvItemQuantity;
        public ImageButton btnEdit;
        public ImageButton btnDelete;

        public ShoppingItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tv_item_name);
            tvItemQuantity = itemView.findViewById(R.id.tv_item_quantity);
            btnEdit = itemView.findViewById(R.id.btn_edit_item);
            btnDelete = itemView.findViewById(R.id.btn_delete_item);
        }
    }
}