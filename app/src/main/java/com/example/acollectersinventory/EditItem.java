package com.example.acollectersinventory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class EditItem extends AppCompatActivity {
    SQLiteDatabase mydb;
    SQLiteOpenHelper mydbhelper;


    Cursor mycursor;
    ActionBar actionBar;
    CursorAdapter adapter;
    TextView newTitle, newDescription;
    Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item2);
        mydbhelper = new SqlHelper(this);

        newTitle = findViewById(R.id.newTitle);
        newDescription = findViewById(R.id.newDescription);
        updateBtn = findViewById(R.id.updateBtn);

        String oldTitle = getIntent().getStringExtra("TITLE");
        String oldDescription = getIntent().getStringExtra("DESCRIPTION");


        newTitle.setText(oldTitle);
        newDescription.setText(oldDescription);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {


                    String updatedTitle = newTitle.getText().toString().trim();
                    String updatedDescription = newDescription.getText().toString().trim();
                    String id = getIntent().getStringExtra("ID");

                    mydb = mydbhelper.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put("Title", updatedTitle);
                    cv.put("Description", updatedDescription);

                    mydb.update("Inventory", cv, "_id = " + id, null);
                    Toast.makeText(EditItem.this, "Updated", Toast.LENGTH_LONG).show();

                    //går tilbake til homescreen
                    Intent homescreen = new Intent(EditItem.this, Homescreen.class);
                    startActivity(homescreen);

                } catch (SQLException e) {
                    Toast.makeText(EditItem.this, "SQL Exception", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
