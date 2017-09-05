package io.chat.spi.Storage;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;

import java.util.HashMap;

/**
 * Created by Sagar Vakkala on 01-09-2017.
 */

public class LocalStorage {

   Context context;

   SharedPreferences pref;

   SharedPreferences.Editor editor;

   int PRIVATE_MODE = 0;

   public static final String USERNAME = "username";

   public static final String INTIME = "inTime";

   public static final String PREF_NAME = "SpiPref";

   public static final String LOGIN = "login";

   public static final String ID = "id";

   public static final String NAME = "name";

   public LocalStorage(Context context) {

      this.context = context;
      pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
      editor = pref.edit();
   }

   public void saveUsername(String username) {
      editor.putString(USERNAME,username);
      editor.commit();

   }

   public void saveInTime(String inTime) {
      editor.putString(INTIME,inTime);
      editor.commit();

   }

   public Boolean getLoginState() {return pref.getBoolean(LOGIN,false);}

   public void loggedIn() {
      editor.putBoolean(LOGIN,true);
      editor.commit();
   }

   public void logOut() {
      editor.putBoolean(LOGIN,false);
      editor.commit();
   }

   public void setLoginId(String id) {
      editor.putString(ID,id);
      editor.commit();
   }

   public void saveName(String name) {
      editor.putString(NAME,name);
      editor.commit();
   }

   public void logoutSession() {
      editor.remove(USERNAME);
      editor.remove(NAME);
      editor.remove(INTIME);
      editor.remove(ID);
      editor.commit();
   }

   public HashMap<String,String> getId() {
      HashMap<String,String> hashId = new HashMap<>();
      hashId.put(ID,pref.getString(ID,null));
      return hashId;
   }

   public HashMap<String,String> getUsername() {
      HashMap<String,String> hashUsername = new HashMap<>();
      hashUsername.put(USERNAME,pref.getString(USERNAME,null));
      return hashUsername;
   }

   public HashMap<String,String> getName() {
      HashMap<String,String> hashName = new HashMap<>();
      hashName.put(NAME,pref.getString(NAME,null));
      return hashName;
   }

   public HashMap<String,String> getInTime() {
      HashMap<String,String> hashInTime = new HashMap<>();
      hashInTime.put(INTIME,pref.getString(INTIME,null));
      return hashInTime;
   }

}
