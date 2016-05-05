package thronlie.balinasoft.by.sacredheartschoolthronlie.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import thronlie.balinasoft.by.sacredheartschoolthronlie.R;
import thronlie.balinasoft.by.sacredheartschoolthronlie.connection_retrofit.RetrofitClient;
import thronlie.balinasoft.by.sacredheartschoolthronlie.constants.Constants;
import thronlie.balinasoft.by.sacredheartschoolthronlie.objects_for_retrofit.ContactsObject;
import thronlie.balinasoft.by.sacredheartschoolthronlie.utils.ServerObserver;

public class ContactUsActivity extends AppCompatActivity implements ServerObserver<Object, String>,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap map;

    private WebView myWebView;

    private RetrofitClient client;

    public ArrayList<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.caldroid_holo_blue_dark,
            R.color.caldroid_holo_blue_dark, R.color.caldroid_holo_blue_dark,
            R.color.caldroid_holo_blue_dark, R.color.caldroid_holo_blue_dark};

    private LatLng start, end;

    private GoogleApiClient mGoogleApiClient;

    //Google Maps  coordinates:
    public static final double X = -32.0678738;
    public static final double Y = 115.9565058;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.contact_us));
        actionBar.setDisplayHomeAsUpEnabled(true);
        Spannable text = new SpannableString(actionBar.getTitle());
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);

        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.color_blue), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        ImageView btnHome = (ImageView) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactUsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnAlerts = (ImageView) findViewById(R.id.btnAlerts);
        btnAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactUsActivity.this, AlertsMenuActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnFeed = (ImageView) findViewById(R.id.btnFeed);
        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactUsActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnCalendar = (ImageView) findViewById(R.id.btnCalendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactUsActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        start = new LatLng(Constants.myX, Constants.myY);
        end = new LatLng(X, Y);

        Button btnRoute = (Button) findViewById(R.id.btnRoute);
        btnRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Routing routing = new Routing.Builder()
                        .travelMode(Routing.TravelMode.DRIVING)
                        .withListener(new RoutingListener() {
                            @Override
                            public void onRoutingFailure(RouteException e) {

                            }

                            @Override
                            public void onRoutingStart() {

                            }

                            @Override
                            public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

                                CameraUpdate center = CameraUpdateFactory.newLatLng(start);
                                CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
                                map.moveCamera(center);

                                //if(polylines.size()>0) {
                                //   for (Polyline poly : polylines) {
                                //        poly.remove();
                                //    }
                                //}

                                polylines = new ArrayList<>();
                                //add route(s) to the map.
                                for (int i = 0; i < route.size(); i++) {

                                    //In case of more than 5 alternative routes
                                    int colorIndex = i % COLORS.length;

                                    PolylineOptions polyOptions = new PolylineOptions();
                                    polyOptions.color(getResources().getColor(COLORS[colorIndex]));
                                    polyOptions.width(10 + i * 3);
                                    polyOptions.addAll(route.get(i).getPoints());
                                    Polyline polyline = map.addPolyline(polyOptions);
                                    polylines.add(polyline);

                                }

                                // Start marker
                                MarkerOptions options = new MarkerOptions();
                                options.position(start);
                                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.disable_cell));
                                if (null != map) {
                                    map.addMarker(new MarkerOptions()
                                                    .position(new LatLng(Constants.myX, Constants.myY))
                                                    .draggable(true)
                                    );
                                }

                                // End marker
                                options = new MarkerOptions();
                                options.position(end);
                                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow_black));
                                if (null != map) {
                                    map.addMarker(new MarkerOptions()
                                                    .position(new LatLng(X, Y))
                                                    .draggable(true)
                                    );
                                }
                            }

                            @Override
                            public void onRoutingCancelled() {

                            }
                        })
                        .waypoints(start, end)
                        .build();
                routing.execute();
            }
        });

        client = new RetrofitClient();
        client.getContacts();
        client.setServerObserver(this);

        createMapView();
        addMarker();
        setCamera();
        createGoogleApiClient();
    }

    private void createGoogleApiClient(){

        if (mGoogleApiClient == null){
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mGoogleApiClient.connect();
    }

    private void createMapView() {
        try {
            if (null == map) {
                map = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapView)).getMap();

                if (null == map) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception) {
            Log.e("mapApp", exception.toString());
        }
    }

    private void addMarker() {
        /** Make sure that the map has been initialised **/
        if (null != map) {
            map.addMarker(new MarkerOptions()
                            .position(new LatLng(X, Y))
                            .draggable(true)
            );
        }
    }

    private void setCamera() {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(X, Y))
                .zoom(15)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.moveCamera(cameraUpdate);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings_inactive, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void successExecuteObject(Object obj) {

        ContactsObject contactObject = (ContactsObject) obj;

        myWebView = (WebView) this.findViewById(R.id.webView);
        myWebView.getSettings().setJavaScriptEnabled(true);

        String html = "<html><body></body></html>";
        html = contactObject.contact_text;

        String mime = "text/html";
        String encoding = "utf-8";
        myWebView.loadDataWithBaseURL(null, html, mime, encoding, null);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(contactObject.contact_title);
    }

    @Override
    public void failedExecute(String err) {

    }

    @Override
    public void onConnected(Bundle connectedHint) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null){
            Log.d("rklogs", "location.getLatitude()_" + mLastLocation.getLatitude());
            Log.d("rklogs", "location.getLongitude()_" + mLastLocation.getLongitude());

            Constants.myX = mLastLocation.getLatitude();
            Constants.myY = mLastLocation.getLongitude();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
