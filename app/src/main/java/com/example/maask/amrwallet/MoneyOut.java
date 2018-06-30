package com.example.maask.amrwallet;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maask.amrwallet.Database.IncomingDatabaseSource;
import com.example.maask.amrwallet.Database.OutgoingDatabaseSource;
import com.example.maask.amrwallet.Database.OutgoingMoney;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * Created by Maask on 12/24/2017.
 */

public class MoneyOut extends Fragment {


    private RecyclerView recyclerView;
    private ArrayList<OutgoingMoney> objects = new ArrayList<>();
    private OutgoingDatabaseSource outgoingDatabaseSource;
    private OutgoingMoneyAdapter outgoingMoneyAdapter;

    Button add_new_outgoing_money;
    String getInDate,getInName,getInAmount;
    EditText takeName,takeAmount;
    TextView takeTitle,takeDate,takeTime;

    SharedPreferences sharedPreferences;
    private static final String PREFERENCES_KEY  = "reg_preferences";
    private static final String USR_START_DATE   = "startDateKey";

    String chk_month_session;

    String currentDate = new SimpleDateFormat("dd").format(Calendar.getInstance().getTime());
    String currentTime = new SimpleDateFormat("hh:mm:ss a").format(Calendar.getInstance().getTime());

    TotalOutgoingDataChangeForToolbarTitle dataChanger;

