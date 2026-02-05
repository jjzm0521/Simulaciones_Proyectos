package com.curso_simulaciones.mitrigesimasegundaapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.curso_simulaciones.mitrigesimasegundaapp.actividades_secundarias.ActividadConfiguracion;
import com.curso_simulaciones.mitrigesimasegundaapp.actividades_secundarias.ActividadDesplegadoraDatos;
import com.curso_simulaciones.mitrigesimasegundaapp.vista.Boton;

import java.io.File;

public class ActividadPrincipalMiTrigesimaSegundaApp extends Activity {

    private int tamanoLetraResolucionIncluida;
    private Boton consultar, ajustes, salir;
    private String ruta = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestionarResolucion();
        crearElementosGUI();
        ViewGroup.LayoutParams parametro_layout_principal = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.setContentView(crearGUI(), parametro_layout_principal);
        existenciaSensor();
        eventos();
        verificacionPermisos();
        crearDirectorioAlmacenamientoDatos();
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
        AlmacenDatosRAM.dimensioReferencia = dimensionReferencia;
        int tamanoLetra = dimensionReferencia / 20;
        tamanoLetraResolucionIncluida = (int) (tamanoLetra / displayMetrics.scaledDensity);
        AlmacenDatosRAM.tamanoLetraResolucionIncluida = tamanoLetraResolucionIncluida;
    }

    private void crearElementosGUI() {
        consultar = new Boton(this);
        consultar.setImagen(R.drawable.consultar);
        ajustes = new Boton(this);
        ajustes.setImagen(R.drawable.configuracion);
        salir = new Boton(this);
        salir.setImagen(R.drawable.salir);
    }

    private LinearLayout crearGUI() {
        LinearLayout linear_layout_principal = new LinearLayout(this);
        linear_layout_principal.setOrientation(LinearLayout.VERTICAL);
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

        int resId = getResources().getIdentifier("imagen_entrada_app_32", "drawable", getPackageName());
        if (resId != 0) {
            Drawable fondo = getResources().getDrawable(resId);
            linear_layout_primera_fila.setBackgroundDrawable(fondo);
        }

        LinearLayout linear_layout_segunda_fila = new LinearLayout(this);
        linear_layout_segunda_fila.setOrientation(LinearLayout.HORIZONTAL);
        linear_layout_segunda_fila.setGravity(Gravity.FILL);
        LinearLayout.LayoutParams parametros_segunda_fila = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametros_segunda_fila.weight = 2.0f;
        linear_layout_segunda_fila.setWeightSum(3.0f);
        linear_layout_segunda_fila.setLayoutParams(parametros_segunda_fila);

        LinearLayout.LayoutParams parametros_pegado_boton = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT);
        parametros_pegado_boton.weight = 1.0f;
        consultar.setLayoutParams(parametros_pegado_boton);
        ajustes.setLayoutParams(parametros_pegado_boton);
        salir.setLayoutParams(parametros_pegado_boton);
        linear_layout_segunda_fila.addView(consultar);
        linear_layout_segunda_fila.addView(ajustes);
        linear_layout_segunda_fila.addView(salir);

        linear_layout_principal.addView(linear_layout_primera_fila);
        linear_layout_principal.addView(linear_layout_segunda_fila);

        return linear_layout_principal;
    }

    private void eventos() {
        consultar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lanzarDatos();
            }
        });

        ajustes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lanzarAjustes();
            }
        });

        salir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void lanzarDatos() {
        Intent intent = new Intent(this, ActividadDesplegadoraDatos.class);
        startActivity(intent);
    }

    private void lanzarAjustes() {
        Intent intent = new Intent(this, ActividadConfiguracion.class);
        startActivity(intent);
    }

    private void crearDirectorioAlmacenamientoDatos() {
        File path = null;
        ruta = "almacen_mis_datos/luxometro/";
        AlmacenDatosRAM.path = ruta;

        // Siempre usar la carpeta pública del almacenamiento
        // Ruta: /storage/emulated/0/almacen_mis_datos/luxometro/
        path = new File(Environment.getExternalStorageDirectory(), ruta);

        if (!path.exists()) {
            path.mkdirs();
        }
    }

    private boolean existenciaSensor() {
        boolean existe = false;
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
            existe = true;
        } else {
            desplegarAviso();
        }
        return existe;
    }

    private void desplegarAviso() {
        Toast.makeText(getApplicationContext(), "SU DISPOSITIVO NO POSEE SENSOR DE LUZ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finish();
    }

    private void verificacionPermisos() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+ (API 30+): Necesita MANAGE_EXTERNAL_STORAGE
            if (!Environment.isExternalStorageManager()) {
                Toast.makeText(this,
                        "Esta app necesita permiso para acceder al almacenamiento. " +
                                "Por favor, habilite 'Permitir acceso a todos los archivos'",
                        Toast.LENGTH_LONG).show();
                try {
                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivity(intent);
                } catch (Exception e) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.setData(android.net.Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android 6-10: Solicitar permisos normales
            int hasReadWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasReadWritePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ActividadPrincipalMiTrigesimaSegundaApp.this,
                        new String[] {
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
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
                        && (grantResults[0] == PackageManager.PERMISSION_GRANTED
                                || (grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED))) {
                    // Permiso concedido
                } else {
                    Toast.makeText(ActividadPrincipalMiTrigesimaSegundaApp.this, "Permiso denegado.",
                            Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
}
