package com.example.acollectersinventory;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText email;
    EditText pwd;
    Button login;
    TextView registrer;
    SqlHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // definerer variabler
        db = new SqlHelper(this);
        email = findViewById(R.id.txt_email);
        pwd = findViewById(R.id.txt_pwd);
        login = findViewById(R.id.loginBtn);
        registrer = findViewById(R.id.registerLink); // i activity_main.xml er det en onClick som kaller på goToRegister()

        // setter onClickListener på login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mailAddress = email.getText().toString().trim(); // mottar mail
                String password = pwd.getText().toString().trim(); // mottar passord

                Boolean result = db.checkUser(mailAddress, password); // sjekker om mail og passord stemmer
                if (result == true) { //hvis result returnerer true kalles vi på loginSuccess som tar deg til homepage
                    //Toast.makeText(MainActivity.this,"Logged in", Toast.LENGTH_SHORT).show();
                    loginSuccess();
                } else { // hvis result returnerer noe annet enn true får brukeren en feilmelding
                    Toast.makeText(MainActivity.this, "Brukernavn eller passord er feil", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void goToRegister(View view){
        //Lager intent til registerpage
        Intent regPage = new Intent(this, Register.class);
        //Tar bruker til registerpage
        startActivity(regPage);
    }

    public void loginSuccess(){
        //Lager intent som starter ny aktivitet
        Intent homepage = new Intent(this, Homescreen.class);

        //"lagrer mail" så den kan vises etter innlogging
        String shown_email = email.getText().toString();
        homepage.putExtra("EMAIL", shown_email);
        //Tar brukeren til homepage hvis login == success
        startActivity(homepage);

    }
}
