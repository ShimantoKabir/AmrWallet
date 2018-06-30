package com.example.maask.amrwallet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.maask.amrwallet.Database.IncomingDatabaseSource;
import com.example.maask.amrwallet.Database.OutgoingDatabaseSource;
import com.example.maask.amrwallet.Database.PreviousMonthShowingAdapter;

import java.util.ArrayList;

public class PreviousMonthActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;

    private ArrayList<String> getDistinctIncomingMonth,getDistinctOutgoingMonth;
    private PreviousMonthShowingAdapter distinctIncomingAdaptor,distinctOutgoingAdaptor;

    private IncomingDatabaseSource incomingDatabaseSource;
    private OutgoingDatabaseSource outgoingDatabaseSource;

    ListView show_previous_incoming,show_previous_outgoing;

    SharedPreferences sharedPreferences;
    private static final String PREFERENCES_KEY  = "reg_preferences";
    private static final String USR_START_DATE   = "startDateKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_month);

        sharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);

        show_previous_incoming = findViewById(R.id.show_previous_incoming);
        show_previous_outgoing = findViewById(R.id.show_previous_outgoing);

        toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setTitle("Previous Month");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        incomingDatabaseSource = new IncomingDatabaseSource(this);
        outgoingDatabaseSource = new OutgoingDatabaseSource(this);

        // previous incoming month ...
        getDistinctIncomingMonth = incomingDatabaseSource.getDistinctMonth();

        distinctIncomingAdaptor = new PreviousMonthShowingAdapter(this,getDistinctIncomingMonth,R.layout.show_previous_month);
        show_previous_incoming.setAdapter(distinctIncomingAdaptor);

        show_previous_incoming.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showThisMonth(adapterView.getItemAtPosition(i).toString());
            }
        });

        // previous outgoing month ...

        getDistinctOutgoingMonth = outgoingDatabaseSource.getDistinctMonth();

        distinctOutgoingAdaptor = new PreviousMonthShowingAdapter(this,getDistinctOutgoingMonth,R.layout.show_previous_month);

        show_previous_outgoing.setAdapter(distinctOutgoingAdaptor);
        show_previous_outgoing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showThisMonth(adapterView.getItemAtPosition(i).toString());
            }
        });

    }

    private void showThisMonth(final String month) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Would you like to see " + month + " month data")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(PreviousMonthActivity.this, "May be you are not interested to see this month data ", Toast.LENGTH_SHORT).show();
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(USR_START_DATE, month);
                editor.commit();
                Intent intent = new Intent(PreviousMonthActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        builder.show();
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
                Toast.makeText(this, "About us clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile_id:

                break;
            case android.R.id.home:
                Intent intentMain = new Intent(PreviousMonthActivity.this,MainActivity.class);
                intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentMain);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
