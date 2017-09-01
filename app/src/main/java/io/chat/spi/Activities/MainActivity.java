package io.chat.spi.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.chat.spi.R;

public class MainActivity extends AppCompatActivity {

   private Button submit;
   private EditText username;
   private EditText password;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      //Initialising UI objects --Sagar

      submit = (Button) findViewById(R.id.login_button);
      username = (EditText) findViewById(R.id.mainUsername);
      password = (EditText) findViewById(R.id.mainPassword);

      //Putting up onClicks --Sagar

      submit.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            Toast.makeText(getApplicationContext(), "You have logged in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);

         }
      });
   }
}
