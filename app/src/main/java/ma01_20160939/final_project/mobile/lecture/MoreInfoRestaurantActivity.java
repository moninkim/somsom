package ma01_20160939.final_project.mobile.lecture;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MoreInfoRestaurantActivity extends AppCompatActivity {
    DBHelper helper;
    Cursor cursor;
    String name, hour,phone,address;
    Geocoder geocoder;
    TextView tvName, tvHour, tvPhone;
    MarkerOptions options;
    Marker marker;
    private GoogleMap mGoogleMap;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        Intent intent = getIntent();
        id = intent.getLongExtra("id", 0);
        helper = new DBHelper(this);
        geocoder = new Geocoder(this);

        tvName = (TextView) findViewById(R.id.tvName);
        tvHour = (TextView) findViewById(R.id.tvHour);
        tvPhone = (TextView) findViewById(R.id.tvPhone);

        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {"_id", DBHelper.COL_NAME, DBHelper.COL_HOUR, DBHelper.COL_PHONE, DBHelper.COL_ADDRESS };
        String selection = "_id=?";
        String[] selectArgs = new String[]{String.valueOf(id)};

        cursor = db.query(DBHelper.TABLE_NAME, columns, selection, selectArgs, null, null, null, null);

        while (cursor.moveToNext()) {
            int _id = cursor.getInt(cursor.getColumnIndex("_id"));
            name = cursor.getString(cursor.getColumnIndex(DBHelper.COL_NAME));
            hour = cursor.getString(cursor.getColumnIndex(DBHelper.COL_HOUR));
            phone = cursor.getString(cursor.getColumnIndex(DBHelper.COL_PHONE));
        }

        tvName.setText(name);
        tvHour.setText(hour);
        tvPhone.setText(phone);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapReadyCallBack);



    }

    OnMapReadyCallback mapReadyCallBack = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.606320, 127.041808), 17));

            mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    options = new MarkerOptions()
                            .position(marker.getPosition())
                            .title(marker.getTitle())
                            .snippet(marker.getSnippet());
                    marker = mGoogleMap.addMarker(options);
                    marker.showInfoWindow();
                    return false;
                }
            });

            mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    List<Address> addressList = null;

                    double latitude = latLng.latitude;
                    double longitude = latLng.longitude;

                    try {
                        addressList = geocoder.getFromLocation(latitude, longitude, 10);
                    } catch (IOException e)  { e.printStackTrace(); }

                    if (addressList == null)    {
                        Toast.makeText(MoreInfoRestaurantActivity.this, "no result", Toast.LENGTH_SHORT).show();
                    } else {
                        for (Address address : addressList) {
                            options = new MarkerOptions()
                                    .position(latLng)
                                    .title(address.getAddressLine(0))
                                    .snippet(address.getPostalCode());

                            marker = mGoogleMap.addMarker(options);
                            marker.showInfoWindow();
                        }
                    }
                }
            });
        }
    };

}
