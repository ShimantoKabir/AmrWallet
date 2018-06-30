package com.example.maask.amrwallet;

/**
 * Created by Maask on 1/5/2018.
 */

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import com.example.maask.amrwallet.Database.OutgoingMoney;

import java.util.ArrayList;

/**
 * Created by Maask on 12/28/2017.
 */

public class OutgoingMoneyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    FragmentActivity activity;
    private ArrayList<OutgoingMoney> objects;

    private OnDeleteIconClickListener deleteIconClickListener;
    private OnEditIconClickListener editIconClickListener;

    // delete interface
    public interface OnDeleteIconClickListener{
        void onDeleteClick(int rowId);
    }

    public void setOnDeleteIconClickListener(OnDeleteIconClickListener onDeleteIconClickListener){
        deleteIconClickListener = onDeleteIconClickListener;
    }

    // edit interface

    public interface OnEditIconClickListener{
        void onEditClick(int rowId);
    }

    public void setOnEditIconClickListener(OnEditIconClickListener onEditIconClickListener){
        editIconClickListener = onEditIconClickListener;
    }

    public OutgoingMoneyAdapter(FragmentActivity activity, ArrayList<OutgoingMoney> objects) {
        this.activity = activity;
        this.objects = objects;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        RecyclerView.ViewHolder viewHolder = null;
        View v = inflater.inflate(R.layout.show_outgoing_history,parent,false);
        viewHolder = new OutgoingMoneyAdapter.MoneyOutViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        OutgoingMoneyAdapter.MoneyOutViewHolder  moneyOutViewHolder = (MoneyOutViewHolder) holder;

        OutgoingMoney outgoingMoney = (OutgoingMoney) objects.get(position);

        moneyOutViewHolder.show_ot_tns_name.setText(outgoingMoney.getTransactionName());
        moneyOutViewHolder.show_ot_tns_date.setText(outgoingMoney.getTransactionDate());
        moneyOutViewHolder.show_ot_tns_amount.setText(Float.toString(outgoingMoney.getTransactionAmount()));
        moneyOutViewHolder.show_ot_money_time.setText(outgoingMoney.getTransactionTime());

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class MoneyOutViewHolder extends RecyclerView.ViewHolder{

        TextView show_ot_tns_name,show_ot_tns_date,show_ot_tns_amount,show_ot_money_time;
        ImageView trans_delete,trans_edit;

        public MoneyOutViewHolder(View itemView) {
            super(itemView);
            show_ot_tns_name = itemView.findViewById(R.id.show_ot_money_name);
            show_ot_tns_date = itemView.findViewById(R.id.show_ot_money_date);
            show_ot_tns_amount = itemView.findViewById(R.id.show_ot_money_amount);
            show_ot_money_time = itemView.findViewById(R.id.show_ot_money_time);
            trans_delete = itemView.findViewById(R.id.trans_delete);
            trans_edit = itemView.findViewById(R.id.trans_edit);

            trans_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (deleteIconClickListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            deleteIconClickListener.onDeleteClick(position);
                        }
                    }
                }
            });

            trans_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (editIconClickListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            editIconClickListener.onEditClick(position);
                        }
                    }
                }
            });

        }

    }

    public void instantDataChang(ArrayList<OutgoingMoney> objects) {
        this.objects = objects;
        notifyDataSetChanged();
    }


}