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
import com.vijay.v_canteen.canteen.Canteen_selected_item;
import com.vijay.v_canteen.pojo.Item_getter;

import java.util.ArrayList;

public class Item_recycleradapter extends RecyclerView.Adapter<Item_recycleradapter.RecyclerViewHolder> {
    ArrayList<Item_getter> allitems;
    Context context;

    public Item_recycleradapter(ArrayList<Item_getter> allitems, Context context) {
        this.allitems = allitems;
        this.context = context;
    }

    @NonNull
    @Override
    public Item_recycleradapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.item_cardview,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,RecyclerView.LayoutParams.WRAP_CONTENT));
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Item_recycleradapter.RecyclerViewHolder holder, int position) {
        final Item_getter itemgetter=allitems.get(position);
        holder.iname.setText(itemgetter.getI_name());
        holder.iprice.setText(itemgetter.getI_price());
        Picasso.with(context).load("http://192.168.0.103/API/V_canteen/photo/item/"+itemgetter.getI_image()).fit().into(holder.image);
    }

    @Override
    public int getItemCount() {
        return allitems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView iname,iprice;
        ImageView image;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            iname=itemView.findViewById(R.id.ic_name);
            iprice=itemView.findViewById(R.id.ic_price);
            image=itemView.findViewById(R.id.ic_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=getAdapterPosition();
                    Log.i("vjnarayanan",allitems.get(pos).getI_name());
                    Intent next=new Intent(context, Canteen_selected_item.class);
//                    next.putExtra("id",String.valueOf(allitems.get(pos).getI_id()));
//                    next.putExtra("name",allitems.get(pos).getI_name());
//                    next.putExtra("type",allitems.get(pos).getI_type());
//                    next.putExtra("price",allitems.get(pos).getI_price());
//                    next.putExtra("quantity",allitems.get(pos).getI_quantity());
                    context.startActivity(next);
                }
            });
        }
    }
}
