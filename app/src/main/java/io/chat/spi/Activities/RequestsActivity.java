package io.chat.spi.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import io.chat.spi.R;
import io.chat.spi.Storage.LocalStorage;

public class RequestsActivity extends AppCompatActivity implements View.OnClickListener{

   private Button submitButton;

   private EditText explain_change, reason_change;

   private RadioButton rb;

   private RadioGroup rg;

   private String REQ_URL = "";

   private LocalStorage localStorage;

   private TextView nameTextView;

   private String username;

   private String name;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_requests);

      rg = (RadioGroup) findViewById(R.id.requests_rg);
      submitButton = (Button) findViewById(R.id.requests_button_submit);
      explain_change = (EditText) findViewById(R.id.requests_EditText_explain);
      reason_change = (EditText) findViewById(R.id.requests_EditText_reason);
      localStorage = new LocalStorage(this);
      nameTextView = (TextView) findViewById(R.id.requests_name);

      submitButton.setOnClickListener(this);

      HashMap<String, String> getUsername = localStorage.getUsername();
      username = getUsername.get(LocalStorage.USERNAME);

      HashMap<String, String> getName = localStorage.getName();
      name = getName.get(LocalStorage.NAME);

      nameTextView.setText(name);
   }

   public void rbClick (View view) {

      int radioButtonId = rg.getCheckedRadioButtonId();
      rb = (RadioButton) findViewById(radioButtonId);

      //Got the RadioButton text -> Need to use it somewhere now --Sagar
   }

   @Override
   public void onClick(View view) {
      if(view == submitButton) {

         String rbString = rb.getTag().toString();
         String explainString = explain_change.toString();
         String reasonString = reason_change.toString();

         if(explainString.isEmpty() && reasonString.isEmpty() && rbString.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Please enter the details",Toast.LENGTH_SHORT).show();
         }

         else if(explainString.isEmpty() && reasonString.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Please fill the fields",Toast.LENGTH_SHORT).show();
         }

         else if(explainString.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Please fill in all the fields",Toast.LENGTH_SHORT).show();
         }

         else if(reasonString.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Please fill in all the fields",Toast.LENGTH_SHORT).show();
         }
         else if(rbString.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Please fill in all the fields",Toast.LENGTH_SHORT).show();
         }
         else {
            sendData(rbString, reasonString, explainString);
         }
      }
   }

   public void sendData(final String requestType , final String reasonChange, final String explainChange) {


      final StringRequest stringRequest = new StringRequest(Request.Method.POST, REQ_URL,
              new Response.Listener<String>() {
                 @Override
                 public void onResponse(String response) {

                    if (response.contains("Successful")) {
                       Toast.makeText(getApplicationContext(), "Request has been successfully submitted", Toast.LENGTH_SHORT).show();

                       Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                       //storage.saveEmail(email);
                       //storage.setLogin();
                       startActivity(i);
                       finish();
                    }

                    if (response.contains("fail")) {

                       Toast.makeText(getApplicationContext(),"Error submitting", Toast.LENGTH_SHORT).show();

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
            Map<String, String> params = new HashMap<String, String>();
            params.put("username",username);
            params.put("reasonType", requestType);
            params.put("reasonChange", reasonChange);
            params.put("explainChange", explainChange);
            return params;

         }


      };

      RequestQueue requestQue = Volley.newRequestQueue(this);
      requestQue.add(stringRequest);


   }
}
