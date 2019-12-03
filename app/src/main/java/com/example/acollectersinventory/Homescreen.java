package com.example.acollectersinventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class Homescreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {
    SQLiteDatabase mydb;
    SQLiteOpenHelper mydbhelper;
    ListView list;
    Cursor mycursor;
    CursorAdapter mycursorAdapter;

    Toolbar toolbar;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        mydbhelper = new SqlHelper(this);

        //Henter brukernavn
        String username = getIntent().getStringExtra("EMAIL");



        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawer,toolbar, R.string.nav_open_drawer, R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar();

        list = findViewById(R.id.list_view1);

        try {
            mydb = mydbhelper.getReadableDatabase();
            mycursor = mydb.rawQuery("SELECT _id, Title, Category FROM Inventory WHERE Email = '" + username + "' GROUP BY Category",null);
        }catch (SQLException e){
            Toast.makeText(this, "SQL Exception", Toast.LENGTH_LONG).show();
        }

        mycursorAdapter = new SimpleCursorAdapter(this, R.layout.simlpe_list_item_1, mycursor, new String[]{"_id","Title", "Category"}, new int[]{R.id.idField, R.id.titleField, R.id.categoryField},0);
        list.setAdapter(mycursorAdapter);
        list.setOnItemClickListener((AdapterView.OnItemClickListener) this);


        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
    }

    @Override
    protected void onPostResume() {
        list = findViewById(R.id.list_view1);
        String username = getIntent().getStringExtra("EMAIL");

        try {
            mydb = mydbhelper.getReadableDatabase();
            mycursor = mydb.rawQuery("SELECT _id, Title, Category FROM Inventory WHERE Email = '" + username + "' GROUP BY Category",null);
        }catch (SQLException e){
            Toast.makeText(this, "SQL Exception", Toast.LENGTH_LONG).show();
        }

        mycursorAdapter = new SimpleCursorAdapter(this, R.layout.simlpe_list_item_1, mycursor, new String[]{"_id","Title", "Category"}, new int[]{R.id.idField, R.id.titleField, R.id.categoryField},0);
        list.setAdapter(mycursorAdapter);
        list.setOnItemClickListener((AdapterView.OnItemClickListener) this);
        super.onPostResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();
        Intent activity = null;

        switch (id){
            case R.id.nav_logout:
                activity = new Intent(this, MainActivity.class);
                onDestroy();
                break;
            case R.id.nav_community:
                activity = new Intent(this, CommunityPage.class);
                break;
            default:
                activity = new Intent(this, Homescreen.class);
        }
        if(activity != null){
            startActivity(activity);
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        switch(item.getItemId()){
            case R.id.actionAdd:
                goToAdd();

        }
            return super.onOptionsItemSelected(item);
    }
     */

    public void goToAdd(View view){
        Intent addPage = new Intent(this, AddPage.class);
        String email = getIntent().getStringExtra("EMAIL");
        addPage.putExtra("EMAIL", email);
        startActivity(addPage);
    }


    // "overfører kategorien en trykker på til ItemShow klassen
    @Override
    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Cursor cursor = (Cursor)  l.getItemAtPosition(position) ;
        String item = cursor.getString(cursor.getColumnIndex("Category"));

        Intent itemShow = new Intent();
        itemShow.setClass(this, ItemShow.class);
        itemShow.putExtra("ITEM", item);
        String email = getIntent().getStringExtra("EMAIL");
        itemShow.putExtra("EMAIL", email);
        startActivity(itemShow);
    }

}
