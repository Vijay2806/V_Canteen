package com.vijay.v_canteen.admin;

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
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class Admin_add_canteen extends AppCompatActivity {
    EditText name, pass, conpass, mobile, landline, email;
    Button add, cancel;
    String cname, cpass, cconpass, cmobile, clandline, cemail;
    ImageView canteenimage;


    private final int SELECT_PHOTO = 101;
    String selectedImagePath = "";
    final private int REQUEST_CODE_WRITE_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_canteen);
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        UploadService.NAMESPACE = "com.vijay.v_canteen";


        name = findViewById(R.id.ac_name);
        pass = findViewById(R.id.ac_password);
        conpass = findViewById(R.id.ac_confirm_password);
        mobile = findViewById(R.id.ac_mobile);
        landline = findViewById(R.id.ac_landline);
        email = findViewById(R.id.ac_email);

        add = findViewById(R.id.add_new_canteen);
        cancel = findViewById(R.id.add_new_canteen_cancel);

        canteenimage = findViewById(R.id.ac_img);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Admin_add_canteen.this, Admin_home.class);
                startActivity(i);
                finish();
            }
        });

        /*canteenimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("NIKHIL", "Image clicked");
            }
        });*/

        canteenimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)  + ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE) + ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        String[] permissions = {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions,1000);
                    }else {
                        takePhotoFromCamera();
                    }
                }else {
                    takePhotoFromCamera();
                }


//                Toast.makeText(getApplicationContext(), "Image clicked", Toast.LENGTH_LONG).show();
//                Log.i("vjnarayanan","Image view clicked");
//                int hasWriteStoragePermission = 0;
//
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                    hasWriteStoragePermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
//                }
//
//                if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                                REQUEST_CODE_WRITE_STORAGE);
//                    }
////return;
//                } else {
//                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                    photoPickerIntent.setType("image/*");
//                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
//                }

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatename() | !validatepass() | !validateconpass() | !validatemobile() | !validatelandline() | !validateemail() ||  !validateImage()) {
                    return;
                } else {
                    cname = name.getText().toString().trim();
                    cpass = pass.getText().toString().trim();
                    cconpass = conpass.getText().toString().trim();
                    cmobile = mobile.getText().toString().trim();
                    clandline = landline.getText().toString().trim();
                    cemail = email.getText().toString().trim();

                    addcanteen(cname, cpass, cconpass, cmobile, clandline, cemail,selectedImagePath);

                }
            }
        });


    }

    private void takePhotoFromCamera() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);

    }

    /*public void testing(View view){
        Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_LONG).show();
    }*/

    private void addcanteen(final String cname, final String cpass, final String cconpass, final String cmobile, final String clandline, final String cemail, String selectedImagePath) {
        try {
            Log.i("vjnarayanan",selectedImagePath);
            String image= UUID.randomUUID().toString();
            UploadNotificationConfig config = new UploadNotificationConfig();
            config.getCompleted().autoClear = true;
            config.setTitleForAllStatuses("photo");
            config.setIconForAllStatuses(R.mipmap.ic_launcher);
            new MultipartUploadRequest(getApplicationContext(),image,getText(R.string.url)+"admin_add_canteen.php")
                    .addFileToUpload(selectedImagePath,"image")
                    .addParameter("name",cname)
                    .addParameter("email",cemail)
                    .addParameter("pass",cpass)
                    .addParameter("mobile",cmobile)
                    .addParameter("landline",clandline)

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
                                    Intent next=new Intent(Admin_add_canteen.this,Admin_manage_canteen.class);
                                    startActivity(next);
                                    clear();

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




    }

    private void clear() {
        name.setText("");
        email.setText("");
        pass.setText("");
        conpass.setText("");
        mobile.setText("");
        landline.setText("");
        canteenimage.setImageResource(0);
        canteenimage.setImageResource(R.drawable.ic_image_black_24dp);
    }


//        StringRequest request = new StringRequest(Request.Method.POST, getString(R.string.url) + "admin_add_canteen.php", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jobj = new JSONObject(response);
//                    Log.i("vjnarayanan", response);
//                    if (jobj.getString("status").equals("0")) {
//                        Toast.makeText(Admin_add_canteen.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent(Admin_add_canteen.this, Admin_manage_canteen.class);
//                        startActivity(i);
//                        finish();
//                    } else if (jobj.getString("status").equals("1")) {
//                        Toast.makeText(Admin_add_canteen.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(Admin_add_canteen.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> parama = new HashMap<>();
//                parama.put("name", cname);
//                parama.put("pass", cpass);
//                parama.put("mobile", cmobile);
//                parama.put("landline", clandline);
//                parama.put("email", cemail);
//                return parama;
//            }
//        };
//        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//        queue.add(request);
//    }

    private boolean validateemail() {
        if (email.getText().toString().trim().equals("")) {
            email.setError("Enter email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
            email.setError("Enter valid email");
            return false;
        } else
            return true;

    }

    private boolean validatelandline() {
        if (landline.getText().toString().trim().equals("")) {
            landline.setError("Enter Landline ext");
            return false;
        } else
            return true;
    }

    private boolean validatemobile() {
        if (mobile.getText().toString().trim().equals("")) {
            mobile.setError("Enter mobile");
            return false;
        } else
            return true;
    }

    private boolean validateconpass() {
        if (conpass.getText().toString().trim().equals("")) {
            conpass.setError("Enter password again");
            return false;
        } else if (!conpass.getText().toString().trim().equals(pass.getText().toString().trim())) {
            conpass.setError("password does not match");
            clearconpass();
            return false;
        } else
            return true;
    }

    private boolean validatepass() {
        if (pass.getText().toString().trim().equals("")) {
            pass.setError("Enter password");
            return false;
        } else
            return true;
    }

    private boolean validateImage() {
        if (selectedImagePath.equals("")) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }



    private void clearconpass() {
        conpass.setText("");
    }

//    private boolean validateid() {
//        if (id.getText().toString().trim().equals(""))
//            return false;
//        else
//            return true;
//    }

    private boolean validatename() {
        if (name.getText().toString().trim().equals("")) {
            name.setError("Enter name");
            return false;
        } else
            return true;
    }




    @Override
    public void onBackPressed() {
        Intent i = new Intent(Admin_add_canteen.this, Admin_home.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = data.getData();
                    selectedImagePath = getPath(imageUri);
                    Log.i("vicky",selectedImagePath);
                    String retrieveImage = selectedImagePath;
                    File newImageFile = new File(retrieveImage);
                    Picasso.with(getApplicationContext()).load(Uri.fromFile(newImageFile)).into(canteenimage);
                }
                break;

        }

    }

    public String getPath(Uri uri) {
// just some safety built in
        if (uri == null) {
            return null;
        }
// try to retrieve the image from the media store first
// this will only work for images selected from gallery
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
// this is our fallback here
        return uri.getPath();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
// photoPickerIntent.setType("image/*");

                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            } else {

            }
        }
    }
}
