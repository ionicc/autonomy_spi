package io.chat.spi.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.chat.spi.Adapters.CustomAdapter;
import io.chat.spi.Models.Item;
import io.chat.spi.R;
import io.chat.spi.Storage.LocalStorage;

public class AttendanceActivity extends AppCompatActivity {

   private static final String JSON_URL = "http://139.59.72.106/attendance.php";

   ListView listView;
   ArrayList<Item> arrayList = new ArrayList<>();

   private LocalStorage localStorage;

   private String username;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_attendance);

      localStorage = new LocalStorage(getApplicationContext());

      HashMap<String, String> getUsername = localStorage.getUsername();
      username = getUsername.get(LocalStorage.USERNAME);


      listView = (ListView) findViewById(R.id.attendance_listview);
      getData();
   }

   private void getData() {

      StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
         @Override
         public void onResponse(String response) {
               if (response.contains("No data available")) {
               Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_SHORT).show();
            } else {
               try {
                  JSONArray jsonArray = new JSONArray(response);
                  for(int i=0; i < jsonArray.length(); i++) {
                     JSONObject jsonObject = jsonArray.getJSONObject(i);
                     String date = jsonObject.getString("Date");
                     String loginTime = jsonObject.getString("LoginTime");
                     String logoutTime = jsonObject.getString("LogoutTime");

                     arrayList.add(new Item(date,loginTime,logoutTime));
                  }

                  CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(),R.layout.list_view_items,arrayList);
                  listView.setAdapter(customAdapter);

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
            params.put("username", username);
            return params;

         }
      };
      RequestQueue requestQueue = Volley.newRequestQueue(this);
      requestQueue.add(stringRequest);
   }


}
