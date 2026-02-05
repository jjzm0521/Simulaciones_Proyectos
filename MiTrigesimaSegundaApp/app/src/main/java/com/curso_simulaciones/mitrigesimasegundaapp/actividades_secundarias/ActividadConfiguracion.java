package com.curso_simulaciones.mitrigesimasegundaapp.actividades_secundarias;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.curso_simulaciones.mitrigesimasegundaapp.AlmacenDatosRAM;

public class ActividadConfiguracion extends Activity {

    private EditText periodo_muestreo, numero_datos;
    private TextView text_periodo_muestreo, text_numero_datos, espacio_1, espacio_2;
    private int tamanoLetraResolucionIncluida;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestionarResolucion();
        crearElementosGUI();
        ViewGroup.LayoutParams parametro_layout_principal = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.setContentView(crearGUI(), parametro_layout_principal);
    }

    private void gestionarResolucion() {
        tamanoLetraResolucionIncluida = (int) (0.6f * AlmacenDatosRAM.tamanoLetraResolucionIncluida);
    }

    private void crearElementosGUI() {
        espacio_1 = new TextView(this);
        espacio_1.setTextSize(tamanoLetraResolucionIncluida);
        espacio_1.setText("    ");

        espacio_2 = new TextView(this);
        espacio_2.setTextSize(tamanoLetraResolucionIncluida);
        espacio_2.setText("    ");

        text_periodo_muestreo = new TextView(this);
        text_periodo_muestreo.setGravity(Gravity.FILL_VERTICAL);
        text_periodo_muestreo.setBackgroundColor(Color.YELLOW);
        text_periodo_muestreo.setTextSize(tamanoLetraResolucionIncluida);
        text_periodo_muestreo.setText("  PERIODO MUESTREO EN ms (Mínimo 50)");
        text_periodo_muestreo.setTextColor(Color.BLACK);

        text_numero_datos = new TextView(this);
        text_numero_datos.setGravity(Gravity.FILL_VERTICAL);
        text_numero_datos.setBackgroundColor(Color.argb(100, 220, 156, 80));
        text_numero_datos.setTextSize(tamanoLetraResolucionIncluida);
        text_numero_datos.setText("  NÚMERO DE DATOS (Maximo 2 000)");
        text_numero_datos.setTextColor(Color.BLACK);

        numero_datos = new EditText(this);
        numero_datos.setKeyListener(DigitsKeyListener.getInstance(false, false));
        numero_datos.setTextSize(tamanoLetraResolucionIncluida);
        numero_datos.setText("" + AlmacenDatosRAM.nDatos);

        periodo_muestreo = new EditText(this);
        periodo_muestreo.setKeyListener(DigitsKeyListener.getInstance(false, false));
        periodo_muestreo.setTextSize(tamanoLetraResolucionIncluida);
        periodo_muestreo.setText("" + AlmacenDatosRAM.periodoMuestreo);
    }

    private LinearLayout crearGUI() {
        LinearLayout linear_principal = new LinearLayout(this);
        linear_principal.setOrientation(LinearLayout.VERTICAL);
        linear_principal.setBackgroundColor(Color.WHITE);

        LinearLayout linear_fila_uno = new LinearLayout(this);
        linear_fila_uno.setOrientation(LinearLayout.HORIZONTAL);
        linear_fila_uno.setWeightSum(1.0f);

        LinearLayout linear_fila_dos = new LinearLayout(this);
        linear_fila_dos.setOrientation(LinearLayout.HORIZONTAL);
        linear_fila_dos.setWeightSum(1.0f);

        LinearLayout linear_fila_tres = new LinearLayout(this);
        linear_fila_tres.setOrientation(LinearLayout.HORIZONTAL);
        linear_fila_tres.setWeightSum(3.0f);

        LinearLayout linear_fila_cuatro = new LinearLayout(this);
        linear_fila_cuatro.setOrientation(LinearLayout.HORIZONTAL);
        linear_fila_cuatro.setWeightSum(3.0f);

        linear_principal.addView(linear_fila_uno);
        linear_principal.addView(linear_fila_dos);
        linear_principal.addView(linear_fila_tres);
        linear_principal.addView(linear_fila_cuatro);

        LinearLayout.LayoutParams parametros_pegado_elementos_fila_uno = new LinearLayout.LayoutParams(0,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        parametros_pegado_elementos_fila_uno.weight = 1.0f;
        linear_fila_uno.addView(espacio_1, parametros_pegado_elementos_fila_uno);

        LinearLayout.LayoutParams parametros_pegado_elementos_fila_dos = new LinearLayout.LayoutParams(0,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        parametros_pegado_elementos_fila_dos.weight = 1.0f;
        linear_fila_dos.addView(espacio_2, parametros_pegado_elementos_fila_dos);

        LinearLayout.LayoutParams parametros_pegado_elementos_fila_tres_izquierda = new LinearLayout.LayoutParams(0,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        parametros_pegado_elementos_fila_tres_izquierda.weight = 2.0f;
        linear_fila_tres.addView(text_periodo_muestreo, parametros_pegado_elementos_fila_tres_izquierda);

        LinearLayout.LayoutParams parametros_pegado_elementos_fila_tres_derecha = new LinearLayout.LayoutParams(0,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        parametros_pegado_elementos_fila_tres_derecha.weight = 1.0f;
        linear_fila_tres.addView(periodo_muestreo, parametros_pegado_elementos_fila_tres_derecha);

        LinearLayout.LayoutParams parametros_pegado_elementos_fila_cuatro_izquierda = new LinearLayout.LayoutParams(0,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        parametros_pegado_elementos_fila_cuatro_izquierda.weight = 2.0f;
        linear_fila_cuatro.addView(text_numero_datos, parametros_pegado_elementos_fila_cuatro_izquierda);

        LinearLayout.LayoutParams parametros_pegado_elementos_fila_cuatro_derecha = new LinearLayout.LayoutParams(0,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        parametros_pegado_elementos_fila_cuatro_derecha.weight = 1.0f;
        linear_fila_cuatro.addView(numero_datos, parametros_pegado_elementos_fila_cuatro_derecha);

        return linear_principal;
    }

    protected void onPause() {
        super.onPause();
        AlmacenDatosRAM.configurar = true;
        String valor_muestreo = periodo_muestreo.getText().toString();
        String valor_n = numero_datos.getText().toString();
        if (!valor_muestreo.isEmpty())
            AlmacenDatosRAM.periodoMuestreo = Integer.parseInt(valor_muestreo);
        if (!valor_n.isEmpty())
            AlmacenDatosRAM.nDatos = Integer.parseInt(valor_n);
    }
}
