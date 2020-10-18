package com.example.myapplication;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TransactionHistory extends AppCompatActivity {
    private ArrayList<String> itemsTransactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemsTransactionList = getIntent().getStringArrayListExtra("transactionHistory");
        setContentView(R.layout.activity_transaction);

        LinearLayout llMain = findViewById(R.id.transMain);
        for(int i=0; i<itemsTransactionList.size(); i++){
            TextView textView = new TextView(this);
            textView.setText(itemsTransactionList.get(i));
            llMain.addView(textView);
        }

    }

}
