package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private RecyclerView testView;
    private Toolbar toolbar;
    private TestAdapter adapter;
    private Dialog progressDialog;
    private TextView dialogText;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        getSupportActionBar().setTitle(DBQuery.g_catList.get(DBQuery.g_selected_cat_index).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        testView = findViewById(R.id.test_recycler_view);

        progressDialog = new Dialog(TestActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText = findViewById(R.id.dialog_text);
        dialogText.setText("Loading..");

        progressDialog.show();


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        testView.setLayoutManager(layoutManager);

        //loadTestData();
        DBQuery.loadTestData(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                adapter = new TestAdapter(DBQuery.g_testList);
                testView.setAdapter(adapter);
                progressDialog.dismiss();

            }

            @Override
            public void onFailure() {
                progressDialog.dismiss();
                Toast.makeText(TestActivity.this, "Something " +
                        "went wrong",Toast.LENGTH_SHORT).show();

            }
        });




    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            TestActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}