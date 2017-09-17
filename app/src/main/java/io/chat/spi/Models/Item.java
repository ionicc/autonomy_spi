package io.chat.spi.Models;

/**
 * Created by Sagar Vakkala on 17-09-2017.
 */

public class Item {

   String date;
   String loginTime;
   String logoutTime;

   public Item(String date, String loginTime, String logoutTime) {
      this.date = date;
      this.loginTime = loginTime;
      this.logoutTime = logoutTime;
   }

   public String getDate() {
      return this.date;
   }

   public String getLoginTime() {
      return this.loginTime;
   }

   public String getLogoutTime() {
      return this.logoutTime;
   }
}
