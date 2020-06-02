package com.example.icoche;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.lang.Thread.sleep;

public class board extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {


    RecyclerView lvRss;
    List<Menu> newsMenu;
    private static final String LOGTAG = "android-localizacion";

    private static final int PETICION_PERMISO_LOCALIZACION = 101;

    GoogleApiClient apiClient;

    Double lblLatitud;
    Double lblLongitud;
    Geocoder geocoder;
    List<Address> direccion;
    Double distancia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        lvRss = findViewById(R.id.recycler);
        newsMenu = new ArrayList<>();

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        geocoder = new Geocoder(this, Locale.getDefault());





        newsMenu.add(new Menu("iAlarma","Descripcion",R.drawable.para_alarma));
        newsMenu.add(new Menu("Mi Radio","Descripcion",R.drawable.music));
        newsMenu.add(new Menu("GPS","Descripcion",R.drawable.para_gps));
        newsMenu.add(new Menu("Repárame","Descripcion",R.drawable.para_mantenimiento));
        newsMenu.add(new Menu("iGreen","Descripcion",R.drawable.para_contaminacion));
        newsMenu.add(new Menu("¿Dónde esta mi iCoche?","Descripcion",R.drawable.para_localizacion));
        newsMenu.add(new Menu("Mi iCoche","Descripcion",R.drawable.perfil));



        lvRss.setAdapter(new NewsItemsAdapter(newsMenu));

     final GestureDetector mGestureDetector = new GestureDetector(board.this, new GestureDetector.SimpleOnGestureListener() {
        @Override public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }
     });
     lvRss.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
        Menu m;
        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean b) {

        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            try {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {

                    int position = recyclerView.getChildAdapterPosition(child);

                    m = newsMenu.get(position);
                    switch (position) {
                        /*case 0:
                            Intent a = new Intent(board.this, Perfil.class);
                            startActivity(a);
                            return true;*/
                        case 0:
                            Intent b = new Intent(board.this, Alarma.class );
                            startActivity(b);
                            return true;
                        /*case 1:
                            Intent c = new Intent(board.this, Localizacion.class);
                            startActivity(c);
                            return true;*/
                        case 1:
                            Intent d = new Intent(board.this, Radio.class);
                            startActivity(d);
                            return true;
                        case 2:
                            //String direccion = "38.402022";
                            //String direccion1 = "-0.523918";
                            try {
                                direccion = geocoder.getFromLocation(lblLatitud, lblLongitud, 1); // 1 representa la cantidad de resultados a obtener
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Address dir = direccion.get(0);

                            String map = "http://maps.google.com/maps?q=" + dir.getAddressLine(0);
                            // Donde direccion es la variable que contiene el string del textview

                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                            startActivity(i);
                            return true;
                        /*case 5:
                            Mensaje();
                            //Intent f = new Intent(board.this, Reparame.class);
                            //startActivity(f);
                            return true;
                        case 6:
                            Mensaje();
                            //Intent g = new Intent(board.this, Green.class);
                            //startActivity(g);
                            return true;*/
                        default:
                            Mensaje();
                            return true;
                    }
                    //mp.pause();

                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

        }
     });



    }

    public void Mensaje (){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(board.this);
        builder.setTitle("Error");
        builder.setMessage("Esta funcion todavia no esta disponible.");
        builder.setBackground(getResources().getDrawable(R.drawable.alert_dialog_bg, null));
        builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do something when clicked
            }
        });

        builder.show();

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Conectado correctamente a Google Play Services

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        } else {

            Location lastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(apiClient);

            updateUI(lastLocation);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        //Se ha interrumpido la conexión con Google Play Services

        Log.e(LOGTAG, "Se ha interrumpido la conexión con Google Play Services");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Se ha producido un error que no se puede resolver automáticamente
        //y la conexión con los Google Play Services no se ha establecido.

        Log.e(LOGTAG, "Error grave al conectar con Google Play Services");

    }
    private void updateUI(Location loc) {
        if (loc != null) {
            lblLatitud = loc.getLatitude();
            lblLongitud = loc.getLongitude();
        } /*else {
            lblLatitud.setText("Latitud: (desconocida)");
            lblLongitud.setText("Longitud: (desconocida)");
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PETICION_PERMISO_LOCALIZACION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Permiso concedido

                @SuppressWarnings("MissingPermission")
                Location lastLocation =
                        LocationServices.FusedLocationApi.getLastLocation(apiClient);

                updateUI(lastLocation);

            } else {
                //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.

                Log.e(LOGTAG, "Permiso denegado");
            }
        }
    }
    public void calculo(){
        new Thread(new Runnable() {
        public void run() {
            try {
                while(true) {
                    sleep(1000);
                    Coordenada coordenada  =getLocation();

                    Location locationA = new Location("point A");

                    locationA.setLatitude(lat);
                    locationA.setLongitude(longi);
                    Location locationB = new Location("point B");

                    locationB.setLatitude(coordenada.getLat());
                    locationB.setLongitude(coordenada.getLongg());

                    distancia = distancia + (locationA.distanceTo(locationB));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }).start();
   }
}
