package com.vijay.v_canteen.canteen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.vijay.v_canteen.BuildConfig;
import com.vijay.v_canteen.R;
import com.vijay.v_canteen.util.SharedPreference;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Canteen_add_item extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText name,price,quantity;
    String iname,itype,iprice,iquantity;
    Button insert,viewbtn,cancel;
    Spinner type;
    ImageView image;

    private final int SELECT_PHOTO = 101;
    String selectedImagePath = "";
    final private int REQUEST_CODE_WRITE_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_add_item);
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        UploadService.NAMESPACE = "com.vijay.v_canteen";

        image=findViewById(R.id.cai_image);

        name=findViewById(R.id.cai_name);
        type=findViewById(R.id.cai_type);
        price=findViewById(R.id.cai_price);
        quantity=findViewById(R.id.cai_quantity);

        insert=findViewById(R.id.cai_add);
        viewbtn=findViewById(R.id.cai_view);
        cancel=findViewById(R.id.cai_cancel);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.item_types_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);
        type.setOnItemSelectedListener(this);

        viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Canteen_add_item.this,Canteen_item_recyclerview.class);
                startActivity(i);
                finish();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) + ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE) + ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        String[] permissions = {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions,1000);
                    }else {
                        takePhotoFromCamera();
                    }
                }else {
                    takePhotoFromCamera();
                }
            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validatename() | !validateprie() | !validatequantity() | !validateImage()){
                    return;
                }
                else{
                    iname=name.getText().toString().trim();
                    iprice=price.getText().toString().trim();
                    iquantity=quantity.getText().toString().trim();

                    String cid=SharedPreference.get("cid");
                    Log.i("vjnarayanan",cid);

                    insertitem(iname,itype,iprice,iquantity,cid,selectedImagePath);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back=new Intent(Canteen_add_item.this,Canteen_home.class);
                startActivity(back);
                finish();
            }
        });
    }

    private boolean validateImage() {
        if (selectedImagePath.equals("")) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    private void takePhotoFromCamera() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    private void insertitem(final String iname, final String itype, final String iprice, final String iquantity, final String cid, String selectedImagePath) {
        try {
            Log.i("vjnarayanan",selectedImagePath);
            String image= UUID.randomUUID().toString();
            UploadNotificationConfig config = new UploadNotificationConfig();
            config.getCompleted().autoClear = true;
            config.setTitleForAllStatuses("photo");
            config.setIconForAllStatuses(R.mipmap.ic_launcher);
            new MultipartUploadRequest(getApplicationContext(),image,getText(R.string.url)+"canteen_add_new_item.php")
                    .addFileToUpload(selectedImagePath,"image")
                    .addParameter("item_name",iname)
                    .addParameter("item_type",itype)
                    .addParameter("item_price",iprice)
                    .addParameter("item_quantity",iquantity)
                    .addParameter("canteen_id", cid)

                    .setMaxRetries(5)
                    .setNotificationConfig(config)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                            Log.i("vjnarayanan","on progress upload");
                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

                            Log.i("vjnarayanan","on error upload");
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                            Log.i("vjnarayanan","on completed upload");
                            Log.i("vjnarayanan",serverResponse.getBodyAsString());
                            try {
                                JSONObject json=new JSONObject(serverResponse.getBodyAsString());
                                if (json.getString("status").equals("0")){
                                    Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
                                    Intent next =new Intent(Canteen_add_item.this,Canteen_item_recyclerview.class);
                                    startActivity(next);
//                                    clear();

                                }else {
                                    Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            Log.i("NIK", "onCancelled");
                        }
                    })
                    .startUpload();


        }catch (Exception e){
            e.printStackTrace();
        }




//        StringRequest request=new StringRequest(Request.Method.POST, getString(R.string.url)+"canteen_add_new_item.php", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jobj=new JSONObject(response);
//                    if (jobj.getString("status").equals("0")){
//                        Log.i("vjnarayanana",jobj.getString("message"));
//                        Toast.makeText(Canteen_add_item.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
//                        Intent next =new Intent(Canteen_add_item.this,Canteen_item_recyclerview.class);
//                        startActivity(next);
//                        finish();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        })
//
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params=new HashMap<>();
//                params.put("item_name",iname);
//                params.put("item_type",itype);
//                params.put("item_price",iprice);
//                params.put("item_quantity",iquantity);
//                params.put("canteen_id", cid);
//                return params;
//            }
//        };
//
//        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
//        queue.add(request);
    }

    private boolean validatequantity() {
        if (quantity.getText().toString().trim().equals("")){
            quantity.setError("Enter quantity");
            return false;
        }
        else{
            return true;
        }
    }

    private boolean validateprie() {
        if (price.getText().toString().trim().equals("")){
            price.setError("Enter price");
            return false;
        }
        else{
            return true;
        }
    }

    private boolean validatename() {
        if (name.getText().toString().trim().equals("")){
            name.setError("Enter name");
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        Intent back=new Intent(Canteen_add_item.this,Canteen_home.class);
        startActivity(back);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        itype=parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = data.getData();
                    selectedImagePath = getPath(imageUri);
                    Log.i("vjnarayanan",selectedImagePath);
                    String retrieveImage = selectedImagePath;
                    File newImageFile = new File(retrieveImage);
                    Picasso.with(getApplicationContext()).load(Uri.fromFile(newImageFile)).into(image);
                }
                break;

        }

    }

    public String getPath(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null,
                null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            } else {

            }
        }
    }

}
