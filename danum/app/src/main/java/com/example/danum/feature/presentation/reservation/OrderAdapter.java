package com.example.danum.feature.presentation.reservation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.danum.R;
import com.example.danum.core.data.model.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final Context context;
    private final List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.orderImageView.setImageResource(order.getImageResource());
        holder.orderTitleTextView.setText(order.getTitle());
        holder.orderPriceTextView.setText(order.getPrice());
        holder.orderQuantityTextView.setText("주문 수량: " + order.getQuantity() + "개");
        holder.orderPickupTimeTextView.setText("픽업시간: " + order.getPickupTime());

        // 버튼 설정
        holder.orderStatusButton.setText(order.getStatus());
        holder.orderStatusButton.setBackgroundTintList(context.getResources().getColorStateList(
                order.getStatus().equals("대기 중") ? android.R.color.holo_red_light : android.R.color.darker_gray
        ));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView orderImageView;
        TextView orderTitleTextView, orderPriceTextView, orderQuantityTextView, orderPickupTimeTextView;
        Button orderStatusButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderImageView = itemView.findViewById(R.id.orderImageView);
            orderTitleTextView = itemView.findViewById(R.id.orderTitleTextView);
            orderPriceTextView = itemView.findViewById(R.id.orderPriceTextView);
            orderQuantityTextView = itemView.findViewById(R.id.orderQuantityTextView);
            orderPickupTimeTextView = itemView.findViewById(R.id.orderPickupTimeTextView);
            orderStatusButton = itemView.findViewById(R.id.orderStatusButton);
        }
    }
}
