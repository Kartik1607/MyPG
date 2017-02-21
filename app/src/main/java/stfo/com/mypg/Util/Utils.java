package stfo.com.mypg.Util;

import android.content.ContentValues;
import android.content.Context;
import android.support.design.widget.Snackbar;

import stfo.com.mypg.Constants;
import stfo.com.mypg.R;
import stfo.com.mypg.pojo.Facility;

/**
 * Created by Kartik Sharma on 21/02/17.
 */
public class Utils {

    public static Facility getFacilitywithId(int id){
        Facility f = null;
        switch (id){
            case 0 :  f = new Facility("AC", R.drawable.ic_ac_unit); break;
            case 1 :  f = new Facility("Food", R.drawable.ic_food); break;
            case 2 :  f = new Facility("Hot Water", R.drawable.ic_hot_water); break;
            case 3 :  f = new Facility("Power Backup", R.drawable.ic_power); break;
            case 4 :  f = new Facility("TV", R.drawable.ic_tv); break;
            case 5 :  f = new Facility("Wifi", R.drawable.ic_wifi); break;
            case 6 :  f = new Facility("Washing Machine", R.drawable.ic_washing_machine); break;
            case 7 :  f = new Facility("Parking", R.drawable.ic_local_parking); break;
            case 8 :  f = new Facility("House Keeping", R.drawable.ic_house_keeping); break;
        }
        return f;
    }

}
