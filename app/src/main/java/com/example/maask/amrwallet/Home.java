package com.example.maask.amrwallet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.maask.amrwallet.Database.IncomingDatabaseSource;
import com.example.maask.amrwallet.Database.OutgoingDatabaseSource;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.Calendar;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

/**
 * Created by Maask on 12/25/2017.
 */

public class Home extends Fragment {

    private IncomingDatabaseSource incomingDatabaseSource;
    private OutgoingDatabaseSource outgoingDatabaseSource;
    HorizontalBarChart horizontalBarChart;
    String moneyField[] = {"In","Out","Box"};

    String chk_month_session,user_name;

    SharedPreferences sharedPreferences;
    private static final String PREFERENCES_KEY  = "reg_preferences";
    private static final String USR_START_DATE   = "startDateKey";
    private static final String USR_NAME         = "userNameKey";

    private float totalIncomingAmount;
    private float totalOutgoingAmount;
    private float depositBox;

    private float EntireIncomingAmount;
    private float EntireOutgoingAmount;
    private float EntireDepositBox;

    TextView deposit_box,total_outgoing_money,total_incoming_money,show_date;

    public static final int[] MY_COLORS = {rgb("#00695C"), rgb("#f42851"), rgb("#f1c40f")};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_home,container,false);

        show_date = myView.findViewById(R.id.show_date);
        sharedPreferences = getActivity().getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        chk_month_session = sharedPreferences.getString(USR_START_DATE,"");
        user_name = sharedPreferences.getString(USR_NAME,"");
        show_date.setText(user_name+" - "+chk_month_session);

        deposit_box = myView.findViewById(R.id.deposit_box);
        total_outgoing_money = myView.findViewById(R.id.total_outgoing_money);
        total_incoming_money = myView.findViewById(R.id.total_incoming_money);

        incomingDatabaseSource = new IncomingDatabaseSource(getActivity());
        totalIncomingAmount = incomingDatabaseSource.getAllIncomingMoneyOnlyAmount(chk_month_session);
        String convertTotalIncomingAmountToString = String.valueOf(totalIncomingAmount);

        outgoingDatabaseSource = new OutgoingDatabaseSource(getActivity());
        totalOutgoingAmount = outgoingDatabaseSource.getAllOutgoingMoneyOnlyAmount(chk_month_session);
        String convertTotalOutgoingAmountToString = String.valueOf(totalOutgoingAmount);

        setNotification(totalIncomingAmount,totalOutgoingAmount);
        depositBox = totalIncomingAmount - totalOutgoingAmount;
        String convertDepositBoxToString = String.valueOf(depositBox);

        deposit_box.setText(convertDepositBoxToString + " TK");
        total_outgoing_money.setText(convertTotalOutgoingAmountToString + " TK");
        total_incoming_money.setText(convertTotalIncomingAmountToString + " TK");

        setUpPieChart(myView,totalIncomingAmount,totalOutgoingAmount,depositBox);

        // entire calculation
        EntireIncomingAmount = incomingDatabaseSource.getEntireIncomingMoneyOnlyAmount();
        EntireOutgoingAmount = outgoingDatabaseSource.getEntireOutgoingMoneyOnlyAmount();
        EntireDepositBox = EntireIncomingAmount - EntireOutgoingAmount;

        setUpHorizontalBarChart(myView,EntireIncomingAmount,EntireOutgoingAmount,EntireDepositBox);

        return myView;
    }

    private void setUpPieChart(View view, float totalIncomingAmount, float totalOutgoingAmount, float moneyAmount) {

        ArrayList<PieEntry> entryList = new ArrayList();


        entryList.add(new PieEntry(totalIncomingAmount,moneyField[0]));
        entryList.add(new PieEntry(totalOutgoingAmount,moneyField[1]));
        entryList.add(new PieEntry(moneyAmount,moneyField[2]));


        PieDataSet dataSet = new PieDataSet(entryList,"");
        dataSet.setColors(MY_COLORS);
        PieData data = new PieData(dataSet);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(10f);

        // set data to chart

        PieChart chart = view.findViewById(R.id.pie_chart);
        chart.getDescription().setEnabled(false);
        chart.setUsePercentValues(true);
        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setCenterText("Pie Chart");
        chart.setCenterTextColor(Color.GRAY);
        chart.setCenterTextSize(10);
        chart.setData(data);
        chart.animateX(500);
        chart.invalidate();

    }

    private void setUpHorizontalBarChart(View myView, float totalIncomingAmount, float totalOutgoingAmount, float moneyAmount) {

        ArrayList<BarEntry> barEntries = new ArrayList<>();

        horizontalBarChart = myView.findViewById(R.id.horizontal_bar_chart);
        horizontalBarChart.getDescription().setEnabled(false);

        barEntries.add(new BarEntry(0,totalIncomingAmount));
        barEntries.add(new BarEntry(1,totalOutgoingAmount));
        barEntries.add(new BarEntry(2,moneyAmount));


        BarDataSet barDataSet = new BarDataSet(barEntries,"");
        barDataSet.setColors(MY_COLORS);
        BarData barData = new BarData(barDataSet);
        horizontalBarChart.setData(barData);

        horizontalBarChart.animateY(3000);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){

            getFragmentManager().beginTransaction().detach(this).attach(this).commit();

        }
    }

    private void setNotification(float tim, float tom) {

        float db = tim - tom;
        String depositBox = String.valueOf(db);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,12);
        cal.set(Calendar.MINUTE,30);
        cal.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(getActivity(), AlarmReceiver.class);
        notificationIntent.putExtra("depositBox",depositBox);
        notificationIntent.putExtra("income",String.valueOf(tim));
        notificationIntent.putExtra("expense",String.valueOf(tom));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }


}
