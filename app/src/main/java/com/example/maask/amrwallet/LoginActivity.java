package com.example.maask.amrwallet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {

    private Button go_dashboard,go_reg;
    EditText usr_name,usr_pass;
    String get_acc_email,get_acc_id;
    CheckBox remember_pass;
    TextView forgot_pass;

    private LoginButton fbLoginBT;
    private CallbackManager callbackManager;

    private TextView or;
    private ImageView proImgIV;

    private static final String PREFERENCES_KEY  = "reg_preferences";
    private static final String USR_NAME         = "userNameKey";
    private static final String USR_PASS         = "userPassKey";
    private static final String USR_START_DATE   = "startDateKey";
    private static final String REM_PASS         = "remPassKey";
    private static final String REM_NAME         = "remNameKey";
    private static final String CHK_REG          = "checkUserRegOrNot";
    private static final String USR_IMG_PATH     = "imagePathKey";

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE);

        usr_name = findViewById(R.id.usr_name);
        usr_pass = findViewById(R.id.usr_pass);

        go_dashboard = findViewById(R.id.go_pro);
        go_reg = findViewById(R.id.go_reg);

        forgot_pass = findViewById(R.id.forgot_password);
        remember_pass = findViewById(R.id.remember_pass);

        proImgIV = findViewById(R.id.pro_img);

        or = findViewById(R.id.or);

        remember_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (remember_pass.isChecked()) {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(REM_NAME, usr_name.getText().toString());
                    editor.putString(REM_PASS, usr_pass.getText().toString());
                    editor.commit();

                }

            }
        });

        if (sharedPreferences.getString(CHK_REG, "").equals("Y")) {
            usr_name.setText(sharedPreferences.getString(REM_NAME, ""));
            usr_pass.setText(sharedPreferences.getString(REM_PASS, ""));
        }

        go_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                get_acc_email = usr_name.getText().toString();
                get_acc_id = usr_pass.getText().toString();

                if (get_acc_email.isEmpty() && get_acc_id.isEmpty()) {

                    Toast.makeText(LoginActivity.this, "Field must not be empty!", Toast.LENGTH_LONG).show();

                } else {

                    if (usr_name.getText().toString().equals(sharedPreferences.getString(USR_NAME, "")) &&
                            usr_pass.getText().toString().equals(sharedPreferences.getString(USR_PASS, ""))) {

                        Intent intentDashboard = new Intent(LoginActivity.this, MainActivity.class);
                        intentDashboard.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentDashboard);

                    } else {
                        Toast.makeText(LoginActivity.this, "ERROR : Username and Password not match !", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        go_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sharedPreferences = getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE);

                if (!sharedPreferences.getString(CHK_REG, "").equals("Y")) {

                    Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                    startActivity(intent);

                } else {

                    checkRegAuthentication();

                }
            }
        });

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                forgotPassword();

            }
        });

        fbLoginBT = findViewById(R.id.facebook_login);
        callbackManager = CallbackManager.Factory.create();
        fbLoginBT.setReadPermissions(Arrays.asList("public_profile","email"));
        fbLoginBT.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        getFacebookData(object);
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields","id,first_name,last_name,email");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


    }

    private void getFacebookData(final JSONObject object) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.facebook_fetch_data,null);

        ImageView fbProfileImg = view.findViewById(R.id.fb_img);
        final EditText fbName = view.findViewById(R.id.fb_name);
        Button goDashBoard = view.findViewById(R.id.fb_reg);
        final ProgressBar fbImgLoader = view.findViewById(R.id.fb_img_loader);

        try {

            fbName.setText(object.getString("first_name"));
            URL profilePicUrl = new URL("https://graph.facebook.com/"+object.getString("id")+"/picture?width=120&height=120");

            Picasso.with(this)
            .load(profilePicUrl.toString())
            .into(fbProfileImg, new Callback() {
                @Override
                public void onSuccess() {
                    fbImgLoader.setVisibility(View.GONE);
                }

                @Override
                public void onError() {

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }

        goDashBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String currentDate = new SimpleDateFormat("MMM/yyyy").format(Calendar.getInstance().getTime());

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(USR_NAME, fbName.getText().toString());
                editor.putString(USR_IMG_PATH, "N");
                try {
                    editor.putString(USR_PASS, object.getString("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editor.putString(USR_START_DATE, currentDate);
                editor.putString(CHK_REG, "Y");
                editor.commit();

                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

        builder.setView(view);
        builder.show();

    }


    private void forgotPassword() {

        AlertDialog.Builder forgotPassBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflaterForgotPassword = this.getLayoutInflater();
        View attachView = inflaterForgotPassword.inflate(R.layout.layout_forgot_password,null);

        final EditText oldPass = attachView.findViewById(R.id.old_pass);
        final EditText newPass = attachView.findViewById(R.id.new_pass);

        forgotPassBuilder.setView(attachView).setTitle("Recover Password")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (oldPass.getText().toString().isEmpty() || newPass.getText().toString().isEmpty()){
                            Toast.makeText(LoginActivity.this, "ERROR : Filed must not be empty !", Toast.LENGTH_SHORT).show();
                        }else {

                            if ( oldPass.getText().toString().equals(sharedPreferences.getString(USR_PASS,"")) ){
                                SharedPreferences.Editor setNewPass = sharedPreferences.edit();
                                setNewPass.putString(USR_PASS,newPass.getText().toString());
                                setNewPass.commit();
                                Toast.makeText(LoginActivity.this, "SUCCESS : Password had been changed !", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(LoginActivity.this, "ERROR : Old password did not match !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(LoginActivity.this, "May be you are not interested to change the password !", Toast.LENGTH_SHORT).show();
            }
        });

        forgotPassBuilder.show();

    }

    private void checkRegAuthentication() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.layout_edit_user_info_dialog,null);

        final EditText passET = view.findViewById(R.id.dialog_pass);

        builder.setView(view).setTitle("Registration Authentication")
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (passET.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "ERROR : Filed must not be empty !", Toast.LENGTH_SHORT).show();
                }else {
                    if (passET.getText().toString().equals(sharedPreferences.getString(USR_PASS,""))){

                        Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
                        startActivity(intent);

                    }else{
                        Toast.makeText(LoginActivity.this, "ERROR : Password not match !", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        })
        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(LoginActivity.this, "May be you are not interested to edit your information", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}