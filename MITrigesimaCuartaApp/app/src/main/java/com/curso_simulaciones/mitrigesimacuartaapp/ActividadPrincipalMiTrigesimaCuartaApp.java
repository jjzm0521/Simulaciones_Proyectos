package com.curso_simulaciones.mitrigesimacuartaapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.curso_simulaciones.mitrigesimacuartaapp.actividades_secundarias.ActividadConfiguracion;
import com.curso_simulaciones.mitrigesimacuartaapp.actividades_secundarias.ActividadDesplegadoraDatos;
import com.curso_simulaciones.mitrigesimacuartaapp.datos.AlmacenDatosRAM;
import com.curso_simulaciones.mitrigesimacuartaapp.utilidades.Boton;

import java.io.File;

/**
 * La clase ActividadPrincipalMiTrigesimaCuartaApp.
 * Actividad principal que recolecta datos del acelerómetro.
 */
public class ActividadPrincipalMiTrigesimaCuartaApp extends Activity {

    private int tamanoLetraResolucionIncluida;

    private Boton consultar, ajustes, salir;

    private String ruta = null;

    private static final int REQUEST_MANAGE_STORAGE = 101;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gestionarResolucion();

        // para crear elementos de la GUI
        crearElementosGUI();

        /*
         * Para informar cómo se debe pegar el administrador de
         * diseño LinearLayout obtenido con el método crearGui()
         */
        ViewGroup.LayoutParams parametro_layout_principal = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // pegar el contenedor con la GUI
        this.setContentView(crearGUI(), parametro_layout_principal);

        existenciaSensor();

        eventos();

        // esto es necesario hacerlo a partir de la API 23 de android
        verificacionPermisos();

        // crear los directorio para almecenar datos
        crearDirectorioAlmacenamientoDatos();

    }// fin del método onCreate

    /* Método auxiliar para asuntos de resolución */
    private void gestionarResolucion() {

        // independencia de la resolución de la pantalla
        DisplayMetrics displayMetrics = this.getApplicationContext().getResources().getDisplayMetrics();
        int alto = displayMetrics.heightPixels;
        int ancho = displayMetrics.widthPixels;
        AlmacenDatosRAM.ancho = ancho;
        AlmacenDatosRAM.alto = alto;
        int dimensionReferencia;

        // tomar el menor valor entre alto y ancho de pantalla
        if (alto > ancho) {

            dimensionReferencia = ancho;
        } else {

            dimensionReferencia = alto;
        }

        AlmacenDatosRAM.dimensioReferencia = dimensionReferencia;

        // una estimación de un buen tamaño
        int tamanoLetra = dimensionReferencia / 20;

        // tamano de letra para usar acomodado a la resolución de pantalla
        tamanoLetraResolucionIncluida = (int) (tamanoLetra / displayMetrics.scaledDensity);

        // guardar en el almacen de datos para que otras clases la accedan fácilmente
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

    /* método responsable de administrar el diseño de la GUI */
    private LinearLayout crearGUI() {

        LinearLayout linear_layout_principal = new LinearLayout(this);
        linear_layout_principal.setOrientation(LinearLayout.VERTICAL);
        linear_layout_principal.setGravity(Gravity.CENTER_HORIZONTAL);
        linear_layout_principal.setGravity(Gravity.FILL);
        linear_layout_principal.setBackgroundColor(Color.WHITE);
        linear_layout_principal.setWeightSum(10);

        // LinearLayout primera fila
        LinearLayout linear_layout_primera_fila = new LinearLayout(this);
        linear_layout_primera_fila.setOrientation(LinearLayout.HORIZONTAL);
        linear_layout_primera_fila.setGravity(Gravity.FILL);
        linear_layout_primera_fila.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams parametros_primera_fila = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametros_primera_fila.weight = 8.0f;
        linear_layout_primera_fila.setLayoutParams(parametros_primera_fila);

        // fondo primera fila
        try {
            Drawable fondo = getResources().getDrawable(R.drawable.imagen_entrada_app_34);
            linear_layout_primera_fila.setBackgroundDrawable(fondo);
        } catch (Exception e) {
            // Si no hay imagen, usar color de fondo
            linear_layout_primera_fila.setBackgroundColor(Color.rgb(30, 60, 100));
        }

        // LinearLayout segunda fila
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

        ruta = "almacen_mis_datos/acelerometro/";
        AlmacenDatosRAM.path = ruta;

        // Usar almacenamiento externo para todas las versiones
        path = new File(Environment.getExternalStorageDirectory(), ruta);
        if (!path.exists()) {
            boolean created = path.mkdirs();
            if (created) {
                Toast.makeText(this, "Directorio creado: " + ruta, Toast.LENGTH_SHORT).show();
            }
        }

    }

    private boolean existenciaSensor() {

        boolean existe = false;
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {

            existe = true;

        } else {

            desplegarAviso();

        }

        return existe;

    }

    private void desplegarAviso() {

        Toast toast = Toast.makeText(getApplicationContext(),
                "SU DISPOSITIVO NO POSEE SENSOR DE ACELERÓMETRO", Toast.LENGTH_SHORT);
        toast.show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finish();
    }

    /*
     * Los siguientes métodos son para confirmar los permisos.
     * Para Android 11+ se requiere permiso MANAGE_EXTERNAL_STORAGE
     */

    private void verificacionPermisos() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+ necesita permiso especial de gestión de almacenamiento
            if (!Environment.isExternalStorageManager()) {
                Toast.makeText(this,
                        "Se requiere permiso de acceso a todos los archivos.\nSe abrirá la configuración.",
                        Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_MANAGE_STORAGE);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android 6-10
            int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[] {
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        100);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MANAGE_STORAGE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    Toast.makeText(this, "Permiso concedido. Creando directorio...", Toast.LENGTH_SHORT).show();
                    crearDirectorioAlmacenamientoDatos();
                } else {
                    Toast.makeText(this, "Permiso denegado. No se podrán guardar archivos.", Toast.LENGTH_LONG).show();
                }
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
                    crearDirectorioAlmacenamientoDatos();
                } else {
                    Toast.makeText(this, "Permiso denegado.", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

}
