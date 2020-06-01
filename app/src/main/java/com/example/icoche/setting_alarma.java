package com.example.icoche;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class setting_alarma extends AppCompatActivity {

    SwitchMaterial aux1;
    LinearLayoutCompat aux2;
    SwitchMaterial medium;
    SwitchMaterial high;
    EditText t;
    String texto;

    Button btn;

    private static final String TAG = "MyActivity";

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    public static final String SWITCH1 = "switch1";

    private final int PHONE_CALL_CODE = 10;
    int CurrentSDKVersion=Build.VERSION.SDK_INT;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_alarma);

        new MaterialAlertDialogBuilder(this)
                .setTitle(getResources().getString(R.string.Warning))
                .setMessage(getResources().getString(R.string.Text_notify))

                .show();
        aux1 = findViewById(R.id.switchMaterial);
        aux2 = findViewById(R.id.linearLayoutCompat);
        aux2.setVisibility(View.INVISIBLE);

        aux1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    //aux2.setVisibility(View.VISIBLE);
                    if (IsOlderPermissionVersion()) {
                        //Estamos en una versión vieja
                        if (ChekPermission(Manifest.permission.CALL_PHONE))
                            aux2.setVisibility(View.VISIBLE);
                        else
                            Toast.makeText(getApplicationContext(), "You don't have permissions to run this action", Toast.LENGTH_LONG).show();

                    } else {
                        //Estamos en una versión nueva, tenemos que utilizar requestPermissions() que es un método asincrono que pregunta al
                        //usuario si da su permiso.
                        //Tras su respuesta se lanzará un callback llamado onRequestPermisionsResults, que tendremos que sobrecargar en nuestra activity.
                        //Pasamos un array con los permisos que solicitamos y un código de solicitud que definimos nosotros
                        if (ChekPermission(Manifest.permission.CALL_PHONE)) {
                            //Ha aceptado el permiso.
                            aux2.setVisibility(View.VISIBLE);
                        }
                        else{
                            //O ha denegado o no se le ha pregundado nunca
                            if (!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
                                //No se le ha preguntado aún, le preguntamos ahora
                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
                                //... el flujo de ejecución termina y nos lleva al callback donde realizaremos la acción
                            }
                            else{
                                //Ha denegado previamente. Puede activar el permiso desde Settings->Apps->Permissions
                                Toast.makeText(getApplicationContext(),"Por favor, activa el permiso para poder realizar la llamada",Toast.LENGTH_LONG).show();
                                //Pero también podemos llevarle al sitio donde debe activarlo, lo hacemos con un implicit intent.
                                Intent intentSettings=new Intent( Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                //de la categoría default
                                intentSettings.addCategory(Intent.CATEGORY_DEFAULT);
                                //y como datos un uri que apunta a nuestra aplicación. La obtenemos así:
                                intentSettings.setData(Uri.parse("package:"+getPackageName()));
                                intentSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intentSettings.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intentSettings.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                startActivity(intentSettings);
                            }

                        }
                    }
                } else {
                    // The toggle is disabled
                    aux2.setVisibility(View.INVISIBLE);
                }
            }
        });

        medium = findViewById(R.id.sensibilidad_media);
        medium.setChecked(true);
        medium.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    high.setChecked(false);
                } else {
                    // The toggle is disabled
                    high.setChecked(true);
                }
            }
        });

        high = findViewById(R.id.sensibilidad_alta);
        high.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    medium.setChecked(false);
                } else {
                    // The toggle is disabled
                    medium.setChecked(true);
                }
            }
        });

        btn = findViewById(R.id.containedButton);
        t = findViewById(R.id.textInput);

        btn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                //texto = t.getText().toString();
                if (high.isChecked())
                {
                    //guardar
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(TEXT, t.getText().toString());
                    editor.putBoolean(SWITCH1, false);
                    editor.commit();


                }else{
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(TEXT, t.getText().toString());
                    editor.putBoolean(SWITCH1, true);
                    editor.commit();
                }


                Intent a = new Intent(setting_alarma.this, Alarma.class );
                startActivity(a);
            }
        });




    }
    private boolean IsOlderPermissionVersion(){
        boolean older=false;
        int CurrentSDKVersion= Build.VERSION.SDK_INT;
        int MarshmallowSDKVersion=Build.VERSION_CODES.M;
        if (CurrentSDKVersion<MarshmallowSDKVersion) older=true;
        return older;
    }

    private boolean ChekPermission(String permission){
        //Este método comprueba si el persmiso que se pasa está declarado o disponemos de el
        //Si el permiso está granted (está declarado con uses-permission) devolverá true.
        int result = this.checkCallingOrSelfPermission(permission);
        return result== PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Este método callback se pone en ejecución tras cualquier llamada previa a requestPermissions()
        //Recibimos: el código de petición que pasamos al llamar
        //           un array de permisos solicitados
        //           un array de resultados (granted or denied) correspondiente al array de permisos solicitados
        switch (requestCode) {
            case PHONE_CALL_CODE:
                //Chequeamos el array de permisos, en este caso solo solicitamos uno, el índice cero del array.
                //Podríamos iterar en bucle si hubiese más...
                String permission = permissions[0];
                int result = grantResults[0];
                if (permission.equals(Manifest.permission.CALL_PHONE)) {
                    //El primer permiso es el CALL_PHONE. Esto ya lo sabíamos pero lo comprobamos :-)
                    //Comprobamos si ha sido aceptado o denegado.
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        //Permiso concedido
                        //Obtenemos el número, montamos el intent y lo lanzamos.

                        //Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + texto));
                        //AndroidStudio avisa que hay que poner esta comprobación, que es para determinar si
                        //tenemos el permiso asignado.
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        aux2.setVisibility(View.VISIBLE);
                    }
                    else {
                        //Permiso denegado
                        Toast.makeText(this, "No se concedió el permiso", Toast.LENGTH_LONG).show();
                    }
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }


}

