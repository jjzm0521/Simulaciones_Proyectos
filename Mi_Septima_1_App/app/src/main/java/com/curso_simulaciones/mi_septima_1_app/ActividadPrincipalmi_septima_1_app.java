package com.curso_simulaciones.mi_septima_1_app;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.curso_simulaciones.mi_septima_1_app.vista.Pizarra;
import com.curso_simulaciones.mi_septima_1_app.objetos_laboratorio.Polea;


public class ActividadPrincipalmi_septima_1_app extends Activity {

    // Pizarra para dibujar
    private Pizarra pizarra;
    // Objetos dibujables para Pizarra
    private Polea polea_1, polea_2, polea_3;
    // Arreglo que contiene las tres poleas
    private Polea poleas[] = new Polea[3];

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

        // Crear objetos de laboratorio
        crearObjetosLaboratorio();
    }

    // Crear los objetos de la interfaz gráfica de usuario (GUI)
    private void crearElementosGui() {
        pizarra = new Pizarra(this);
        pizarra.setBackgroundColor(Color.BLACK);
    }

    // Organizar la distribución de los objetos de la GUI usando administradores de diseño
    private LinearLayout crearGui() {
        // Administrador de diseño
        LinearLayout linear_principal = new LinearLayout(this);
        linear_principal.setOrientation(LinearLayout.VERTICAL);
        linear_principal.setGravity(Gravity.CENTER_HORIZONTAL);
        linear_principal.setGravity(Gravity.FILL);
        linear_principal.setBackgroundColor(Color.rgb(250, 150, 50));

        /* Parámetro de pegada para la pizarra */
        LinearLayout.LayoutParams parametrosPegada = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0
        );
        parametrosPegada.setMargins(50, 50, 50, 50);
        parametrosPegada.weight = 1.0f;

        // Pegar pizarra al contenedor
        linear_principal.addView(pizarra, parametrosPegada);

        return linear_principal;
    }

    /* Crea los objetos polea con su estado inicial */
    private void crearObjetosLaboratorio() {
        /* * El constructor por defecto hace una polea
         * ubicada en (0,0) de radio 100 y color rojo
         */
        polea_1 = new Polea();
        // Agregar al arreglo poleas[]
        poleas[0] = polea_1;

        // Polea ubicada en (600,200) de radio 150 y color rojo
        polea_2 = new Polea(600f, 200f, 150f);
        // Agregar al arreglo poleas[]
        poleas[1] = polea_2;

        // Polea ubicada en (600,600) de radio 150 y color verde
        polea_3 = new Polea(600f, 600f, 150f);
        polea_3.setColorPolea(Color.GREEN);
        // Agregar al arreglo poleas[]
        poleas[2] = polea_3;

        // Estado inicial de la escena: envía el arreglo de poleas a la pizarra.
        pizarra.setEstadoEscena(poleas);
    }
}
