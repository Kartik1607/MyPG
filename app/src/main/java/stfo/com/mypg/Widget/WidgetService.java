package stfo.com.mypg.Widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import stfo.com.mypg.Constants;
import stfo.com.mypg.Util.Utils;
import stfo.com.mypg.pojo.Payment;

/**
 * Created by Kartik Sharma on 27/02/17.
 */
public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetFactory(getApplicationContext(), Utils.CURRENT_EMAIL);
    }
}