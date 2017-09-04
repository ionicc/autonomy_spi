package io.chat.spi.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.chat.spi.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

   private Button rosterButton;
   private Button tvButton;
   private Button reqButton;
   private Button prButton;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_home);

      rosterButton = (Button) findViewById(R.id.roster);
      tvButton = (Button) findViewById(R.id.tv);
      reqButton = (Button) findViewById(R.id.requests);
      prButton = (Button) findViewById(R.id.performance_record);

      rosterButton.setOnClickListener(this);
      tvButton.setOnClickListener(this);
      reqButton.setOnClickListener(this);
      prButton.setOnClickListener(this);

   }

   @Override
   public void onClick(View view) {
      if(view == reqButton) {
         Intent intent = new Intent(HomeActivity.this,RequestsActivity.class);
         startActivity(intent);
      }
   }
}
