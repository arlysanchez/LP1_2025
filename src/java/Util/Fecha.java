/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Timestamp;

/**
 *
 * @author LAB_REDES
 */
public class Fecha {
    public static Calendar calendar = Calendar.getInstance();
    private static String fecha;

    public Fecha() {
    }
   
    public static String Fecha(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        fecha = sdf.format(calendar.getTime());
        return fecha;
    }
     public static String FechaBD(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        fecha = sdf.format(calendar.getTime());
        return fecha;
    }
      public static Timestamp FechaBD2() {
        Calendar calendar = Calendar.getInstance();
        return new Timestamp(calendar.getTimeInMillis());
    }
}
