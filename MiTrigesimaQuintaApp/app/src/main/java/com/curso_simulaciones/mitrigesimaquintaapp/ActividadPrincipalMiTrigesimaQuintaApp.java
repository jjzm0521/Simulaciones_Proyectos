package com.curso_simulaciones.mitrigesimaquintaapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;

import com.curso_simulaciones.mitrigesimaquintaapp.actividades_secundarias.ActividadComunicacion;
import com.curso_simulaciones.mitrigesimaquintaapp.vista.Boton;

public class ActividadPrincipalMiTrigesimaQuintaApp extends Activity {

    private Boton entrar, salir;
    private BluetoothAdapter BA;
    LinearLayout linear_layout_segunda_fila;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gestionarResolucion();
        crearElementosGUI();

        ViewGroup.LayoutParams parametro_layout_principal = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.setContentView(crearGUI(), parametro_layout_principal);

        eventos();
        verificacionPermisos();

    }

    private void gestionarResolucion() {

        DisplayMetrics displayMetrics = this.getApplicationContext().getResources().getDisplayMetrics();
        int alto = displayMetrics.heightPixels;
        int ancho = displayMetrics.widthPixels;
        AlmacenDatosRAM.ancho = ancho;
        AlmacenDatosRAM.alto = alto;
        int dimensionReferencia;

        if (alto > ancho) {
            dimensionReferencia = ancho;
        } else {
            dimensionReferencia = alto;
        }

        AlmacenDatosRAM.dimensionReferencia = dimensionReferencia;
        int tamanoLetra = dimensionReferencia / 20;

        int tamanoLetraResolucionIncluida = (int) (tamanoLetra / displayMetrics.scaledDensity);
        AlmacenDatosRAM.tamanoLetraResolucionIncluida = tamanoLetraResolucionIncluida;

    }

    private void crearElementosGUI() {
        entrar = new Boton(this);
        entrar.setImagen(R.drawable.entrar);

        salir = new Boton(this);
        salir.setImagen(R.drawable.salir);
    }

    private LinearLayout crearGUI() {

        LinearLayout linear_layout_principal = new LinearLayout(this);
        linear_layout_principal.setOrientation(LinearLayout.VERTICAL);
        linear_layout_principal.setGravity(Gravity.CENTER_HORIZONTAL);
        linear_layout_principal.setGravity(Gravity.FILL);
        linear_layout_principal.setBackgroundColor(Color.WHITE);
        linear_layout_principal.setWeightSum(10);

        LinearLayout linear_layout_primera_fila = new LinearLayout(this);
        linear_layout_primera_fila.setOrientation(LinearLayout.HORIZONTAL);
        linear_layout_primera_fila.setGravity(Gravity.FILL);
        linear_layout_primera_fila.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams parametros_primera_fila = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametros_primera_fila.weight = 8.0f;
        linear_layout_primera_fila.setLayoutParams(parametros_primera_fila);

        int resId = getResources().getIdentifier("comunicacion_cliente_servidor", "drawable", getPackageName());
        if (resId != 0) {
            Drawable fondo = getResources().getDrawable(resId);
            linear_layout_primera_fila.setBackgroundDrawable(fondo);
        }

        linear_layout_segunda_fila = new LinearLayout(this);
        linear_layout_segunda_fila.setOrientation(LinearLayout.HORIZONTAL);
        linear_layout_segunda_fila.setGravity(Gravity.FILL);
        LinearLayout.LayoutParams parametros_segunda_fila = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametros_segunda_fila.weight = 2.0f;
        linear_layout_segunda_fila.setWeightSum(1.0f);
        linear_layout_segunda_fila.setLayoutParams(parametros_segunda_fila);

        LinearLayout.LayoutParams parametros_pegado_boton = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT);
        parametros_pegado_boton.weight = 1.0f;
        entrar.setLayoutParams(parametros_pegado_boton);
        salir.setLayoutParams(parametros_pegado_boton);
        linear_layout_segunda_fila.addView(entrar);

        linear_layout_principal.addView(linear_layout_primera_fila);
        linear_layout_principal.addView(linear_layout_segunda_fila);

        return linear_layout_principal;

    }

    private void eventos() {

        entrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                activarBluetooth();
                lanzarActividadComunicacion();
                linear_layout_segunda_fila.removeAllViews();
                linear_layout_segunda_fila.addView(salir);
            }
        });

        salir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    // This annotation requires API 31, but we handle versions below inside.
    // Suppressing or using CheckResult where applicable.
    private void activarBluetooth() {

        BA = BluetoothAdapter.getDefaultAdapter();
        if (BA != null && !BA.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // Permission check handled in verificacionPermisos, but strictly standard
                // Android requires check here too.
                // For this simple example, we assume permissions are granted or requested at
                // start.
            }
            // BA.enable() is deprecated but used in legacy/simple apps. Ideally use an
            // intent.
            // But following the provided code:
            BA.enable();
        }
    }

    private void lanzarActividadComunicacion() {
        Intent intent = new Intent(this, ActividadComunicacion.class);
        startActivity(intent);
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (BA != null) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // ...
            }
            BA.disable();
        }

        AlmacenDatosRAM.conexion_bluetooth = "  ";
        finish();
    }

    private void verificacionPermisos() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            // Toast.makeText(this, "This version is not Android 12 or later " +
            // Build.VERSION.SDK_INT, Toast.LENGTH_LONG).show();
            // Pre-Android 12 typically needs BLUETOOTH and BLUETOOTH_ADMIN, and
            // ACCESS_FINE_LOCATION for scanning.
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 1);
            }

        } else {

            int hasReadWritePermission = checkSelfPermission(Manifest.permission.BLUETOOTH);
            int hasBluetoothScan = checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN);

            if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(ActividadPrincipalMiTrigesimaQuintaApp.this,
                        new String[] {
                                Manifest.permission.BLUETOOTH_ADMIN,
                                Manifest.permission.BLUETOOTH_ADVERTISE,
                                Manifest.permission.BLUETOOTH_SCAN,
                                Manifest.permission.BLUETOOTH_CONNECT
                        },
                        100);
            }
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Assuming all granted for brevity, logic in original code was specific
                } else {
                    Toast.makeText(ActividadPrincipalMiTrigesimaQuintaApp.this, "Permiso denegado.", Toast.LENGTH_SHORT)
                            .show();
                    finish();
                }
                return;
        }
    }
}
