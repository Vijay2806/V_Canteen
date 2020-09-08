package com.vijay.v_canteen.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vijay.v_canteen.R;
import com.vijay.v_canteen.canteen.Canteen_selected_user;
import com.vijay.v_canteen.pojo.User_getter;
import com.vijay.v_canteen.util.SharedPreference;

import java.util.ArrayList;

public class Canteen_user_recycleradapter extends RecyclerView.Adapter<Canteen_user_recycleradapter.RecyclerViewHolder> {
    ArrayList<User_getter> allusers;
    Context context;

    public Canteen_user_recycleradapter(ArrayList<User_getter> allusers, Context context) {
        this.allusers = allusers;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.user_view,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,RecyclerView.LayoutParams.WRAP_CONTENT));
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        final User_getter usergetter=allusers.get(position);
        holder.name.setText(usergetter.getU_firstname()+" "+usergetter.getU_lastname());
        holder.id.setText(usergetter.getU_id());
    }

    @Override
    public int getItemCount() {
        return allusers.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView name,id;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.uv_name);
            id=itemView.findViewById(R.id.uv_id);
            Integer pos=getAdapterPosition();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=getAdapterPosition();
                    if (pos!=RecyclerView.NO_POSITION){
                        Log.i("vjnarayanan",String.valueOf(allusers.get(pos).getU_serial()));
                        SharedPreference.save("auserial",String.valueOf(allusers.get(pos).getU_serial()));
                        SharedPreference.save("aufirst",allusers.get(pos).getU_firstname());
                        SharedPreference.save("aulast",allusers.get(pos).getU_lastname());
                        SharedPreference.save("auemail",allusers.get(pos).getU_email());
                        SharedPreference.save("auphone",allusers.get(pos).getU_phone());
                        SharedPreference.save("augender",allusers.get(pos).getU_gender());
                        SharedPreference.save("audob",allusers.get(pos).getU_dob());
                        SharedPreference.save("aublock",allusers.get(pos).getU_block());
                        SharedPreference.save("auphoto",allusers.get(pos).getU_photo());
                        SharedPreference.save("auid",allusers.get(pos).getU_id());
                        Intent next=new Intent(context, Canteen_selected_user.class);
                        context.startActivity(next);
                    }
                }
            });
        }
    }
}