package com.inform.communitynoticeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder> {

    private final ArrayList<Request> requestsList;
    private final Context context;
    private final FirebaseConnector firebase= FirebaseConnector.getInstance();

    public RequestsAdapter(ArrayList<Request> requestsList, Context context) {
        this.requestsList = requestsList;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView displayNameTV, emailTV, dateTimeTV, reasonTV;
        MaterialCardView cardView;
        MaterialButton acceptBtn, declineBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            displayNameTV = itemView.findViewById(R.id.displayName_TV);
            emailTV = itemView.findViewById(R.id.email_TV);
            dateTimeTV = itemView.findViewById(R.id.dateTime_TV);
            reasonTV = itemView.findViewById(R.id.reason_TV);
            cardView = itemView.findViewById(R.id.request_cardView);
            acceptBtn = itemView.findViewById(R.id.accept_Btn);
            declineBtn = itemView.findViewById(R.id.decline_Btn);
        }
    }


    @NonNull
    @Override
    public RequestsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View requestView = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_card, parent, false);
        return new RequestsAdapter.ViewHolder(requestView);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestsAdapter.ViewHolder holder, int position) {
        holder.displayNameTV.setText(requestsList.get(position).getDisplayName());
        holder.emailTV.setText(requestsList.get(position).getEmailAddress());
        holder.dateTimeTV.setText(requestsList.get(position).getDateTime());
        holder.reasonTV.setText(requestsList.get(position).getReason());

        holder.acceptBtn.setOnClickListener(view -> firebase.acceptRequest(requestsList.get(position).getRequestID(), requestsList.get(position).getUserID()));
        holder.declineBtn.setOnClickListener(view -> firebase.declineRequest(requestsList.get(position).getRequestID(), requestsList.get(position).getUserID()));
    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }
}
