package com.example.icoche;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class board extends AppCompatActivity {


    RecyclerView lvRss;
    List<Menu> newsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        lvRss = findViewById(R.id.recycler);
        newsMenu = new ArrayList<>();


        newsMenu.add(new Menu("Mi iCoche","Descripcion",R.drawable.perfil));
        newsMenu.add(new Menu("iAlarma","Descripcion",R.drawable.para_alarma));
        newsMenu.add(new Menu("¿Dónde esta mi iCoche?","Descripcion",R.drawable.para_localizacion));
        newsMenu.add(new Menu("iRescate","Descripcion",R.drawable.para_accidente));
        newsMenu.add(new Menu("GPS","Descripcion",R.drawable.para_gps));
        newsMenu.add(new Menu("Repárame","Descripcion",R.drawable.para_mantenimiento));
        newsMenu.add(new Menu("iGreen","Descripcion",R.drawable.para_contaminacion));



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
                        case 0:
                            Intent a = new Intent(board.this, Perfil.class);
                            startActivity(a);
                            return true;
                        case 1:
                            Intent b = new Intent(board.this, Alarma.class );
                            startActivity(b);
                            return true;
                        case 2:
                            Intent c = new Intent(board.this, Localizacion.class);
                            startActivity(c);
                            return true;
                        case 3:
                            Intent d = new Intent(board.this, Radio.class);
                            startActivity(d);
                            return true;
                        /*case 4:
                            Mensaje();
                            //Intent e = new Intent(board.this, GPS.class);
                            //startActivity(e);
                            return true;
                        case 5:
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

}
