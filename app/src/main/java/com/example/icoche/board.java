package com.example.icoche;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


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
    Double distancia = 0.0;
    Location cero;
    Location fin;
    int flag = 0;
    private String TAG = "VEEEEERRRR";

    Notification n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        lvRss = findViewById(R.id.recycler);
        newsMenu = new ArrayList<>();

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        geocoder = new Geocoder(this, Locale.getDefault());


        newsMenu.add(new Menu("iAlarma",getString(R.string.dp_alarma),R.drawable.para_alarma));
        newsMenu.add(new Menu(getString(R.string.radio),getString(R.string.dp_radio),R.drawable.music));
        newsMenu.add(new Menu("GPS",getString(R.string.dp_gps),R.drawable.para_gps));
        newsMenu.add(new Menu(getString(R.string.reparame),getString(R.string.dp_reparame),R.drawable.para_mantenimiento));
        newsMenu.add(new Menu("iGreen",getString(R.string.dp_iGreen),R.drawable.para_contaminacion));
        newsMenu.add(new Menu(getString(R.string.where),getString(R.string.dp_where),R.drawable.para_localizacion));
        newsMenu.add(new Menu(getString(R.string.icoche),getString(R.string.dp_iCoche),R.drawable.perfil));



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
                        case 3:
                            createNotificationChannel();
                            addNotification();
                            Intent x = new Intent(board.this, RepairActivity.class);
                            startActivity(x);
                            return true;
                        /*case 6:
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

     //if (){

     //}
    }

    public void Mensaje (){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(board.this);
        builder.setTitle(getString(R.string.error));
        builder.setMessage(getString(R.string.mess_error));
        builder.setBackground(getResources().getDrawable(R.drawable.alert_dialog_bg, null));
        builder.setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
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

            fin = loc;
            lblLatitud = loc.getLatitude();
            lblLongitud = loc.getLongitude();

            if (flag == 0){
                cero = loc;
                flag = 1;
            }

            distancia = distancia + cero.distanceTo(fin);
            Log.e(TAG, distancia.toString());


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

    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "CHANNEL_ID")
                        .setSmallIcon(R.drawable.ic_warning_24px) //set icon for notification
                        .setContentTitle(getString(R.string.alert)) //set title of notification
                        .setContentText(getString(R.string.revision))//this is notification message
                        .setAutoCancel(true) // makes auto cancel of notification
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification


        try {
            direccion = geocoder.getFromLocation(lblLatitud, lblLongitud, 1); // 1 representa la cantidad de resultados a obtener
        } catch (IOException e) {
            e.printStackTrace();
        }

        Address dir = direccion.get(0);

        String map = "http://maps.google.com/maps?q=" + dir.getAddressLine(0);
        // Donde direccion es la variable que contiene el string del textview

        //Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //notification message will get at NotificationView
        notificationIntent.putExtra("message", "This is a notification message");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

}
