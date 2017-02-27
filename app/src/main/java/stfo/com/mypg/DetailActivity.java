package stfo.com.mypg;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import stfo.com.mypg.Adapters.FacilitiesAdapter;
import stfo.com.mypg.Util.Utils;
import stfo.com.mypg.pojo.Facility;
import stfo.com.mypg.pojo.PG;

/**
 * Created by Kartik Sharma on 21/02/17.
 */
public class DetailActivity extends AppCompatActivity {

    Context context;
    RecyclerView recyclerView;
    TextView tv_Price, tv_location, tv_contact, tv_address;
    ImageView imageView;
    PG pg;
    View map, v_activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
    }

    private void init(){
        context = this;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tv_location = (TextView) findViewById(R.id.textView_pg_location);
        tv_Price = (TextView) findViewById(R.id.textView_pg_price);
        tv_contact = (TextView) findViewById(R.id.textView_Contact);
        imageView = (ImageView) findViewById(R.id.imageView_pg);
        tv_address = (TextView) findViewById(R.id.textView_Address);
        map = findViewById(R.id.button_map);
        v_activity = findViewById(R.id.view_main_activity);

        Bundle b = getIntent().getExtras();
        pg = new PG();
        pg.setImage(b.getString(Constants.KEY_PG_IMAGE));
        pg.setLocation(b.getString(Constants.KEY_PG_LOCATION));
        pg.setFacilities(b.getString(Constants.KEY_PG_FACILITIES));
        pg.setPhone(b.getString(Constants.KEY_PG_PHONE));
        pg.setPrice(b.getLong(Constants.KEY_PG_PRICE));
        pg.setLatitude(b.getDouble(Constants.KEY_PG_LATITUDE));
        pg.setLongitude(b.getDouble(Constants.KEY_PG_LONGITIUDE));
        pg.setAddress(b.getString(Constants.KEY_PG_ADDRESS));


        recyclerView.setLayoutManager(new GridLayoutManager(this,3){
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });
        recyclerView.setAdapter(new FacilitiesAdapter(this,parseFacility(pg.getFacilities())));

        Glide.with(this).load(pg.getImage()).asBitmap().listener(new RequestListener<String, Bitmap>() {
            @Override
            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {

                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        int vibrant = palette.getDarkVibrantColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                        setStatusBarColor(vibrant);
                    }
                });

                return false;
            }
        }).into(imageView);
        tv_Price.setText(getString(R.string.price,pg.getPrice()));
        tv_location.setText(pg.getLocation());
        tv_contact.setText(pg.getPhone());
        tv_address.setText(pg.getAddress());

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String geoUri =
                        "http://maps.google.com/maps?q=loc:" + pg.getLatitude() + "," + pg.getLongitude() + " (" + pg.getAddress() + " " + pg.getLocation() + ")";
                Uri gmmIntentUri = Uri.parse(geoUri);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }else{
                    Snackbar.make(v_activity, getString(R.string.no_maps), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setStatusBarColor(int color){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(color);
        }
    }
    ArrayList<Facility> parseFacility(String facility){
        ArrayList<Facility> f = new ArrayList<>();
        String[] index = facility.split(",");
        for(String i : index){
            f.add(
                    Utils.getFacilitywithId(Integer.parseInt(i))
            );
        }
        return f;
    }
}
