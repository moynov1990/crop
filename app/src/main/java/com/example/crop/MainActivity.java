package com.example.crop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText textFilename;
    private Button btnBrowse, btnUpload;
    private ImageView imageUpload;
    private String URL ="http://192.168.2.67/LoginRegister/saveimage.php";
    private Uri mImageUri;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageUpload = findViewById(R.id.img);
        textFilename = findViewById(R.id.editTextFilename);
        btnBrowse = findViewById(R.id.buttonBrowse);
        btnUpload = findViewById(R.id.buttonUpload);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                if (bitmap !=null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    String encodeImage = android.util.Base64.encodeToString(bytes, Base64.DEFAULT);
                    StringRequest request2 = new StringRequest(Request.Method.POST, URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response2) {
                                    if (response2.equals("success")) {
                                        imageUpload.setImageResource(R.drawable.browse_image);
                                        textFilename.setText("");
                                        Toast.makeText(MainActivity.this, "success", Toast.LENGTH_LONG).show();
                                    } else if (response2.equals("failed to insert to Database")) {
                                        Toast.makeText(MainActivity.this, "failed to insert to Database", Toast.LENGTH_LONG).show();
                                    } else if (response2.equals("failed to upload image")) {
                                        Toast.makeText(MainActivity.this, "failed to upload image", Toast.LENGTH_LONG).show();
                                    } else if (response2.equals("no image found")) {
                                        Toast.makeText(MainActivity.this, "no image found", Toast.LENGTH_LONG).show();
                                    } else if (response2.equals("Database connetion failed")) {
                                        Toast.makeText(MainActivity.this, "Database connetion failed", Toast.LENGTH_LONG).show();
                                    } else if (response2.equals("failure")) {
                                        Toast.makeText(MainActivity.this, "failure", Toast.LENGTH_LONG).show();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error2) {
                            Toast.makeText(getApplicationContext(), error2.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        protected Map<String, String> getParams() throws AuthFailureError {
                            String name1 = textFilename.getText().toString().trim();
                            Map<String, String> map = new HashMap<>();
                            map.put("name", name1);
                            map.put("upload", encodeImage);
                            return map;
                        }
                    };
                    RequestQueue queue2 = Volley.newRequestQueue(getApplicationContext());
                    queue2.add(request2);
                } else Toast.makeText(getApplicationContext(), "оберіть зображення", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void onChoseFile (View v) {

        CropImage.activity().start(MainActivity.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                try {
                   bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageUpload.setImageBitmap(bitmap);
            }

            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception e = result.getError();
                Toast.makeText(this,"Error: "+ e, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
