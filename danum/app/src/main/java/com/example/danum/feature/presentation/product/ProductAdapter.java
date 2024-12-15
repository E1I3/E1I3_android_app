package com.example.danum.feature.presentation.product;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.danum.R;
import com.example.danum.core.data.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.StoreViewHolder> {

    private final Context context;
    private final List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;

        Log.d("상품", "ProductAdapter: " + productList.get(0).getPrice());
        Log.d("상품", "ProductAdapter: " + productList.get(1).getPrice());
        Log.d("상품", "ProductAdapter: " + productList.get(2).getPrice());
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.storeNameTextView.setText(product.getName());
        Log.d("상품 가격", "onBindViewHolder: " + product.getPrice());
        holder.storePriceTextView.setText("거래가(1개당)\n" + product.getPrice() + "원");
        holder.tvQuantity.setText(String.valueOf(product.getQuantity()));

        holder.storeImageView.setImageResource(product.getImageResource());

        holder.btnDecrease.setOnClickListener(v -> {
            int quantity = product.getQuantity();
            if (quantity > 0) {
                product.setQuantity(quantity - 1);
                holder.tvQuantity.setText(String.valueOf(product.getQuantity()));
            }
        });

        holder.btnIncrease.setOnClickListener(v -> {
            int quantity = product.getQuantity();
            product.setQuantity(quantity + 1);
            holder.tvQuantity.setText(String.valueOf(product.getQuantity()));
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class StoreViewHolder extends RecyclerView.ViewHolder {
        ImageView storeImageView;
        TextView storeNameTextView, storePriceTextView, tvQuantity;
        Button btnIncrease;
        ImageButton btnDecrease;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            storeImageView = itemView.findViewById(R.id.storeImageView);
            storeNameTextView = itemView.findViewById(R.id.storeNameTextView);
            storePriceTextView = itemView.findViewById(R.id.storePriceTextView);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
        }
    }
}
