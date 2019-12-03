package com.example.acollectersinventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.sql.SQLException;

public class CommunityPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ListView communityList;
    SQLiteDatabase mydb;
    SQLiteOpenHelper mydbHelper;
    Cursor c;
    CursorAdapter ca;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_page);
        mydbHelper = new SqlHelper(this);

        toolbar = findViewById(R.id.comToolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawer,toolbar, R.string.nav_open_drawer, R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar();

        communityList = findViewById(R.id.community_listview);

        try{
            mydb = mydbHelper.getReadableDatabase();
            c = mydb.rawQuery("SELECT  _id, Title, Category FROM Inventory GROUP BY Title",null);
        }catch (Exception e){
            Toast.makeText(this, "noe gikk galt " + e , Toast.LENGTH_LONG);
        }

        ca = new SimpleCursorAdapter(this, R.layout.show_item_layout, c, new String[]{"_id", "Title", "Category"}, new int[]{R.id.idField, R.id.titleField,R.id.descriptionField});
        communityList.setAdapter(ca);

        NavigationView navView = findViewById(R.id.com_nav_view);
        navView.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent activity = null;

        switch (id){
            case R.id.nav_logout:
                activity = new Intent(this, MainActivity.class);
                onDestroy();
                break;
            case R.id.nav_myInventory:
                activity = new Intent(this, Homescreen.class);
                break;
            default:
                activity = new Intent(this, CommunityPage.class);
        }
        if(activity != null){
            startActivity(activity);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
