package io.chat.spi.Storage;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sagar Vakkala on 01-09-2017.
 */

public class LocalStorage {

   Context context;

   SharedPreferences pref;

   SharedPreferences.Editor editor;

   int PRIVATE_MODE = 0;

   public static final String PREF_NAME = "SpiPref";

   public LocalStorage(Context context) {

      this.context = context;
      pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
      editor = pref.edit();
   }

}
