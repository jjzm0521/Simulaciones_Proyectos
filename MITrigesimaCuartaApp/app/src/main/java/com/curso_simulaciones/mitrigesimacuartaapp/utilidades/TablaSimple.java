package com.curso_simulaciones.mitrigesimacuartaapp.utilidades;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.curso_simulaciones.mitrigesimacuartaapp.datos.AlmacenDatosRAM;

/**
 * Tabla simple para mostrar datos del acelerómetro
 * con 6 columnas: # de DATO, Tiempo, ax, ay, az, a
 */
public class TablaSimple extends LinearLayout {

    private ScrollView panelScroll;
    private TableLayout table;
    private int tamanoLetraResolucionIncluida;
    private int dimensionReferencia;
    private Context context;
    private int contador = -1;

    private int colorColumna1 = Color.YELLOW;
    private int colorColumna2 = Color.CYAN;
    private int colorColumna3 = Color.GREEN;
    private int colorColumna4 = Color.MAGENTA;
    private int colorColumna5 = Color.rgb(255, 165, 0); // Orange
    private int colorColumna6 = Color.RED;

    /**
     * Constructor de TablaSimple
     * 
     * @param context
     */
    public TablaSimple(Context context) {
        super(context);
        this.context = context;

        gestionarResolucion();

        this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        gui();

    }

    private void gestionarResolucion() {

        /*
         * El alto en la actividad principal (PORTRAIT)
         * corresponde al ancho aquí (LANDSCAPE)
         */
        dimensionReferencia = (int) (0.4f * AlmacenDatosRAM.alto);

        tamanoLetraResolucionIncluida = (int) (0.5 * AlmacenDatosRAM.tamanoLetraResolucionIncluida);

    }// fin método gestionarResolucion()

    private void gui() {

        panelScroll = new ScrollView(context);
        table = new TableLayout(context);

        LinearLayout linearLayoutPrincipal = new LinearLayout(context);
        linearLayoutPrincipal.setOrientation(LinearLayout.VERTICAL);
        linearLayoutPrincipal.setBackgroundColor(Color.BLACK);

        LinearLayout.LayoutParams parametroPegado = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        panelScroll.addView(table);
        linearLayoutPrincipal.addView(panelScroll);

        this.addView(linearLayoutPrincipal, parametroPegado);

    }

    /**
     * Envía los datos a la tabla (6 columnas para acelerómetro)
     * 
     * @param tiempo
     * @param ax
     * @param ay
     * @param az
     * @param a
     */
    public void enviarDatos(float tiempo, float ax, float ay, float az, float a) {

        contador = contador + 1;
        incrementarFila(tiempo, ax, ay, az, a);

    }

    /**
     * Borra los datos enviados a la tabla
     */
    public void borrar() {
        removerFilas();
    }

    /**
     * Modifica los colores de las columnas
     * 
     * @param c1
     * @param c2
     * @param c3
     * @param c4
     * @param c5
     * @param c6
     */
    public void setColorColumnas(int c1, int c2, int c3, int c4, int c5, int c6) {

        this.colorColumna1 = c1;
        this.colorColumna2 = c2;
        this.colorColumna3 = c3;
        this.colorColumna4 = c4;
        this.colorColumna5 = c5;
        this.colorColumna6 = c6;

    }

    private void incrementarFila(float tiempo, float ax, float ay, float az, float a) {

        // crear nueva TableRow
        TableRow fila = new TableRow(context);

        int anchoColumna = (int) (0.16 * dimensionReferencia);
        TableRow.LayoutParams layoutTexto = new TableRow.LayoutParams(anchoColumna, TableRow.LayoutParams.WRAP_CONTENT);

        // columna 1: # de Dato
        TextView textNumeroDato = new TextView(context);
        textNumeroDato.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        textNumeroDato.setLayoutParams(layoutTexto);

        if (contador == 0) {

            textNumeroDato.setText("# DATO");

        } else {

            textNumeroDato.setText("" + contador);
        }

        textNumeroDato.setTextSize(tamanoLetraResolucionIncluida);
        textNumeroDato.setTextColor(colorColumna1);
        textNumeroDato.setGravity(Gravity.CENTER_HORIZONTAL);

        // columna 2: Tiempo
        TextView textTiempo = new TextView(context);
        textTiempo.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        textTiempo.setLayoutParams(layoutTexto);

        if (contador == 0) {
            textTiempo.setText("t(s)");

        } else {

            textTiempo.setText("" + tiempo);

        }

        textTiempo.setTextSize(tamanoLetraResolucionIncluida);
        textTiempo.setTextColor(colorColumna2);
        textTiempo.setGravity(Gravity.CENTER_HORIZONTAL);

        // columna 3: ax
        TextView textAx = new TextView(context);
        textAx.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        textAx.setLayoutParams(layoutTexto);

        if (contador == 0) {
            textAx.setText("ax");

        } else {

            textAx.setText("" + ax);

        }

        textAx.setTextSize(tamanoLetraResolucionIncluida);
        textAx.setTextColor(colorColumna3);
        textAx.setGravity(Gravity.CENTER_HORIZONTAL);

        // columna 4: ay
        TextView textAy = new TextView(context);
        textAy.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        textAy.setLayoutParams(layoutTexto);

        if (contador == 0) {
            textAy.setText("ay");

        } else {

            textAy.setText("" + ay);

        }

        textAy.setTextSize(tamanoLetraResolucionIncluida);
        textAy.setTextColor(colorColumna4);
        textAy.setGravity(Gravity.CENTER_HORIZONTAL);

        // columna 5: az
        TextView textAz = new TextView(context);
        textAz.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        textAz.setLayoutParams(layoutTexto);

        if (contador == 0) {
            textAz.setText("az");

        } else {

            textAz.setText("" + az);

        }

        textAz.setTextSize(tamanoLetraResolucionIncluida);
        textAz.setTextColor(colorColumna5);
        textAz.setGravity(Gravity.CENTER_HORIZONTAL);

        // columna 6: a (magnitud)
        TextView textA = new TextView(context);
        textA.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        textA.setLayoutParams(layoutTexto);

        if (contador == 0) {
            textA.setText("a");

        } else {

            textA.setText("" + a);

        }

        textA.setTextSize(tamanoLetraResolucionIncluida);
        textA.setTextColor(colorColumna6);
        textA.setGravity(Gravity.CENTER_HORIZONTAL);

        fila.setGravity(Gravity.CENTER_HORIZONTAL);
        // adicionar las seis columnas a la fila
        fila.addView(textNumeroDato);
        fila.addView(textTiempo);
        fila.addView(textAx);
        fila.addView(textAy);
        fila.addView(textAz);
        fila.addView(textA);

        // Adicionar TableRow a la Tabla
        table.addView(fila, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

    }

    private void removerFilas() {

        if (contador > 1) {

            table.removeAllViews();
            contador = -1;

        }

    }
}
