package com.curso_simulaciones.midecimanovenaapp.actividades_secundarias;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.curso_simulaciones.midecimanovenaapp.datos.AlmacenDatosRAM;

public class ActividadSecundaria_3 extends Activity {

    private TextView avisoResultados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        crearElementosGUI();

        ViewGroup.LayoutParams parametro_layout_principal = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        this.setContentView(crearGUI(), parametro_layout_principal);
    }

    /* método responsable de la creación de los elementos de la GUI */
    private void crearElementosGUI() {
        avisoResultados = new TextView(this);
        avisoResultados.setTextSize(TypedValue.COMPLEX_UNIT_SP, AlmacenDatosRAM.tamanoLetraResolucionIncluida);
        avisoResultados.setTextColor(Color.BLACK);

        // Formato exacto de la Figura 28
        String mensaje = "La imagen de la AplicacionSecundaria_1 se denomina " +
                AlmacenDatosRAM.nombreImagenActividad1 +
                " y la de la AplicacionSecundaria_2 se denomina " +
                AlmacenDatosRAM.nombreImagenActividad2;

        avisoResultados.setText(mensaje);
    }

    /* método responsable de administrar el diseño de la GUI */
    private LinearLayout crearGUI() {
        // Contenedor principal
        LinearLayout linearPrincipal = new LinearLayout(this);
        linearPrincipal.setOrientation(LinearLayout.VERTICAL);
        linearPrincipal.setBackgroundColor(Color.WHITE);
        linearPrincipal.setPadding(40, 40, 40, 40); // Margen para que se vea como en el dibujo

        // Pegar el aviso
        linearPrincipal.addView(avisoResultados);

        return linearPrincipal;
    }

    @Override
    protected void onDestroy() {
        this.finish();
        super.onDestroy();
    }
}
