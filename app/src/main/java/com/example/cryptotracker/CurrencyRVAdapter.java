package com.example.cryptotracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CurrencyRVAdapter extends RecyclerView.Adapter<CurrencyRVAdapter.ViewHolder> {
    ArrayList<CurrencyModel> currencyModelArrayList;
    private Context context;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public CurrencyRVAdapter(ArrayList<CurrencyModel> currencyModelArrayList, Context context) {
        this.currencyModelArrayList = currencyModelArrayList;
        this.context = context;
    }

    public void filter (ArrayList<CurrencyModel> filteredList){
          currencyModelArrayList = filteredList;
          notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     View view = LayoutInflater.from(context).inflate(R.layout.currency_rv_item, parent, false);
     return new CurrencyRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      CurrencyModel currencyModel = currencyModelArrayList.get(position);
      holder.currencyName.setText(currencyModel.getName());
      holder.symbol.setText(currencyModel.getSymbol());
      holder.rate.setText("$ "+df2.format(currencyModel.getPrice()));
    }

    @Override
    public int getItemCount() {
       return currencyModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView currencyName , symbol , rate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            currencyName = itemView.findViewById(R.id.CurrencyName);
            symbol = itemView.findViewById(R.id.symbol);
            rate = itemView.findViewById(R.id.CurrencyRate);

        }
    }
}
