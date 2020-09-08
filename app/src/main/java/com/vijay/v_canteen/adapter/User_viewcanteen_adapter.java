package com.vijay.v_canteen.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vijay.v_canteen.R;
import com.vijay.v_canteen.pojo.canteen_getter;
import com.vijay.v_canteen.user.User_canteen_items;
import com.vijay.v_canteen.util.SharedPreference;

import java.util.ArrayList;

public class User_viewcanteen_adapter extends RecyclerView.Adapter<User_viewcanteen_adapter.RecyclerViewHolder> {
    ArrayList<canteen_getter> allcanteens;
    Context context;

    public User_viewcanteen_adapter(ArrayList<canteen_getter> allcanteens, Context context) {
        this.allcanteens = allcanteens;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.user_canten_view,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,RecyclerView.LayoutParams.WRAP_CONTENT));
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        final canteen_getter canteengetter=allcanteens.get(position);

        holder.name.setText(canteengetter.getC_name());
        Picasso.with(context).load("http://192.168.0.103/API/V_canteen/photo/canteen/" + canteengetter.getC_image()).fit().into(holder.image);
    }

    @Override
    public int getItemCount() {
        return allcanteens.size();
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView id,name,phone,email,landline;
        ImageView image;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.ucv_canteen_name);
            image=itemView.findViewById(R.id.ucv_view_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer pos=getAdapterPosition();
                    Log.i("vjnarayanan",allcanteens.get(pos).getC_name());
                    Intent next=new Intent(context, User_canteen_items.class);
                    next.putExtra("cid",String.valueOf(allcanteens.get(getAdapterPosition()).getC_id()));
//                    Log.i("canteen id:",allcanteens.get(getAdapterPosition()).getC_id());
                    sp();
                    context.startActivity(next);
                }
            });
        }

        private void sp() {
            SharedPreference.save("canteenname",allcanteens.get(getAdapterPosition()).getC_name());
            SharedPreference.save("canteenid",allcanteens.get(getAdapterPosition()).getC_id());
            SharedPreference.save("canteenphone",allcanteens.get(getAdapterPosition()).getC_phone());
            SharedPreference.save("canteenlandlineext",allcanteens.get(getAdapterPosition()).getC_landline_ext());
            SharedPreference.save("canteenemail",allcanteens.get(getAdapterPosition()).getC_email());

        }
    }
}
