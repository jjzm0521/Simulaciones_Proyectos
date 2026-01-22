package com.curso_simulaciones.miquintaapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.curso_simulaciones.miquintaapp.componentes.GaugeSimple;

public class ActividadPrincipalMiQuintaApp extends Activity {

    private GaugeSimple tacometro_1, tacometro_2, tacometro_3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        crearElementosGui();

        ViewGroup.LayoutParams parametro_layout_principal = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        this.setContentView(crearGui(), parametro_layout_principal);
    }

    /* crear los objetos de la interfaz gráfica de usuario (GUI) */
    private void crearElementosGui() {

        // Gauge 1: Estilo Figura 46 (Negro/Si, Rojo/Si, Unidades Gauss)
        tacometro_1 = new GaugeSimple(this);
        tacometro_1.setColorFondo(Color.BLACK);
        tacometro_1.setColorBorde(Color.RED);
        tacometro_1.setUnidades("Gauss");
        tacometro_1.setRango(-20, 40); // Rango figura 46 ( -20 a 40)
        tacometro_1.setMedida(32f); // Valor de imagen

        // Ajuste PRECISO para Figura 46:
        // Inicio escala: 90 grados (Abajo/Sur)
        // Azul: 180 grados (de Abajo a Arriba por la Izquierda)
        // Amarillo: 67.5 grados (Arriba a derecha)
        // Verde: 22.5 grados (Relleno final)
        tacometro_1.setAnguloInicio(90f);
        tacometro_1.setColorSectores(Color.BLUE, Color.YELLOW, Color.GREEN);
        tacometro_1.setAngulosSectores(180f, 67.5f, 22.5f);

        tacometro_1.setColorAguja(Color.RED);
        tacometro_1.setColorNumeros(Color.WHITE);
        tacometro_1.setColorUnidades(Color.YELLOW);

        // Gauge 2: Estilo diferente (White/Blue)
        tacometro_2 = new GaugeSimple(this);
        tacometro_2.setColorFondo(Color.WHITE);
        tacometro_2.setColorBorde(Color.BLUE);
        tacometro_2.setUnidades("m/s");
        tacometro_2.setRango(0, 100);
        tacometro_2.setMedida(75f);
        tacometro_2.setAnguloInicio(135f); // Estándar
        tacometro_2.setColorSectores(Color.CYAN, Color.MAGENTA, Color.DKGRAY);
        tacometro_2.setAngulosSectores(100f, 100f, 50f);
        tacometro_2.setColorAguja(Color.BLUE);
        tacometro_2.setColorNumeros(Color.BLACK);
        tacometro_2.setColorUnidades(Color.BLUE);

        // Gauge 3: Estilo diferente (DarkGray/Green)
        tacometro_3 = new GaugeSimple(this);
        tacometro_3.setColorFondo(Color.DKGRAY);
        tacometro_3.setColorBorde(Color.GREEN);
        tacometro_3.setUnidades("°C");
        tacometro_3.setRango(0, 200);
        tacometro_3.setMedida(150f);
        tacometro_3.setAnguloInicio(135f); // Estándar
        tacometro_3.setColorSectores(Color.GREEN, Color.YELLOW, Color.RED);
        tacometro_3.setAngulosSectores(100f, 100f, 50f);
        tacometro_3.setColorAguja(Color.WHITE);
        tacometro_3.setColorNumeros(Color.CYAN);
        tacometro_3.setColorUnidades(Color.GREEN);
    }

    /*
     * organizar la distribución de los objetos de de la GUI usando
     * administradores de diseño
     */
    private LinearLayout crearGui() {

        // administrador de diseño
        LinearLayout linear_principal = new LinearLayout(this);
        linear_principal.setOrientation(LinearLayout.HORIZONTAL);
        linear_principal.setGravity(Gravity.CENTER_HORIZONTAL);
        linear_principal.setGravity(Gravity.FILL);
        linear_principal.setBackgroundColor(Color.GRAY); // Fondo neutro
        linear_principal.setWeightSum(3);

        LinearLayout linear_izquierdo = new LinearLayout(this);
        linear_izquierdo.setOrientation(LinearLayout.VERTICAL);
        linear_izquierdo.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        linear_izquierdo.setPadding(10, 10, 10, 10);
        linear_izquierdo.setBackgroundColor(Color.parseColor("#1a1a2e")); // Azul oscuro

        LinearLayout linear_centro = new LinearLayout(this);
        linear_centro.setOrientation(LinearLayout.VERTICAL);
        linear_centro.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        linear_centro.setPadding(10, 10, 10, 10);
        linear_centro.setBackgroundColor(Color.parseColor("#e8f4f8")); // Azul muy claro

        LinearLayout linear_derecho = new LinearLayout(this);
        linear_derecho.setOrientation(LinearLayout.VERTICAL);
        linear_derecho.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        linear_derecho.setPadding(10, 10, 10, 10);
        linear_derecho.setBackgroundColor(Color.parseColor("#0f3460")); // Azul marino

        // parametro para pegar los gauges (aspecto cuadrado, centrado vertical)
        LinearLayout.LayoutParams parametrosPegadaGauges = new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

        // pegar gauges
        linear_izquierdo.addView(tacometro_1, parametrosPegadaGauges);
        linear_centro.addView(tacometro_2, parametrosPegadaGauges);
        linear_derecho.addView(tacometro_3, parametrosPegadaGauges);

        // parametro para pegar los linear al pricipal
        LinearLayout.LayoutParams parametrosPegadaLinear = new LinearLayout.LayoutParams(0,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        parametrosPegadaLinear.weight = 1.0f;

        linear_principal.addView(linear_izquierdo, parametrosPegadaLinear);
        linear_principal.addView(linear_centro, parametrosPegadaLinear);
        linear_principal.addView(linear_derecho, parametrosPegadaLinear);

        return linear_principal;
    }

}
