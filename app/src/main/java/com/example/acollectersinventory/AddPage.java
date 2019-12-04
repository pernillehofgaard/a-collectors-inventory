package com.example.acollectersinventory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AddPage extends AppCompatActivity {
    EditText title, category, description;
    Button addBtn;
    ActionBar actionBar;
    SqlHelper db;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_page);

        Toolbar back_toolbar = findViewById(R.id.app_bar_back);
        setSupportActionBar(back_toolbar);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        db = new SqlHelper(this);
        title = findViewById(R.id.title);
        category = findViewById(R.id.category);
        description = findViewById(R.id.Description);
        addBtn = findViewById(R.id.addBtn);



        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ent_title = title.getText().toString().trim();
                String ent_category = category.getText().toString().trim();
                String ent_description = description.getText().toString().trim();
                String email = getIntent().getStringExtra("EMAIL");



                int val = db.addToInventory(ent_title, ent_category, ent_description, email);
                if (val > 0) {
                    Toast.makeText(AddPage.this, "Added to your inventory in " + ent_category, Toast.LENGTH_LONG).show();
                    Intent homepage = new Intent(AddPage.this, Homescreen.class);
                    startActivity(homepage);
                }else{
                    Toast.makeText(AddPage.this, "Error",Toast.LENGTH_SHORT).show();
                }


            }
        });


    }
}
