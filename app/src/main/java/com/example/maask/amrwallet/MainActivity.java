package com.example.maask.amrwallet;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.ViewPagerOnTabSelectedListener;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.maask.amrwallet.Database.IncomingDatabaseSource;
import com.example.maask.amrwallet.Database.IncomingMoney;
import com.example.maask.amrwallet.Database.OutgoingDatabaseSource;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements
        MoneyIn.TotalIncomingDataChangeForToolbarTitle,MoneyOut.TotalOutgoingDataChangeForToolbarTitle{


    private IncomingDatabaseSource incomingDatabaseSource;
    private OutgoingDatabaseSource outgoingDatabaseSource;

    SharedPreferences sharedPreferences;
    private static final String PREFERENCES_KEY  = "reg_preferences";
    private static final String USR_START_DATE   = "startDateKey";
    private static final String CHK_REG          = "checkUserRegOrNot";
    private static final String USR_NAME         = "userNameKey";
    private static final String USR_PASS         = "userPassKey";
    private static final String USR_IMG_PATH     = "imagePathKey";

    private float totalIncomingAmount;
    private float totalOutgoingAmount;
    private float depositBox;

    private String chk_month_session;

    android.support.v7.widget.Toolbar toolbar;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    FloatingActionMenu floatingActionMenu;
    FloatingActionButton logout,start_new_month;

    private Calendar calendar;
    private int year,month,day;

    private ArrayList<IncomingMoney> objects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);

        if (!sharedPreferences.getString(CHK_REG, "").equals("Y")) {

            final Intent intentChkReg = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(intentChkReg);

        } else {

            chk_month_session = sharedPreferences.getString(USR_START_DATE, "");

            floatingActionMenu = findViewById(R.id.floating_action_menu);
            logout = findViewById(R.id.logout);
            start_new_month = findViewById(R.id.start_new_month);

            start_new_month.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startNewMonth();
                }
            });

            chk_month_session = sharedPreferences.getString(USR_START_DATE, "");

            incomingDatabaseSource = new IncomingDatabaseSource(this);
            totalIncomingAmount = incomingDatabaseSource.getAllIncomingMoneyOnlyAmount(chk_month_session);


            outgoingDatabaseSource = new OutgoingDatabaseSource(this);
            totalOutgoingAmount = outgoingDatabaseSource.getAllOutgoingMoneyOnlyAmount(chk_month_session);


            depositBox = totalIncomingAmount - totalOutgoingAmount;
            String convertDepositBoxToString = String.valueOf(depositBox);


            toolbar = findViewById(R.id.custom_toolbar);
            toolbar.setTitle("Balance " + convertDepositBoxToString + " TK");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            mViewPager = findViewById(R.id.containerPage);
            mViewPager.setAdapter(mSectionsPagerAdapter);


            TabLayout tabLayout = findViewById(R.id.tabs);

            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new ViewPagerOnTabSelectedListener(mViewPager));

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    logOut();
                }
            });

            start_new_month.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startNewMonth();
                }
            });

        }

    }


    DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(year,month,dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM/yyyy");
            String userDate = sdf.format(calendar.getTime());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(USR_START_DATE, userDate);
            editor.commit();

            Toast.makeText(MainActivity.this, "Sir/Mam your newly selected month is = " + userDate, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    };

    private boolean startNewMonth() {

        calendar = Calendar.getInstance();
        year     = calendar.get(Calendar.YEAR);
        month    = calendar.get(Calendar.MONTH);
        day      = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,dateListener,year,month,day);
        datePickerDialog.show();

        return true;
    }

    private void logOut() {

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.user_guide_line:
                Toast.makeText(this, "User Guide Line", Toast.LENGTH_SHORT).show();
                break;
            case R.id.previous_month:
                Intent intentPreviousMonth = new Intent(MainActivity.this,PreviousMonthActivity.class);
                startActivity(intentPreviousMonth);
                break;
            case R.id.profile_id:

                showProfile();

                break;
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showProfile() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.facebook_fetch_data,null);

        ImageView profileImg = view.findViewById(R.id.fb_img);
        final EditText profileName = view.findViewById(R.id.fb_name);
        Button goEdit = view.findViewById(R.id.fb_reg);
        final ProgressBar fbImgLoader = view.findViewById(R.id.fb_img_loader);

        String usrName = sharedPreferences.getString(USR_NAME, "");
        String usrFacebookId = sharedPreferences.getString(USR_PASS, "");

        profileName.setText(usrName);

        if (sharedPreferences.getString(USR_IMG_PATH, "").equals("N")){

            try {

                URL profilePicUrl = new URL("https://graph.facebook.com/"+usrFacebookId+"/picture?width=120&height=120");
                Picasso.with(this)
                .load(profilePicUrl.toString())
                .into(profileImg, new Callback() {
                    @Override
                    public void onSuccess() {
                        fbImgLoader.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }else {
            fbImgLoader.setVisibility(View.GONE);
            byte[] getImageByteCode = Base64.decode(sharedPreferences.getString(USR_IMG_PATH,""), Base64.DEFAULT);
            Bitmap getConvertedImageByteCode = BitmapFactory.decodeByteArray(getImageByteCode, 0, getImageByteCode.length);
            profileImg.setImageBitmap(getConvertedImageByteCode);
        }

        builder.setView(view);
        builder.show();

        goEdit.setText("EDIT");
        goEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sharedPreferences.getString(USR_IMG_PATH, "").equals("N")){
                    Intent intent = new Intent(MainActivity.this,RegistrationActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "WARNING : Sir/Mam you can not edit your profile because you are registered by Facebook !", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onInDataChange() {

        float tim = incomingDatabaseSource.getAllIncomingMoneyOnlyAmount(chk_month_session);
        float tom = outgoingDatabaseSource.getAllOutgoingMoneyOnlyAmount(chk_month_session);
        float db = tim - tom;
        toolbar.setTitle("Balance " + String.valueOf(db) + " TK");

    }

    @Override
    public void onOutDataChange() {

        float tim = incomingDatabaseSource.getAllIncomingMoneyOnlyAmount(chk_month_session);
        float tom = outgoingDatabaseSource.getAllOutgoingMoneyOnlyAmount(chk_month_session);
        float db = tim - tom;
        toolbar.setTitle("Balance " + String.valueOf(db) + " TK");

    }

}
