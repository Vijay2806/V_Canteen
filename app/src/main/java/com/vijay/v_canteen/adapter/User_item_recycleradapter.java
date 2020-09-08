package com.vijay.v_canteen.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.vijay.v_canteen.R;
import com.vijay.v_canteen.pojo.Item_getter;
import com.vijay.v_canteen.user.User_selected_item;
import com.vijay.v_canteen.util.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User_item_recycleradapter extends RecyclerView.Adapter<User_item_recycleradapter.RecyclerViewHolder> {
    ArrayList<Item_getter> allitems;
    Context context;

    public User_item_recycleradapter(ArrayList<Item_getter> allitems, Context context) {
        this.allitems = allitems;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.item_cardview,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,RecyclerView.LayoutParams.WRAP_CONTENT));
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        final Item_getter itemgetter=allitems.get(position);
        holder.iname.setText(itemgetter.getI_name());
        holder.iprice.setText("Rs."+itemgetter.getI_price()+" per plate");
        holder.itype.setText(itemgetter.getI_type());
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
        TextView iname,iprice,itype;
        ImageView image;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            iname=itemView.findViewById(R.id.ic_name);
            iprice=itemView.findViewById(R.id.ic_price);
            itype=itemView.findViewById(R.id.ic_type);

            image=itemView.findViewById(R.id.ic_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer pos=getAdapterPosition();
                    String iid,name,type,price,quantity,image,cid,uid;
                    Log.i("vijaynarayanan","user id :"+ SharedPreference.get("userial")+"  Item id :"+String.valueOf(allitems.get(pos).getI_id()));
                    iid=String.valueOf(allitems.get(pos).getI_id());
                    name=allitems.get(pos).getI_name();
                    type=allitems.get(pos).getI_type();
                    price=allitems.get(pos).getI_price();
                    quantity=allitems.get(pos).getI_quantity();
                    image=allitems.get(pos).getI_image();
                    cid=allitems.get(pos).getC_id();
                    uid=SharedPreference.get("userial");
                    getfavorite(iid,name,type,price,quantity,image,cid,uid);
//                    Log.i("vjnarayanan",allitems.get(pos).getI_name());
//                    Intent next=new Intent(context, User_selected_item.class);
//                    next.putExtra("id",String.valueOf(allitems.get(pos).getI_id()));
//                    next.putExtra("name",allitems.get(pos).getI_name());
//                    next.putExtra("type",allitems.get(pos).getI_type());
//                    next.putExtra("price",allitems.get(pos).getI_price());
//                    next.putExtra("quantity",allitems.get(pos).getI_quantity());
//                    next.putExtra("image",allitems.get(pos).getI_image());
//                    next.putExtra("cid",allitems.get(pos).getC_id());
//                    context.startActivity(next);
                }
            });
        }
    }

    private void getfavorite(final String iid, final String name, final String type, final String price, final String quantity, final String image, final String cid, final String uid) {
        StringRequest request=new StringRequest(Request.Method.POST, context.getText(R.string.url) + "get_favorite.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jobj=new JSONObject(response);
                    Log.i("vjnarayanan",response);
                    if (jobj.getString("status").equals("0")){
                        Log.i("vj",jobj.getString("message"));
                        JSONObject ob=jobj.getJSONObject("data");
                        Log.i("favorite",ob.getString("fav_status"));
                        Intent next=new Intent(context,User_selected_item.class);
                        next.putExtra("id",iid);
                    next.putExtra("name",name);
                    next.putExtra("type",type);
                    next.putExtra("price",price);
                    next.putExtra("quantity",quantity);
                    next.putExtra("image",image);
                    next.putExtra("cid",cid);
                    next.putExtra("fav",ob.getString("fav_status"));
                        context.startActivity(next);
                    }
                    else if (jobj.getString("status").equals("1")){
                        Intent next=new Intent(context,User_selected_item.class);
                        next.putExtra("id",iid);
                        next.putExtra("name",name);
                        next.putExtra("type",type);
                        next.putExtra("price",price);
                        next.putExtra("quantity",quantity);
                        next.putExtra("image",image);
                        next.putExtra("cid",cid);
                        next.putExtra("fav","null");
                        context.startActivity(next);
                        Log.i("vj",jobj.getString("message"));
                    }
                    else{
                        Log.i("vj",jobj.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("cid",cid);
                params.put("uid",uid);
                params.put("iid",iid);
                return params;
            }
        };
        RequestQueue que= Volley.newRequestQueue(context);
        que.add(request);
    }
}
