package io.chat.spi.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import io.chat.spi.R;

public class MainActivity extends AppCompatActivity {

   private Button submit;
   private EditText username;
   private EditText password;
   private Boolean wifiStatus;
   WifiInfo wifiInfo;

   private String ssid = "null";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      //Initialising WifiManager

      final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
      wifiInfo = wifiManager.getConnectionInfo();
      if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
         ssid = wifiInfo.getSSID();

      }

      //Initialising UI objects --Sagar

      submit = (Button) findViewById(R.id.login_button);
      username = (EditText) findViewById(R.id.mainUsername);
      password = (EditText) findViewById(R.id.mainPassword);

      //Putting up onClicks --Sagar


      submit.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
               ssid = wifiInfo.getSSID();

            }
            if (ssid.contains("null")) {
               Toast.makeText(getApplicationContext(), "Connect to Casino", Toast.LENGTH_SHORT).show();
            } else {
               if (ssid.contains("The Master Server")) {
                  Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
                  Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                  startActivity(intent);
               } else {
                  Toast.makeText(getApplicationContext(), ssid, Toast.LENGTH_SHORT).show();
               }
            }
         }
      });



      /*      if(ssid.equals("The Master Server")) {

               submit.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {

                     Toast.makeText(getApplicationContext(), "You have logged in", Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                     startActivity(intent);

                  }
               });
            }
            else {
               Toast.makeText(this,"Please connect to Casino's Wifi to login",Toast.LENGTH_SHORT).show();

            }

*/


   }

   public boolean isConnected() throws InterruptedException, IOException {
      String command = "ping -c 1 google.com";
      return (Runtime.getRuntime().exec(command).waitFor() == 0);
   }


}
