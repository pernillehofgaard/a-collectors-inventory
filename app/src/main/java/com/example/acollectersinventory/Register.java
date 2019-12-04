package com.example.acollectersinventory;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    EditText username;
    EditText email;
    EditText reg_pwd;
    EditText rep_reg_pwd;
    Button signUp;
    TextView loginLink;
    SqlHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new SqlHelper(this);
        username = findViewById(R.id.username);
        email = findViewById(R.id.txt_email);
        reg_pwd = findViewById(R.id.reg_pwd);
        rep_reg_pwd = findViewById(R.id.rep_reg_pwd);
        signUp = findViewById(R.id.signupBtn);
        loginLink = findViewById(R.id.loginBtn);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ent_username = username.getText().toString().trim(); // mottar username og trimmer stringen så det ikke er mellomrom før/etter stringen
                String ent_email = email.getText().toString().trim(); // mottar email og trimmer stringen så det ikke er mellomrom før/etter stringen
                String ent_pwd = reg_pwd.getText().toString().trim(); // mottar passord og trimmer stringen så det ikke er mellomrom før/etter stringen
                String conf_pwd = rep_reg_pwd.getText().toString().trim(); // mottar bekreftet passord og trimmer stringen så det ikke er mellomrom før/etter stringen

                if(ent_username.length() == 0 || ent_email.length() == 0 || ent_pwd.length() == 0 || conf_pwd.length() == 0){
                    Toast.makeText(Register.this, "Empty field(s)", Toast.LENGTH_SHORT).show();
                }else if(ent_pwd.length() <= 7 && conf_pwd.length() <= 7){
                    Toast.makeText(Register.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                }else if(db.checkEmail(ent_email) == true){ //Sjekker om email er i bruk
                    Toast.makeText(Register.this, "Email already used", Toast.LENGTH_LONG).show();
                }else if(ent_pwd.equals(conf_pwd)){ //sjekker om passordene er like
                    int val = db.addUser(ent_username, ent_email,ent_pwd); // gjør addUser til et tall
                    if(val > 0) { //hvis val er større enn 1 vil det si at brukeren er registrert
                        Toast.makeText(Register.this, "Registrert", Toast.LENGTH_SHORT).show();
                        Intent loginPage = new Intent(Register.this, MainActivity.class);
                        startActivity(loginPage); // hvis registreringen er vellykket blir brukeren tatt til MainActivity aka login
                    }else{
                        Toast.makeText(Register.this, "signup error", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Register.this, "Password does not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
        db.close();
    }

    public void goToLogin(View view){
        Intent loginPage = new Intent(this, MainActivity.class);
        startActivity(loginPage);
    }
}