    ArrayList<Integer> OutgoingMoneyColId;
    ArrayList<String> singleRowInfo;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.fragment_money_out,container,false);

        recyclerView = myView.findViewById(R.id.rv_for_show_outgoing_money);
        add_new_outgoing_money = myView.findViewById(R.id.add_new_outgoing_money);

        sharedPreferences = getActivity().getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        chk_month_session = sharedPreferences.getString(USR_START_DATE,"");

        add_new_outgoing_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOutgoingMoney();
            }
        });

        outgoingDatabaseSource = new OutgoingDatabaseSource(getActivity());
        objects = outgoingDatabaseSource.getAllOutgoingMoney(chk_month_session);
        outgoingMoneyAdapter = new OutgoingMoneyAdapter(getActivity(),objects);

        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(outgoingMoneyAdapter);

        // delete action ...

        outgoingMoneyAdapter.setOnDeleteIconClickListener(new OutgoingMoneyAdapter.OnDeleteIconClickListener() {
            @Override
            public void onDeleteClick(final int rowId) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("WARNING : Sir/Mam are you sure want to delete this ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        OutgoingMoneyColId = outgoingDatabaseSource.getAllColumnId(chk_month_session);

                        Collections.reverse(OutgoingMoneyColId);

                        singleRowInfo = outgoingDatabaseSource.getSingleRowInfoById(chk_month_session,OutgoingMoneyColId.get(rowId));

                        boolean status = outgoingDatabaseSource.deleteOutgoingTransaction(OutgoingMoneyColId.get(rowId));

                        if (status == true){

                            notifyDataChange();
                            objects = outgoingDatabaseSource.getAllOutgoingMoney(chk_month_session);
                            outgoingMoneyAdapter.instantDataChang(objects);
                            Toast.makeText(getActivity(), "SUCCESS : Money deleted successfully !", Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(getActivity(), "ERROR : Money did not deleted successfully !", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).setNegativeButton("Cancle",null);
                builder.show();



            }
        });

        // edit action ...

        outgoingMoneyAdapter.setOnEditIconClickListener(new OutgoingMoneyAdapter.OnEditIconClickListener() {
            @Override
            public void onEditClick(final int rowId) {

                OutgoingMoneyColId = outgoingDatabaseSource.getAllColumnId(chk_month_session);

                Collections.reverse(OutgoingMoneyColId);

                singleRowInfo = outgoingDatabaseSource.getSingleRowInfoById(chk_month_session,OutgoingMoneyColId.get(rowId));

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater layoutInflater = getLayoutInflater();
                View view = layoutInflater.inflate(R.layout.take_transection,null);

                takeName   = view.findViewById(R.id.take_tan_name);
                takeDate   = view.findViewById(R.id.trans_date);
                takeTime   = view.findViewById(R.id.trans_time);
                takeAmount = view.findViewById(R.id.take_tan_amount);
                takeTitle = view.findViewById(R.id.trans_title);

                takeTitle.setText("Edit this entry");
                takeName.setText(singleRowInfo.get(0));
                takeDate.setText("Date : "+singleRowInfo.get(1));
                takeTime.setText("Time : "+singleRowInfo.get(2));
                takeAmount.setText(singleRowInfo.get(3));

                builder.setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        getInName = takeName.getText().toString();
                        getInAmount = takeAmount.getText().toString();

                        boolean status = outgoingDatabaseSource.updateSingleRow(OutgoingMoneyColId.get(rowId),getInName,singleRowInfo.get(1),singleRowInfo.get(2),Float.parseFloat(getInAmount));

                        if (status== true){

                            notifyDataChange();

                            objects = outgoingDatabaseSource.getAllOutgoingMoney(chk_month_session);
                            outgoingMoneyAdapter.instantDataChang(objects);
                            Toast.makeText(getActivity(), "SUCCESS : Update Successful !", Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(getActivity(), "ERROR : Update Unsuccessful !", Toast.LENGTH_SHORT).show();
                        }


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "May be you are not interested to update this row", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();

            }
        });

        return myView;
    }

    private void addOutgoingMoney() {

        OutgoingDatabaseSource ods = new OutgoingDatabaseSource(getActivity());
        IncomingDatabaseSource ids = new IncomingDatabaseSource(getActivity());
        final float tom = ods.getAllOutgoingMoneyOnlyAmount(chk_month_session);
        final float tim = ids.getAllIncomingMoneyOnlyAmount(chk_month_session);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.take_transection, null);

        takeName = view.findViewById(R.id.take_tan_name);
        takeDate = view.findViewById(R.id.trans_date);
        takeTime = view.findViewById(R.id.trans_time);
        takeAmount = view.findViewById(R.id.take_tan_amount);
        takeTitle = view.findViewById(R.id.trans_title);

        takeTitle.setText("Entry new outgoing money");
        takeDate.setText("Date : " + currentDate + "/" + chk_month_session);
        takeTime.setText("Time : " + currentTime);

        builder.setView(view)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                getInName = takeName.getText().toString();
                getInDate = takeDate.getText().toString();
                getInAmount = takeAmount.getText().toString();

                if (getInName.isEmpty() || getInDate.isEmpty() || getInAmount.isEmpty()) {

                    Toast.makeText(getActivity(), "ERROR : Field must not be empty ! ", Toast.LENGTH_SHORT).show();

                } else {

                    float getNewOutAmt = Float.parseFloat(getInAmount);

                    float depositBox = tim - tom - getNewOutAmt;

                    if (depositBox<0){
                        Toast.makeText(getActivity(), "WARNING : Sir/Mam your newly expense cross your deposit box ... !", Toast.LENGTH_SHORT).show();
                    }else {

                        boolean status = outgoingDatabaseSource.insertOutgoingTransactions(new OutgoingMoney(getInName, currentDate + "/" + chk_month_session, Float.valueOf(getInAmount), currentTime, chk_month_session));

                        if (status == true) {

                            notifyDataChange();
                            objects = outgoingDatabaseSource.getAllOutgoingMoney(chk_month_session);
                            outgoingMoneyAdapter.instantDataChang(objects);

                            Toast.makeText(getActivity(), "SUCCESS : Money expense successfully !", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getActivity(), "ERROR : Money did not expense successfully !", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            }
        })
        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(), "May be you do not have any transaction", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();

    }

    public interface TotalOutgoingDataChangeForToolbarTitle {
        void onOutDataChange();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataChanger = (TotalOutgoingDataChangeForToolbarTitle) context;
    }

    public void notifyDataChange() {
        dataChanger.onOutDataChange();
    }
}
