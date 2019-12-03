package com.example.acollectersinventory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ChooseItemToEdit extends AppCompatActivity {
    SQLiteDatabase mydb;
    SQLiteOpenHelper mydbhelper;

    ListView itemList;
    Cursor mycursor;
    CursorAdapter adapter;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_item_to_edit);
        mydbhelper = new SqlHelper(this);

        itemList = findViewById(R.id.editList);
        final String item = getIntent().getStringExtra("ITEM"); // henter item (kategori) fra forrige aktivitet
        String username = getIntent().getStringExtra("EMAIL"); // henter email fra forrige aktivitet

        androidx.appcompat.widget.Toolbar back_toolbar = findViewById(R.id.app_bar_back);
        setSupportActionBar(back_toolbar);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Edit item");
        actionBar.setDisplayHomeAsUpEnabled(true);


        try {
            mydb = mydbhelper.getWritableDatabase();
            mycursor = mydb.rawQuery("SELECT _id, Title, Description FROM Inventory WHERE Category = '" + item + "' AND Email ='" + username + "'", null);
        } catch (SQLException e) {
            Toast.makeText(this, "SQL Exception", Toast.LENGTH_LONG).show();
        }

        adapter = new SimpleCursorAdapter(this, R.layout.show_item_layout, mycursor, new String[]{"_id", "Title", "Description"}, new int[]{R.id.idField, R.id.titleField, R.id.descriptionField}, 0);
        itemList.setAdapter(adapter);

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) itemList.getItemAtPosition(i);
                String title = cursor.getString(cursor.getColumnIndex("Title"));
                String description = cursor.getString(cursor.getColumnIndex("Description"));
                String id =cursor.getString(cursor.getColumnIndex("_id"));

                String email = getIntent().getStringExtra("EMAIL");


                Intent editItem = new Intent(ChooseItemToEdit.this, EditItem.class);
                editItem.putExtra("ID", id);
                editItem.putExtra("TITLE", title);
                editItem.putExtra("DESCRIPTION", description);
                editItem.putExtra("EMAIL",email);
                startActivity(editItem);
            }
        });

    }

}
