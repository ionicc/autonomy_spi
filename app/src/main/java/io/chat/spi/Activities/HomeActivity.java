package io.chat.spi.Activities;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.jar.Manifest;

import io.chat.spi.R;
import io.chat.spi.Storage.LocalStorage;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

   private Button rosterButton;
   private Button tvButton;
   private Button reqButton;
   private Button attendanceButton;
   private LocalStorage localStorage;
   private String id;
   private String username;
   private String name;
   private String inTime;
   private String loginToken;

   private TextView homeName;
   private TextView homeInTime;

   private DownloadManager downloadManager;

   private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 56;

   private static final String LOGOUT_URL = "http://139.59.72.106/logout.php";
   private static final String RETURN_DETAILS_URL = "http://139.59.72.106/return_name.php";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_home);

      rosterButton = (Button) findViewById(R.id.roster);
      tvButton = (Button) findViewById(R.id.tv);
      reqButton = (Button) findViewById(R.id.requests);
      attendanceButton = (Button) findViewById(R.id.attendance);

      homeName = (TextView) findViewById(R.id.home_name);
      homeInTime = (TextView) findViewById(R.id.IntimeHome);

      Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
      setSupportActionBar(myToolbar);

      localStorage = new LocalStorage(this);

      rosterButton.setOnClickListener(this);
      tvButton.setOnClickListener(this);
      reqButton.setOnClickListener(this);
      attendanceButton.setOnClickListener(this);

      HashMap<String, String> getId = localStorage.getId();
      id = getId.get(LocalStorage.ID);

      HashMap<String, String> getUsername = localStorage.getUsername();
      username = getUsername.get(LocalStorage.USERNAME);

      HashMap<String, String> getName = localStorage.getName();
      name = getName.get(LocalStorage.NAME);

      HashMap<String, String> getInTime = localStorage.getInTime();
      inTime = getInTime.get(LocalStorage.INTIME);

      HashMap<String,String> getLoginToken = localStorage.getLoginToken();
      loginToken = getLoginToken.get(LocalStorage.TOKEN);

      if (name == null && inTime == null) {

         getDetails();
      } else {

         homeInTime.setText(inTime);
         homeName.setText(name);
      }

   }

   @Override
   public void onClick(View view) {
      if (view == reqButton) {
         Intent intent = new Intent(HomeActivity.this, RequestsActivity.class);
         startActivity(intent);
      }

      if(view == rosterButton) {checkPermissions();}

      if(view == tvButton) {goToYoutubeChannel();}

      if(view == attendanceButton) {
         Intent intent = new Intent(getApplicationContext(),AttendanceActivity.class);
         startActivity(intent);
      }

   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      // Handle action bar item clicks here. The action bar will
      // automatically handle clicks on the Home/Up button, so long
      // as you specify a parent activity in AndroidManifest.xml.
      int id = item.getItemId();

      //noinspection SimplifiableIfStatement
      if (id == R.id.logout) {

         logout();
         return true;
      }


      return super.onOptionsItemSelected(item);
   }

   private void logout() {

      StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGOUT_URL, new Response.Listener<String>() {
         @Override
         public void onResponse(String response) {
            if (response.contains("Logged Out")) {
               Toast.makeText(getApplicationContext(), "You have been successfully logged out", Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(getApplicationContext(), MainActivity.class);
               startActivity(intent);
               localStorage.logoutSession();
               localStorage.logOut();
               finish();
            }

            if (response.contains("Failed")) {
               Toast.makeText(getApplicationContext(), "Failed to log out", Toast.LENGTH_SHORT).show();
            }

         }
      }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
         }
      }) {

         protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("id", id);
            params.put("username", username);
            params.put("token",loginToken);
            return params;

         }
      };
      RequestQueue requestQueue = Volley.newRequestQueue(this);
      requestQueue.add(stringRequest);
   }

   private void getDetails() {

      if (name == null) {
         StringRequest stringRequest = new StringRequest(Request.Method.POST, RETURN_DETAILS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               if (response.contains("No data available")) {
                  Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_SHORT).show();
               } else {
                  try {
                     JSONArray jsonArray = new JSONArray(response);
                     JSONObject jsonObject = jsonArray.getJSONObject(0);

                     name = jsonObject.getString("name");
                     inTime = jsonObject.getString("login_time");

                     localStorage.saveName(name);
                     localStorage.saveInTime(inTime);

                     homeName.setText(name);
                     homeInTime.setText(inTime);



                  } catch (JSONException e) {
                     e.printStackTrace();
                  }

               }
            }
         },
                 new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                 }) {
            protected Map<String, String> getParams() {

               Map<String, String> params = new HashMap<>();
               params.put("id",id);
               params.put("username",username);
               return params;

            }
         };
         RequestQueue requestQueue = Volley.newRequestQueue(this);
         requestQueue.add(stringRequest);
      }
   }

   public void checkPermissions() {

      if (ContextCompat.checkSelfPermission(getApplicationContext(),
              android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
              != PackageManager.PERMISSION_GRANTED) {

         // Should we show an explanation?


            //Didn't add any explanation right now --Sagar Vakkala

            ActivityCompat.requestPermissions(HomeActivity.this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            //Requesting permissions for reading contacts

            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
               Toast.makeText(this,"Downloading",Toast.LENGTH_SHORT).show();
               downloadRoster();

            }

            else {
               Toast.makeText(getApplicationContext(),"Please grant access",Toast.LENGTH_SHORT).show();
            }



      }
      else {
         downloadRoster();
      }
   }

   public void downloadRoster() {


      downloadManager= (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
      Uri uri=Uri.parse("http://139.59.72.106/download.pdf");
      DownloadManager.Request request=new DownloadManager.Request(uri);
      request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Roster");
      request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
      downloadManager.enqueue(request);

   }

   private void goToYoutubeChannel() {
      Intent intent = new Intent(Intent.ACTION_VIEW);
      intent.setData(Uri.parse("https://www.youtube.com/user/PewDiePie"));
      startActivity(intent);
   }
}
