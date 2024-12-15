package com.example.danum.feature.presentation.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.danum.R;
import com.example.danum.core.data.model.Store;
import com.example.danum.feature.presentation.product.ProductDetailActivity;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {

    private final Context context;
    private final List<Store> storeList;
    private final OnItemClickListener onItemClickListener;

    /**
     * 아이템 클릭 리스너 인터페이스
     * - onActionClick: 버튼 클릭 시 호출
     * - onItemClick: 전체 아이템 클릭 시 호출
     */
    public interface OnItemClickListener {
        void onActionClick(Store store);
        void onItemClick(Store store);
    }

    /**
     * 생성자
     *
     * @param context            컨텍스트
     * @param storeList          상점 리스트
     * @param onItemClickListener 아이템 클릭 리스너
     */
    public StoreAdapter(Context context, List<Store> storeList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.storeList = storeList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 아이템 레이아웃을 inflate
        View view = LayoutInflater.from(context).inflate(R.layout.item_store, parent, false);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        Store store = storeList.get(position);

        // 상점 이름 및 주소 설정
        holder.storeNameTextView.setText(store.getName());
        holder.storeAddressTextView.setText(store.getAddress());

        // 거래 상태에 따른 버튼 텍스트 및 배경 설정
        Log.d("StoreAdapter", "거래 상태 확인: " + store.getType());
        switch (store.getType()) {
            case 0: // 거래
                holder.actionButton.setText("거래");
                holder.actionButton.setBackground(
                        ContextCompat.getDrawable(context, R.drawable.button_transaction)
                );
                break;
            case 1: // 나눔
                holder.actionButton.setText("나눔");
                holder.actionButton.setBackground(
                        ContextCompat.getDrawable(context, R.drawable.button_sharing)
                );
                break;
            case 2: // 거래/나눔
                holder.actionButton.setText("거래/나눔");
                holder.actionButton.setBackground(
                        ContextCompat.getDrawable(context, R.drawable.button_transaction_and_sharing)
                );
                break;
        }

        // 상점 이미지 설정
        Uri imageUri = Uri.parse(store.getImageResource()); // store.getImageUri()는 URI 문자열을 반환해야 합니다.
        holder.storeImageView.setImageURI(imageUri);


        // 버튼 클릭 리스너 설정
        holder.actionButton.setOnClickListener(v -> {
            onItemClickListener.onActionClick(store);
            Log.d("StoreAdapter", "Clicked Action Button. Store ID: " + store.getId());
        });

        // 전체 아이템 클릭 리스너 설정
        holder.itemView.setOnClickListener(v -> {
            onItemClickListener.onItemClick(store);
            Log.d("StoreAdapter", "Clicked Item View. Store ID: " + store.getId());
        });
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    /**
     * ViewHolder 클래스
     */
    public static class StoreViewHolder extends RecyclerView.ViewHolder {
        ImageView storeImageView;
        TextView storeNameTextView;
        TextView storeAddressTextView;
        Button actionButton;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            storeImageView = itemView.findViewById(R.id.storeImageView);
            storeNameTextView = itemView.findViewById(R.id.storeNameTextView);
            storeAddressTextView = itemView.findViewById(R.id.storeAddressTextView);
            actionButton = itemView.findViewById(R.id.actionButton);
        }
    }
}
