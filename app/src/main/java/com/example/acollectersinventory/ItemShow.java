package com.example.acollectersinventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import android.widget.Toast;

import static com.example.acollectersinventory.R.menu.menu_in_item_show;

public class ItemShow extends AppCompatActivity {
    SQLiteDatabase mydb;
    SQLiteOpenHelper mydbhelper;
    SqlHelper db;
    ListView itemList;
    Cursor mycursor;
    CursorAdapter adapter;

    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_show);
        mydbhelper = new SqlHelper(this);

        itemList = findViewById(R.id.itemListview);
        final String item = getIntent().getStringExtra("ITEM"); // henter item (kategori) fra forrige aktivitet
        String username = getIntent().getStringExtra("EMAIL"); // henter email fra forrige aktivitet

        androidx.appcompat.widget.Toolbar back_toolbar = findViewById(R.id.app_bar_back);
        setSupportActionBar(back_toolbar);

        actionBar = getSupportActionBar();
        actionBar.setTitle(item);
        actionBar.setDisplayHomeAsUpEnabled(true);


        try {
            mydb = mydbhelper.getReadableDatabase();
            mycursor = mydb.rawQuery("SELECT _id, Title, Description FROM Inventory WHERE Category = '" + item + "' AND Email ='" + username + "'", null);
        } catch (SQLException e) {
            Toast.makeText(this, "SQL Exception", Toast.LENGTH_LONG).show();
        }

        adapter = new SimpleCursorAdapter(this, R.layout.show_item_layout, mycursor, new String[]{"_id", "Title", "Description"}, new int[]{R.id.idField, R.id.titleField, R.id.descriptionField}, 0);
        itemList.setAdapter(adapter);


    }

    public void goToAddToCategory(View view) {
        Intent goToAddToCategory = new Intent(ItemShow.this, AddToCategory.class);
        String category = getIntent().getStringExtra("ITEM");
        String email = getIntent().getStringExtra("EMAIL");
        goToAddToCategory.putExtra("ITEM", category);
        goToAddToCategory.putExtra("EMAIL", email);
        startActivity(goToAddToCategory);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(menu_in_item_show,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.deleteItem:
                goToDelete();
                return true;
            case R.id.editItem:
                goToEditChoice();
            case R.id.shareItem:
                shareItem();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void onDelete(String id){
        mydbhelper.getWritableDatabase();
        mydb.delete("Inventory", "_id=?", new String[]{id});
        mycursor = mydb.query("Inventory", new String[]{"_id", "Title", "Category", "Description", "Email"}, null,null,null,null,null);
    }

    public void goToDelete(){
        Intent deleteItem = new Intent(ItemShow.this, com.example.acollectersinventory.DeleteItem.class);
        String email = getIntent().getStringExtra("EMAIL");
        String category = getIntent().getStringExtra("ITEM");
        deleteItem.putExtra("EMAIL", email);
        deleteItem.putExtra("ITEM",category);
        startActivity(deleteItem);
    }

    public void goToEditChoice(){
        Intent editItem = new Intent(ItemShow.this, ChooseItemToEdit.class);
        String email = getIntent().getStringExtra("EMAIL");
        String category = getIntent().getStringExtra("ITEM");
        editItem.putExtra("EMAIL", email);
        editItem.putExtra("ITEM",category);
        startActivity(editItem);
    }

    public void shareItem(){
        String category = getIntent().getStringExtra("ITEM");
        String email = getIntent().getStringExtra("EMAIL");
        StringBuilder res = new StringBuilder();
        try {
            mydb = mydbhelper.getWritableDatabase();
            mycursor = mydb.rawQuery("SELECT _id, Title, Description FROM Inventory WHERE Category = '" + category +"' AND Email = '"+ email + "'", null );
        }catch (SQLException e){
            Toast.makeText(this, "SQL Exception", Toast.LENGTH_LONG).show();
        }

        Toast.makeText(this, mycursor.toString(), Toast.LENGTH_LONG).show();
        int i = 0;
        if(mycursor.getCount() > 0){
            while(mycursor.moveToNext()){
                res.append(mycursor.getString(i));
                i ++;
            }
            Toast.makeText(this, res.toString(), Toast.LENGTH_LONG).show();
        }
//        mycursor.close();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, category);
        shareIntent.putExtra(Intent.EXTRA_TEXT, res.toString());
        startActivity(Intent.createChooser(shareIntent, "share using"));
    }
}
