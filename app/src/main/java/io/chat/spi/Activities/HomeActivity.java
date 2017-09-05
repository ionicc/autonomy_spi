package io.chat.spi.Activities;

import android.app.DownloadManager;
import android.content.Intent;
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

import io.chat.spi.R;
import io.chat.spi.Storage.LocalStorage;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

   private Button rosterButton;
   private Button tvButton;
   private Button reqButton;
   private Button prButton;
   private LocalStorage localStorage;
   private String id;
   private String username;
   private String name;
   private String inTime;

   private TextView homeName;
   private TextView homeInTime;

   private static final String LOGOUT_URL = "http://139.59.72.106/logout.php";
   private static final String RETURN_DETAILS_URL = "http://139.59.72.106/return_name.php";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_home);

      rosterButton = (Button) findViewById(R.id.roster);
      tvButton = (Button) findViewById(R.id.tv);
      reqButton = (Button) findViewById(R.id.requests);
      prButton = (Button) findViewById(R.id.performance_record);

      homeName = (TextView) findViewById(R.id.home_name);
      homeInTime = (TextView) findViewById(R.id.IntimeHome);

      Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
      setSupportActionBar(myToolbar);

      localStorage = new LocalStorage(this);

      rosterButton.setOnClickListener(this);
      tvButton.setOnClickListener(this);
      reqButton.setOnClickListener(this);
      prButton.setOnClickListener(this);

      HashMap<String, String> getId = localStorage.getId();
      id = getId.get(LocalStorage.ID);

      HashMap<String, String> getUsername = localStorage.getUsername();
      username = getUsername.get(LocalStorage.USERNAME);

      HashMap<String, String> getName = localStorage.getName();
      name = getName.get(LocalStorage.NAME);

      HashMap<String, String> getInTime = localStorage.getInTime();
      inTime = getInTime.get(LocalStorage.INTIME);

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
}
