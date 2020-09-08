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
import com.vijay.v_canteen.admin.Admin_view_canteen;
import com.vijay.v_canteen.R;
import com.vijay.v_canteen.pojo.canteen_getter;
import com.vijay.v_canteen.util.SharedPreference;

import java.util.ArrayList;

public class Admin_canteen_recycleradapter extends RecyclerView.Adapter<Admin_canteen_recycleradapter.RecyclerViewHolder> {
    ArrayList<canteen_getter> allcanteen;
    Context context;

    public Admin_canteen_recycleradapter(ArrayList<canteen_getter> allcanteen, Context context) {
        this.allcanteen = allcanteen;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.canteen_view,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,RecyclerView.LayoutParams.WRAP_CONTENT));
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        final canteen_getter canteengetter=allcanteen.get(position);
//        holder.id.setText(String.valueOf(canteengetter.getC_id()));
        holder.name.setText(canteengetter.getC_name());
        Picasso.with(context).load("http://192.168.0.103/API/V_canteen/photo/canteen/"+canteengetter.getC_image()).fit().into(holder.canteenimage);
        Log.i("vjnarayanan","http://192.168.0.103/API/V_canteen/photo/canteen/"+canteengetter.getC_image());
//        holder.phone.setText(canteengetter.getC_phone());
//        holder.email.setText(canteengetter.getC_email());
//        holder.landline.setText(canteengetter.getC_landline_ext());

    }

    @Override
    public int getItemCount() {
        return allcanteen.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView canteenimage;
        TextView name;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            canteenimage=itemView.findViewById(R.id.canteen_view_img);

//            id=itemView.findViewById(R.id.cv_canteen_id);
            name=itemView.findViewById(R.id.cv_canteen_name);
//            phone=itemView.findViewById(R.id.cv_canteen_mobile);
//            email=itemView.findViewById(R.id.cv_canteen_email);
//            landline=itemView.findViewById(R.id.cv_canteen_landline_ext);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreference.initialize(context);
                    Integer pos=getAdapterPosition();
                    SharedPreference.save("image",allcanteen.get(pos).getC_image());
                    SharedPreference.save("id" ,String.valueOf(allcanteen.get(pos).getC_id()));
                    SharedPreference.save("name",allcanteen.get(pos).getC_name());
                    SharedPreference.save("phone",allcanteen.get(pos).getC_phone());
                    SharedPreference.save("email",allcanteen.get(pos).getC_email());
                    SharedPreference.save("landline",allcanteen.get(pos).getC_landline_ext());
                    Intent next =new Intent(context, Admin_view_canteen.class);
                    context.startActivity(next);

                }
            });

        }

    }
}
