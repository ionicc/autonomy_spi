package io.chat.spi.Storage;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;

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

   public void loggedIn() {
      editor.putBoolean(LOGIN,true);
      editor.commit();
   }

   public void loggedOut() {
      editor.putBoolean(LOGIN,false);
      editor.commit();
   }

}
