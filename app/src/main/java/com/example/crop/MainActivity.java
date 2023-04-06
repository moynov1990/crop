package com.example.crop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class MainActivity extends AppCompatActivity {

    private EditText textFilename;
    private Button btnBrowse, btnUpload;
    private ImageView imageUpload;
    Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageUpload = findViewById(R.id.img);
        textFilename = findViewById(R.id.editTextFilename);
        btnBrowse = findViewById(R.id.buttonBrowse);
        btnUpload = findViewById(R.id.buttonUpload);

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
                imageUpload.setImageURI(mImageUri);
            }

            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception e = result.getError();
                Toast.makeText(this,"Error: "+ e, Toast.LENGTH_SHORT).show();
            }
        }
    }
}












