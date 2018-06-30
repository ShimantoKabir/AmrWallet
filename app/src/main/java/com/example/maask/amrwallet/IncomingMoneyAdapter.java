package com.example.maask.amrwallet;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.maask.amrwallet.Database.IncomingDatabaseSource;
import com.example.maask.amrwallet.Database.IncomingMoney;
import java.util.ArrayList;

/**
 * Created by Maask on 12/26/2017.
 */

public class IncomingMoneyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    FragmentActivity activity;
    private ArrayList<IncomingMoney> objects;

    IncomingDatabaseSource incomingDatabaseSource;

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

    public IncomingMoneyAdapter(FragmentActivity activity, ArrayList<IncomingMoney> objects) {
        this.activity = activity;
        this.objects = objects;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        RecyclerView.ViewHolder viewHolder = null;
        View v = inflater.inflate(R.layout.show_incoming_history,parent,false);
        viewHolder = new MoneyInViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        MoneyInViewHolder mivh = (MoneyInViewHolder) holder;
        IncomingMoney incomingMoney = objects.get(position);
        incomingDatabaseSource = new IncomingDatabaseSource(activity);

        mivh.show_in_tns_name.setText(incomingMoney.getTransactionName());
        mivh.show_in_tns_date.setText(incomingMoney.getTransactionDate());
        mivh.show_in_tns_amount.setText(Float.toString(incomingMoney.getTransactionAmount()));
        mivh.show_in_money_time.setText(incomingMoney.getTransactionTime());

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class MoneyInViewHolder extends RecyclerView.ViewHolder{

        TextView show_in_tns_name,show_in_tns_date,show_in_tns_amount,show_in_money_time;
        ImageView trans_delete,trans_edit;

        public MoneyInViewHolder(View itemView) {
            super(itemView);
            show_in_tns_name = itemView.findViewById(R.id.show_in_money_name);
            show_in_tns_date = itemView.findViewById(R.id.show_in_money_date);
            show_in_tns_amount = itemView.findViewById(R.id.show_in_money_amount);
            show_in_money_time = itemView.findViewById(R.id.show_in_money_time);
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

    public void instantDataChang(ArrayList<IncomingMoney> objects) {
        this.objects = objects;
        notifyDataSetChanged();
    }

}
