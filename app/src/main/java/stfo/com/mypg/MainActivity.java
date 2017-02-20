package stfo.com.mypg;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    //TODO : Strings in strimgs.xml.
    //TODO : Save when screen rotates.

    FragmentManager fragmentManager;
    Fragment pg_overview_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        pg_overview_fragment = new Fragement_PG_Overview();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.frame, pg_overview_fragment)
                .commit();
    }

}
