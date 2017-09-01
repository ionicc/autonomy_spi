package io.chat.spi.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.chat.spi.R;

public class HomeActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_home);
   }
}
