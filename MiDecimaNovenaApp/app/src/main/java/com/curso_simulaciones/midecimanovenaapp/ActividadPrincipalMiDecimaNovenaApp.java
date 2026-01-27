package com.curso_simulaciones.midecimanovenaapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.curso_simulaciones.midecimanovenaapp.actividades_secundarias.ActividadSecundaria_1;
import com.curso_simulaciones.midecimanovenaapp.actividades_secundarias.ActividadSecundaria_2;
import com.curso_simulaciones.midecimanovenaapp.actividades_secundarias.ActividadSecundaria_3;
import com.curso_simulaciones.midecimanovenaapp.datos.AlmacenDatosRAM;

public class ActividadPrincipalMiDecimaNovenaApp extends Activity {

    private Button botonUno, botonDos, botonTres, botonCuatro;
    private int tamanoLetraResolucionIncluida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gestionarResolucion();
        crearElementosGUI();

        ViewGroup.LayoutParams parametro_layout_principal = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        this.setContentView(crearGUI(), parametro_layout_principal);

        eventos();
    }

    private void gestionarResolucion() {
        // independencia de la resolución de la pantalla
        DisplayMetrics displayMetrics = this.getApplicationContext().getResources().getDisplayMetrics();
        int alto = displayMetrics.heightPixels;
        int ancho = displayMetrics.widthPixels;
        int dimensionReferencia;

        // tomar el menor valor entre alto y ancho de pantalla
        if (alto > ancho) {
            dimensionReferencia = ancho;
        } else {
            dimensionReferencia = alto;
        }

        // una estimación de un buen tamaño
        int tamanoLetra = dimensionReferencia / 20;

        // tamano de letra para usar acomodado a la resolución de pantalla
        tamanoLetraResolucionIncluida = (int) (tamanoLetra / displayMetrics.scaledDensity);

        // guardar en el almacen de datos para que otras clases la accedan fácilmente
        AlmacenDatosRAM.tamanoLetraResolucionIncluida = tamanoLetraResolucionIncluida;
    }

    /* método responsable de la creación de los elementos de la GUI */
    private void crearElementosGUI() {
        /*
         * 1. El tamaño a usar de la letra tiene corrección de
         * resolución y tamaño de pantalla.
         * 2. Se usa un diseño de color de botón especial
         * PorterDuff.Mode.MULTIPLY.
         */
        botonUno = new Button(this);
        botonUno.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        botonUno.setText("UNO");
        botonUno.getBackground().setColorFilter(Color.rgb(255, 140, 0), PorterDuff.Mode.MULTIPLY); // Naranja

        botonDos = new Button(this);
        botonDos.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        botonDos.setText("DOS");
        botonDos.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);

        botonTres = new Button(this);
        botonTres.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        botonTres.setText("TRES");
        botonTres.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
        botonTres.setEnabled(false);

        botonCuatro = new Button(this);
        botonCuatro.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        botonCuatro.setText("CUATRO");
        botonCuatro.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        botonCuatro.setEnabled(false);
    }

    /* método responsable de administrar el diseño de la GUI */
    private LinearLayout crearGUI() {
        // Contenedor principal
        LinearLayout linearPrincipal = new LinearLayout(this);
        linearPrincipal.setOrientation(LinearLayout.VERTICAL);
        linearPrincipal.setWeightSum(10.0f); // El total vertical es 10 (100%)
        linearPrincipal.setBackgroundColor(Color.YELLOW);

        // --- FILA ARRIBA (30%) ---
        // El "conjunto botones e imagen 1"
        LinearLayout linearArriba = new LinearLayout(this);
        linearArriba.setOrientation(LinearLayout.HORIZONTAL);
        linearArriba.setWeightSum(10.0f); // El total horizontal de esta fila es 10

        LinearLayout.LayoutParams paramsArriba = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        paramsArriba.weight = 3.0f; // Esto es el 30% de la pantalla (vertical)

        // Columna de Botones (20% de la fila arriba)
        LinearLayout linearBotones = new LinearLayout(this);
        linearBotones.setOrientation(LinearLayout.VERTICAL);
        linearBotones.setWeightSum(4.0f); // 4 botones (cada uno 1.0f de peso)
        linearBotones.setBackgroundColor(Color.rgb(150, 200, 150)); // Borde verdoso

        LinearLayout.LayoutParams paramsBotonesCol = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT);
        paramsBotonesCol.weight = 2.0f; // Esto es el 20% horizontal de la fila
        paramsBotonesCol.setMargins(5, 5, 5, 5);

        // Atributos para los botones
        LinearLayout.LayoutParams paramsBoton = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        paramsBoton.weight = 1.0f;
        paramsBoton.setMargins(2, 2, 2, 2);

        // Pegar botones
        linearBotones.addView(botonUno, paramsBoton);
        linearBotones.addView(botonDos, paramsBoton);
        linearBotones.addView(botonTres, paramsBoton);
        linearBotones.addView(botonCuatro, paramsBoton);

        // Espacio para IMAGEN 1 (80% de la fila arriba)
        LinearLayout linearImagen1 = new LinearLayout(this);
        linearImagen1.setBackgroundColor(Color.rgb(255, 165, 79)); // Naranja
        linearImagen1.setGravity(android.view.Gravity.CENTER);

        TextView txtImagen1 = new TextView(this);
        txtImagen1.setText("IMAGEN");
        txtImagen1.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        txtImagen1.setTextColor(Color.BLACK);
        linearImagen1.addView(txtImagen1);

        LinearLayout.LayoutParams paramsImagen1 = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT);
        paramsImagen1.weight = 8.0f; // Esto es el 80% horizontal de la fila
        paramsImagen1.setMargins(5, 5, 5, 5);

        // Pegar columnas a la fila de arriba
        linearArriba.addView(linearBotones, paramsBotonesCol);
        linearArriba.addView(linearImagen1, paramsImagen1);

        // --- FILA ABAJO (70%) ---
        // La "imagen 2"
        LinearLayout linearImagen2 = new LinearLayout(this);
        linearImagen2.setBackgroundColor(Color.CYAN);
        linearImagen2.setGravity(android.view.Gravity.CENTER);

        TextView txtImagen2 = new TextView(this);
        txtImagen2.setText("IMAGEN");
        txtImagen2.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        txtImagen2.setTextColor(Color.BLACK);
        linearImagen2.addView(txtImagen2);

        LinearLayout.LayoutParams paramsImagen2 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        paramsImagen2.weight = 7.0f; // Esto es el 70% de la pantalla (vertical)
        paramsImagen2.setMargins(10, 10, 10, 10);

        // Pegar filas al principal
        linearPrincipal.addView(linearArriba, paramsArriba);
        linearPrincipal.addView(linearImagen2, paramsImagen2);

        return linearPrincipal;
    }

    /* Administra los eventos de la GUI */
    private void eventos() {
        // evento del boton con etiqueta UNO
        botonUno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActividadPrincipalMiDecimaNovenaApp.this, ActividadSecundaria_1.class);
                startActivity(intent);
            }
        });

        // evento del boton con etiqueta DOS
        botonDos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActividadPrincipalMiDecimaNovenaApp.this, ActividadSecundaria_2.class);
                startActivity(intent);
            }
        });

        // evento del boton con etiqueta TRES
        botonTres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActividadPrincipalMiDecimaNovenaApp.this, ActividadSecundaria_3.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Activar botón TRES si hay datos
        if (!AlmacenDatosRAM.nombreImagenActividad1.equals("") || !AlmacenDatosRAM.nombreImagenActividad2.equals("")) {
            botonTres.setEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        // Limpiar datos al salir
        AlmacenDatosRAM.reset();
        this.finish();
        super.onDestroy();
    }
}
