package io.chat.spi.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import io.chat.spi.R;
import io.chat.spi.Storage.LocalStorage;

public class MainActivity extends AppCompatActivity {

   private Button submit;
   private EditText username;
   private EditText password;
   private Boolean wifiStatus;

   private String usernameString;
   private String passwordString;

   WifiInfo wifiInfo;

   private LocalStorage localStorage;
   private String LOGIN_URL = "http://139.59.72.106/login.php";

   private String ssid = "null";
   private String bssid = "null";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      localStorage = new LocalStorage(this);

      //Initialising WifiManager

      final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
      wifiInfo = wifiManager.getConnectionInfo();
      if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
         ssid = wifiInfo.getSSID();
         bssid = wifiInfo.getBSSID();
      }

      if(localStorage.getLoginState()) {
         Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
         startActivity(intent);
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
               bssid = wifiInfo.getBSSID();

               Log.d("The BSSID", wifiInfo.getBSSID());
            }
            if (bssid.contains("null")) {
               Toast.makeText(getApplicationContext(), "Connect to Casino", Toast.LENGTH_SHORT).show();
            } else {
               if (ssid.contains("Guest")) {
                  Date currentTime = Calendar.getInstance().getTime();
                  String currentTimeString = currentTime.toString();

                  usernameString = username.getText().toString();
                  passwordString = password.getText().toString();

                  if(bssid.contains("56:d9:e7:f8:b0:2d")) {

                     if (usernameString.isEmpty() && passwordString.isEmpty()) {

                        Toast.makeText(getApplicationContext(), "Please enter your credentials", Toast.LENGTH_SHORT).show();
                     } else if (usernameString.isEmpty()) {

                        Toast.makeText(getApplicationContext(), "Please enter your username", Toast.LENGTH_SHORT).show();
                     } else if (passwordString.isEmpty()) {

                        Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
                     } else {
                        arrangeData(usernameString, passwordString);
                     }

                  }
                  else {
                     Toast.makeText(getApplicationContext(),"Nice try",Toast.LENGTH_SHORT).show();
                  }

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

   public String getDateCurrentTimeZone(long timestamp) {
      try {
         Calendar calendar = Calendar.getInstance();
         TimeZone tz = TimeZone.getDefault();
         calendar.setTimeInMillis(timestamp * 1000);
         calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         Date currenTimeZone = (Date) calendar.getTime();
         return sdf.format(currenTimeZone);
      } catch (Exception e) {
      }
      return "";
   }

   public boolean isConnected() throws InterruptedException, IOException {
      String command = "ping -c 1 google.com";
      return (Runtime.getRuntime().exec(command).waitFor() == 0);
   }

   public void arrangeData(final String usernameString, final String passwordString) {


      final StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
              new Response.Listener<String>() {
                 @Override
                 public void onResponse(String response) {

                 /*   if (response.contains("Logged")) {
                       Toast.makeText(MainActivity.this, "You have Succesfully logged in", Toast.LENGTH_SHORT).show();

                       Intent i = new Intent(MainActivity.this, HomeActivity.class);
                       //storage.saveEmail(email);
                       //storage.setLogin();
                       localStorage.saveUsername(usernameString);
                       startActivity(i);
                       finish();
                    }
                    */

                    if (response.contains("Please enter your password")) {

                       Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                    }

                    else if (response.contains("Please enter your username")) {

                       Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                    }

                    else if (response.contains("Your Username or Password is invalid")) {

                       Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                    else {
                       String id = response;
                       localStorage.setLoginId(id);
                       localStorage.loggedIn();
                       localStorage.saveUsername(usernameString);
                       Toast.makeText(getApplicationContext(),id,Toast.LENGTH_SHORT).show();
                       finish();
                       Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                       startActivity(intent);


                    }
                 }
              },
              new Response.ErrorListener() {
                 @Override
                 public void onErrorResponse(VolleyError error) {

                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                 }
              }) {

         protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", usernameString);
            params.put("password", passwordString);
            return params;

         }


      };

      RequestQueue reuqestQue = Volley.newRequestQueue(this);
      reuqestQue.add(stringRequest);


   }


}
