package com.example.acollectersinventory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddToCategory extends AppCompatActivity {
    EditText title, description;
    Button add;
    ActionBar actionBar;
    SqlHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_category);
        db = new SqlHelper(this);

        androidx.appcompat.widget.Toolbar back_btn = findViewById(R.id.back_btn);
        setSupportActionBar(back_btn);

        actionBar = getSupportActionBar();
        final String CATEGORY = getIntent().getStringExtra("ITEM");
        final String EMAIL = getIntent().getStringExtra("EMAIL");
        actionBar.setTitle("Add To " + CATEGORY);
        actionBar.setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.titleField);
        description = findViewById(R.id.descriptionField);
        add = findViewById(R.id.addBtn);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ent_title = title.getText().toString().trim();
                String ent_description = description.getText().toString().trim();

                int val = db.addToInventory(ent_title, CATEGORY, ent_description, EMAIL);
                if(val > 0){
                    Toast.makeText(AddToCategory.this, "Added to " + CATEGORY, Toast.LENGTH_LONG).show();
                    Intent home = new Intent(AddToCategory.this, Homescreen.class);
                    startActivity(home);
                }else{
                    Toast.makeText(AddToCategory.this, "Error", Toast.LENGTH_LONG).show();
                }

            }
        });

        //Thumbnail variable
        ImageView imageView = (ImageView) findViewById(R.id.thumbnailIV);

        final Button button = findViewById(R.id.addThumbnailBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                takePhoto();
            }
        });
        //Thumbnail variable slutt

    }



    static final int REQUEST_IMAGE_CAPTURE = 1;

    //Lager ny intent som åpner kamera
    private void takePhoto() {
        Intent takePhotoIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
            //Bruker intent til å åpne kamera
            startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Fetcher bildet
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //Legger det fetchede bildet i imageView
            ImageView imageView = (ImageView) findViewById(R.id.thumbnailIV);
            imageView.setImageBitmap(imageBitmap);
        }
    }
}
