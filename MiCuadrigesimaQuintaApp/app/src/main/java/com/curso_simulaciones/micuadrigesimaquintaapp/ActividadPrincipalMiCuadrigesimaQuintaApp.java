package com.curso_simulaciones.micuadrigesimaquintaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.curso_simulaciones.micuadrigesimaquintaapp.actividades_secundarias.ActividadComoClientePubMQTT;
import com.curso_simulaciones.micuadrigesimaquintaapp.actividades_secundarias.ActividadComoClienteSubMQTT;
import com.curso_simulaciones.micuadrigesimaquintaapp.actividades_secundarias.ActividadConfiguracion;
import com.curso_simulaciones.micuadrigesimaquintaapp.datos.AlmacenDatosRAM;
import com.curso_simulaciones.micuadrigesimaquintaapp.gui_auxiliares.DialogoSalir;
import com.curso_simulaciones.micuadrigesimaquintaapp.utilidades.Boton;

public class ActividadPrincipalMiCuadrigesimaQuintaApp extends Activity {

    private Boton entrar, salir, ajustes, entrar_pub;
    LinearLayout linear_layout_segunda_fila;

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

        // actualizar preferencias
        actualizarPreferenciasMQTT();

        eventos();

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

        AlmacenDatosRAM.dimensionReferencia = dimensionReferencia;

        // una estimación de un buen tamaño
        int tamanoLetra = dimensionReferencia / 20;

        // tamano de letra para usar acomodado a la resolución de pantalla
        int tamanoLetraResolucionIncluida = (int) (tamanoLetra / displayMetrics.scaledDensity);

        // guardar en el almacen de datos para que otras clases la accedan fácilmente
        AlmacenDatosRAM.tamanoLetraResolucionIncluida = tamanoLetraResolucionIncluida;

    }

    private void crearElementosGUI() {

        entrar = new Boton(this);
        // entrar.setImagen(R.drawable.entrar); // Imagenes no tenemos, usaremos texto
        entrar.setText("ENTRAR (SUB)");

        entrar_pub = new Boton(this);
        entrar_pub.setText("ENTRAR (PUB)");

        salir = new Boton(this);
        // salir.setImagen(R.drawable.salir);
        salir.setText("SALIR");

        ajustes = new Boton(this);
        // ajustes.setImagen(R.drawable.configuracion);
        ajustes.setText("AJUSTES");

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

        // LinearLayout segunda fila
        linear_layout_segunda_fila = new LinearLayout(this);
        linear_layout_segunda_fila.setOrientation(LinearLayout.HORIZONTAL);
        linear_layout_segunda_fila.setGravity(Gravity.FILL);
        linear_layout_segunda_fila.setBackgroundColor(Color.WHITE);
        linear_layout_segunda_fila.setWeightSum(2);

        // LinearLayout segunda fila
        LinearLayout linear_layout_tercera_fila = new LinearLayout(this);
        linear_layout_tercera_fila.setOrientation(LinearLayout.HORIZONTAL);
        linear_layout_tercera_fila.setGravity(Gravity.FILL);
        linear_layout_segunda_fila.setBackgroundColor(Color.WHITE);

        // pegar primera fila al principal
        LinearLayout.LayoutParams parametros_primera_fila = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametros_primera_fila.weight = 3.0f;
        linear_layout_primera_fila.setLayoutParams(parametros_primera_fila);
        linear_layout_principal.addView(linear_layout_primera_fila);

        // pegar segunda fila al principal
        LinearLayout.LayoutParams parametros_segunda_fila = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        // ocupará el 40% de linear_principal
        parametros_segunda_fila.weight = 4.0f;
        linear_layout_segunda_fila.setLayoutParams(parametros_segunda_fila);
        linear_layout_principal.addView(linear_layout_segunda_fila);

        // pegar tercera fila al principal
        LinearLayout.LayoutParams parametros_tercera_fila = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametros_tercera_fila.weight = 3.0f;
        linear_layout_tercera_fila.setLayoutParams(parametros_tercera_fila);
        linear_layout_principal.addView(linear_layout_tercera_fila);

        // pegar botones en segunda fila
        LinearLayout.LayoutParams parametros_pegado_botones = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT);
        parametros_pegado_botones.weight = 1.0f;
        parametros_pegado_botones.setMargins(10, 0, 10, 0);
        linear_layout_segunda_fila.addView(entrar, parametros_pegado_botones);
        linear_layout_segunda_fila.addView(ajustes, parametros_pegado_botones);
        // linear_layout_segunda_fila.addView(salir, parametros_pegado_botones);

        return linear_layout_principal;

    }// fin gui

    private void eventos() {

        entrar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // hacer transición
                Intent i = new Intent(ActividadPrincipalMiCuadrigesimaQuintaApp.this,
                        ActividadComoClienteSubMQTT.class);
                startActivity(i);

            }
        });

        ajustes.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // hacer transición
                Intent i = new Intent(ActividadPrincipalMiCuadrigesimaQuintaApp.this, ActividadConfiguracion.class);
                startActivity(i);
            }
        });

        salir.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // alerta();
                DialogoSalir dialogo_salir = new DialogoSalir(ActividadPrincipalMiCuadrigesimaQuintaApp.this);
                dialogo_salir.mostrarPopMenuCoeficientes();
            }
        });

    }

    private void actualizarPreferenciasMQTT() {

        SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        String broker = prefs.getString("broker", AlmacenDatosRAM.MQTTHOST);
        AlmacenDatosRAM.MQTTHOST = broker;

        String usuario = prefs.getString("usuario", AlmacenDatosRAM.USERNAME);
        AlmacenDatosRAM.USERNAME = usuario;

        String pasword = prefs.getString("pasword", AlmacenDatosRAM.PASSWORD);
        AlmacenDatosRAM.PASSWORD = pasword;

        String topico = prefs.getString("topico", AlmacenDatosRAM.topicStr);
        AlmacenDatosRAM.topicStr = topico;

        int n_datos = prefs.getInt("n_datos", AlmacenDatosRAM.nDatos);
        AlmacenDatosRAM.nDatos = n_datos;

        int n_datos_graficar = prefs.getInt("n_datos_graficar", AlmacenDatosRAM.nDatosGraficar);
        AlmacenDatosRAM.nDatosGraficar = n_datos_graficar;
    }

    protected void onResume() {
        super.onResume();
        actualizarPreferenciasMQTT();
    }

}
