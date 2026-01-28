package com.curso_simulaciones.midecimanovenaapp.actividades_secundarias;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.curso_simulaciones.midecimanovenaapp.datos.AlmacenDatosRAM;

public class ActividadSecundaria_1 extends Activity {

    private EditText campoTexto;
    private TextView etiquetaNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        crearElementsGUI();

        ViewGroup.LayoutParams parametro_layout_principal = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        this.setContentView(crearGUI(), parametro_layout_principal);
    }

    /* método responsable de la creación de los elementos de la GUI */
    private void crearElementsGUI() {
        etiquetaNombre = new TextView(this);
        etiquetaNombre.setText("NOMBRE");
        etiquetaNombre.setTextSize(TypedValue.COMPLEX_UNIT_SP, AlmacenDatosRAM.tamanoLetraResolucionIncluida);
        etiquetaNombre.setBackgroundColor(Color.rgb(255, 140, 0)); // Naranja
        etiquetaNombre.setTextColor(Color.BLACK);
        etiquetaNombre.setGravity(Gravity.CENTER);

        campoTexto = new EditText(this);
        campoTexto.setTextSize(TypedValue.COMPLEX_UNIT_SP, AlmacenDatosRAM.tamanoLetraResolucionIncluida);
        campoTexto.setHint("Xxxx");
        campoTexto.setBackgroundColor(Color.WHITE);
        campoTexto.setTextColor(Color.BLACK);
    }

    /* método responsable de administrar el diseño de la GUI */
    private LinearLayout crearGUI() {
        // Contenedor principal externo (blanco)
        LinearLayout linearPrincipal = new LinearLayout(this);
        linearPrincipal.setOrientation(LinearLayout.VERTICAL);
        linearPrincipal.setBackgroundColor(Color.WHITE);
        linearPrincipal.setPadding(20, 20, 20, 20);

        // --- EL RECUADRO VERDE (que rodea tanto imagen como controles) ---
        LinearLayout linearBordeVerde = new LinearLayout(this);
        linearBordeVerde.setOrientation(LinearLayout.VERTICAL);
        linearBordeVerde.setBackgroundColor(Color.rgb(0, 255, 0)); // Verde brillante
        linearBordeVerde.setPadding(10, 10, 10, 10); // Grosor del borde verde
        linearBordeVerde.setWeightSum(10.0f);

        // --- ÁREA SUPERIOR: IMAGEN (90%) ---
        LinearLayout linearImagenArea = new LinearLayout(this);
        linearImagenArea.setBackgroundColor(Color.WHITE);
        linearImagenArea.setGravity(Gravity.CENTER);

        android.widget.ImageView imgImagen = new android.widget.ImageView(this);
        imgImagen.setImageResource(com.curso_simulaciones.midecimanovenaapp.R.drawable.imagen_uno);
        imgImagen.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
        linearImagenArea.addView(imgImagen);

        LinearLayout.LayoutParams paramsImagen = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        paramsImagen.weight = 9.0f;
        paramsImagen.setMargins(0, 0, 0, 10); // Pequeña separación del área amarilla

        // --- ÁREA INFERIOR: CONTROLES (10%) ---
        // El recuadro amarillo
        LinearLayout linearControlesAmarillo = new LinearLayout(this);
        linearControlesAmarillo.setOrientation(LinearLayout.HORIZONTAL);
        linearControlesAmarillo.setBackgroundColor(Color.YELLOW);
        linearControlesAmarillo.setWeightSum(2.0f); // 50/50 split
        linearControlesAmarillo.setPadding(15, 15, 15, 15); // Inset para que los elementos sean más pequeños que el
                                                            // recuadro

        LinearLayout.LayoutParams paramsNOMBRE = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT);
        paramsNOMBRE.weight = 1.0f; // 50%
        paramsNOMBRE.setMargins(10, 0, 10, 0);

        LinearLayout.LayoutParams paramsEditText = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT);
        paramsEditText.weight = 1.0f; // 50%
        paramsEditText.setMargins(10, 0, 10, 0);

        linearControlesAmarillo.addView(etiquetaNombre, paramsNOMBRE);
        linearControlesAmarillo.addView(campoTexto, paramsEditText);

        LinearLayout.LayoutParams paramsControles = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        paramsControles.weight = 1.0f;

        // Ensamblar todo
        linearBordeVerde.addView(linearImagenArea, paramsImagen);
        linearBordeVerde.addView(linearControlesAmarillo, paramsControles);

        linearPrincipal.addView(linearBordeVerde, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return linearPrincipal;
    }

    @Override
    public void onBackPressed() {
        AlmacenDatosRAM.nombreImagenActividad1 = campoTexto.getText().toString();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        AlmacenDatosRAM.nombreImagenActividad1 = campoTexto.getText().toString();
        this.finish();
        super.onDestroy();
    }
}
