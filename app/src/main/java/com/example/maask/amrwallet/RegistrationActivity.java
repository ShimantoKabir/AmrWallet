package com.example.maask.amrwallet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class RegistrationActivity extends AppCompatActivity {

    EditText usr_name,usr_password;
    ImageView usr_img;
    Button reg;

    private static final int PICK_IMAGE = 100;
    Uri imgUri;

    private static final String PREFERENCES_KEY  = "reg_preferences";
    private static final String USR_NAME         = "userNameKey";
    private static final String USR_PASS         = "userPassKey";
    private static final String USR_START_DATE   = "startDateKey";
    private static final String USR_IMG_PATH     = "imagePathKey";
    private static String CHK_REG                = "checkUserRegOrNot";

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        usr_name       = findViewById(R.id.usr_name);
        usr_password   = findViewById(R.id.usr_password);
        usr_img        = findViewById(R.id.usr_img);
        reg            = findViewById(R.id.reg);

        sharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);

        usr_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfo();
                Toast.makeText(RegistrationActivity.this, "SUCCESS : User information saved !", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegistrationActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        if (sharedPreferences.getString(CHK_REG,"").equals("Y")){

            usr_name.setText(sharedPreferences.getString(USR_NAME,""));
            usr_password.setText(sharedPreferences.getString(USR_PASS,""));
            byte[] getImageByteCode = Base64.decode(sharedPreferences.getString(USR_IMG_PATH,""), Base64.DEFAULT);
            Bitmap getConvertedImageByteCode = BitmapFactory.decodeByteArray(getImageByteCode, 0, getImageByteCode.length);
            usr_img.setImageBitmap(getConvertedImageByteCode);

        }

    }

    private void saveUserInfo() {

        String currentDate = new SimpleDateFormat("MMM/yyyy").format(Calendar.getInstance().getTime());
        String getUsername   = usr_name.getText().toString();
        String getPass       = usr_password.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USR_NAME, getUsername);
        editor.putString(USR_PASS, getPass);
        editor.putString(USR_START_DATE, currentDate);
        editor.putString(CHK_REG, "Y");
        editor.commit();

    }

    public void openGallery(){

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent,PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode == RESULT_OK && requestCode == PICK_IMAGE ){

            imgUri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(imgUri);

                Bitmap realImage = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                realImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(USR_IMG_PATH, encodedImage);
                editor.commit();

                byte[] getImageByteCode = Base64.decode(sharedPreferences.getString(USR_IMG_PATH,""), Base64.DEFAULT);

                Bitmap getConvertedImageByteCode = BitmapFactory.decodeByteArray(getImageByteCode, 0, getImageByteCode.length);

                usr_img.setImageBitmap(getConvertedImageByteCode);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }else{
            Toast.makeText(this, "ERROR : Some Thing went wrong !", Toast.LENGTH_SHORT).show();
        }

    }

}
