package io.chat.spi.Adapters;

import android.content.ClipData;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import io.chat.spi.Models.Item;
import io.chat.spi.R;

/**
 * Created by Sagar Vakkala on 17-09-2017.
 */

public class CustomAdapter extends ArrayAdapter<Item> {

   ArrayList<Item> arrayList = new ArrayList<>();
   LayoutInflater inflater;

   public CustomAdapter(Context context, int resource, ArrayList<Item> objects) {
      super(context, resource, objects);
      arrayList = objects;
   }

   @Override
   public int getCount() {
      return super.getCount();
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {


      Item item = getItem(position);
      if(convertView == null) {
         convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_items,parent,false);
      }
      TextView date = (TextView) convertView.findViewById(R.id.list_date);
      TextView loginTime = (TextView) convertView.findViewById(R.id.list_loginTime);
      TextView logoutTime = (TextView) convertView.findViewById(R.id.list_logoutTime);

      date.setText(item.getDate());
      loginTime.setText(item.getLoginTime());
      logoutTime.setText(item.getLogoutTime());
      return convertView;
      /*
      TextView date = (TextView) v.findViewById(R.id.list_date);
      TextView loginTime = (TextView) v.findViewById(R.id.list_loginTime);
      TextView logoutTime = (TextView) v.findViewById(R.id.list_logoutTime);
      date.setText(arrayList.get(position).getDate());
      loginTime.setText(arrayList.get(position).getLoginTime());
      logoutTime.setText(arrayList.get(position).getLogoutTime());
      return super.getView(position, convertView, parent);
      */


   }
}
