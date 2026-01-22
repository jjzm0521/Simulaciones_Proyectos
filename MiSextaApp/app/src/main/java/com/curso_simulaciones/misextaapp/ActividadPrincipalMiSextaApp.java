package com.curso_simulaciones.misextaapp;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.curso_simulaciones.misextaapp.componentes.Reloj;

public class ActividadPrincipalMiSextaApp extends Activity {

    private Reloj reloj_1, reloj_2, reloj_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Llamada al método para crear los elementos de la interfaz gráfica de usuario (GUI) */
        crearElementosGui();

        /* Para informar cómo se debe adaptar la GUI a la pantalla del dispositivo */
        ViewGroup.LayoutParams parametro_layout_principal = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        /* Pegar al contenedor la GUI: en el argumento se está llamando al método crearGui() */
        this.setContentView(crearGui(), parametro_layout_principal);
    }

    /* Crear los objetos de la interfaz gráfica de usuario (GUI) */
    private void crearElementosGui() {
        // Crear objeto Reloj 1 (Default)
        reloj_1 = new Reloj(this);

        // Crear objeto Reloj 2 (Personalizado en tonos amarillos/naranja)
        reloj_2 = new Reloj(this);
        reloj_2.setColorAgujaHorario(Color.YELLOW);
        reloj_2.setColorAgujaMinutero(Color.YELLOW);
        reloj_2.setColorAgujaSegundero(Color.YELLOW);
        reloj_2.setColorFondo(Color.rgb(250, 150, 0));

        // Crear objeto Reloj 3 (Personalizado con fondo semitransparente)
        reloj_3 = new Reloj(this);
        reloj_3.setColorAgujaHorario(Color.GREEN);
        reloj_3.setColorFondo(Color.argb(50, 200, 200, 0));
    }

    /* Organizar la distribución de los objetos de la GUI usando administradores de diseño */
    private LinearLayout crearGui() {
        // Administrador de diseño principal (Horizontal)
        LinearLayout linear_principal = new LinearLayout(this);
        linear_principal.setOrientation(LinearLayout.HORIZONTAL);
        linear_principal.setGravity(Gravity.FILL);
        linear_principal.setBackgroundColor(Color.rgb(250, 150, 50));
        linear_principal.setWeightSum(3);

        // Contenedor izquierdo
        LinearLayout linear_izquierdo = new LinearLayout(this);
        linear_izquierdo.setOrientation(LinearLayout.VERTICAL);
        linear_izquierdo.setGravity(Gravity.FILL);
        linear_izquierdo.setBackgroundColor(Color.WHITE);

        // Contenedor central
        LinearLayout linear_centro = new LinearLayout(this);
        linear_centro.setOrientation(LinearLayout.VERTICAL);
        linear_centro.setGravity(Gravity.FILL);
        linear_centro.setBackgroundColor(Color.WHITE);

        // Contenedor derecho
        LinearLayout linear_derecho = new LinearLayout(this);
        linear_derecho.setOrientation(LinearLayout.VERTICAL);
        linear_derecho.setGravity(Gravity.FILL);
        linear_derecho.setBackgroundColor(Color.WHITE);

        // Parámetros para pegar los relojes (Gauges) dentro de sus columnas
        LinearLayout.LayoutParams parametrosPegadaGauges = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametrosPegadaGauges.setMargins(20, 20, 20, 20);
        parametrosPegadaGauges.weight = 1.0f;

        // Pegar relojes en sus respectivos contenedores
        linear_izquierdo.addView(reloj_1, parametrosPegadaGauges);
        linear_centro.addView(reloj_2, parametrosPegadaGauges);
        linear_derecho.addView(reloj_3, parametrosPegadaGauges);

        // Parámetros para pegar las columnas al contenedor principal
        LinearLayout.LayoutParams parametrosPegadaLinear = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT);
        parametrosPegadaLinear.setMargins(20, 20, 20, 20);
        parametrosPegadaLinear.weight = 1.0f;

        // Agregar las tres columnas al layout principal
        linear_principal.addView(linear_izquierdo, parametrosPegadaLinear);
        linear_principal.addView(linear_centro, parametrosPegadaLinear);
        linear_principal.addView(linear_derecho, parametrosPegadaLinear);

        return linear_principal;
    }
}