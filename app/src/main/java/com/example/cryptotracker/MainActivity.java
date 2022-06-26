package com.example.cryptotracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText search;
    private RecyclerView recyclerView;
private ProgressBar progressBar;
private ArrayList<CurrencyModel> currencyModelArrayList;
private CurrencyRVAdapter currencyRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = findViewById(R.id.search);
        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.Progressbar);
        currencyModelArrayList = new ArrayList<>();
        currencyRVAdapter = new CurrencyRVAdapter(currencyModelArrayList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(currencyRVAdapter);
        getCurrencyData();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterCurrency(s.toString());
            }
        });

    }

    private void filterCurrency(String currency){
        ArrayList<CurrencyModel> filteredList = new ArrayList<>();
        for(CurrencyModel item : currencyModelArrayList){
            if(item.getName().toLowerCase().contains(currency.toLowerCase())){
                filteredList.add(item);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(MainActivity.this, "No currency found for searched query", Toast.LENGTH_SHORT).show();
        } else {
            currencyRVAdapter.filter(filteredList);
        }
    }

    private void getCurrencyData(){
        progressBar.setVisibility(View.VISIBLE);
      String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               progressBar.setVisibility(View.GONE);
               try {
                   JSONArray dataArray = response.getJSONArray("data");
                   for(int i = 0 ;i < dataArray.length(); i++){
                       JSONObject dataObj = dataArray.getJSONObject(i);
                       String name = dataObj.getString("name");
                       String symbol = dataObj.getString("symbol");
                       JSONObject quote = dataObj.getJSONObject("quote");
                       JSONObject USD = quote.getJSONObject("USD");
                     double price = USD.getDouble("price");
                     currencyModelArrayList.add(new CurrencyModel(name, symbol, price));
                   }
                   currencyRVAdapter.notifyDataSetChanged();
               }catch (JSONException e){
                   e.printStackTrace();
                   Toast.makeText(MainActivity.this, "Failed to extract json data", Toast.LENGTH_SHORT).show();
               }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this , " Something went wrong", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("X-CMC_PRO_API_KEY", "3d641861-dd9e-456e-be9d-c6897ca40539");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}