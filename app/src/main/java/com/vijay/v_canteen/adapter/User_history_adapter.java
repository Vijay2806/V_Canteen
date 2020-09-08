package com.vijay.v_canteen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vijay.v_canteen.R;
import com.vijay.v_canteen.pojo.History_getter;

import java.util.ArrayList;

public class User_history_adapter extends RecyclerView.Adapter<User_history_adapter.RecyclerViewHolder> {
    ArrayList<History_getter> allhistory;
    Context context;

    public User_history_adapter(ArrayList<History_getter> allhistory, Context context) {
        this.allhistory = allhistory;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.user_history,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,RecyclerView.LayoutParams.WRAP_CONTENT));
        return  new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        final History_getter gethistory=allhistory.get(position);
        holder.i_count.setText(gethistory.getI_count());
        holder.i_name.setText(gethistory.getI_name());
        holder.o_total.setText("..."+gethistory.getO_total());
        if (gethistory.getO_status().equals("0")){
            holder.o_status.setText("Pending...");
            holder.o_status.setTextColor(context.getResources().getColor(R.color.orange));
        }
        else {
            holder.o_status.setText("Delivered");
            holder.o_status.setTextColor(context.getResources().getColor(R.color.green));
        }

    }

    @Override
    public int getItemCount() {
        return allhistory.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView i_count,i_name,o_total,o_status;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            i_count=itemView.findViewById(R.id.uh_iquantity);
            i_name=itemView.findViewById(R.id.uh_iname);
            o_total=itemView.findViewById(R.id.uh_ototal);
            o_status=itemView.findViewById(R.id.uh_status);

        }
    }
}
