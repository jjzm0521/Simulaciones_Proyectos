package com.curso_simulaciones.micuadrigesimaquintaapp.utilidades;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class Graficador extends LineChart {

    private LineDataSet lineDataSet;
    private String tituloEjeY = "";
    private int colorLinea = Color.RED;
    private float grosorLinea = 2f;
    private int colorValores = Color.WHITE;
    private int colorMarcadores = Color.RED;

    public Graficador(Context context) {
        super(context);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        // Configuraciones iniciales básicas para que se vea bien en fondo oscuro
        this.setBackgroundColor(Color.BLACK);
        this.getLegend().setTextColor(Color.WHITE);
        this.getDescription().setTextColor(Color.WHITE);
        this.getXAxis().setTextColor(Color.WHITE);
        this.getAxisLeft().setTextColor(Color.WHITE);
        this.getAxisRight().setEnabled(false); // Ocultar eje derecho por defecto
    }

    public void setDatos(ArrayList<Entry> datos) {
        if (datos == null) return;
        
        lineDataSet = new LineDataSet(datos, tituloEjeY);
        lineDataSet.setLineWidth(grosorLinea);
        lineDataSet.setColor(colorLinea);
        lineDataSet.setValueTextColor(colorValores);
        lineDataSet.setCircleColor(colorMarcadores);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setCircleRadius(3f);
        lineDataSet.setDrawValues(false); // No mostrar valores sobre los puntos para no saturar

        LineData lineData = new LineData(lineDataSet);
        this.setData(lineData);

        /*
        Esta instrucción es de la librería y es necesaria para
        que el eje se vaya desplazando a medida que entran datos
        */
        if (lineData.getEntryCount() > 0) {
            // Desplazar la vista para seguir el último dato
            this.moveViewToX(lineData.getEntryCount());
            // El usuario sugirió moveViewTo con parámetros específicos, 
            // pero moveViewToX es más común para scroll horizontal simple.
            // Implementamos la sugerencia del usuario adaptada:
            this.moveViewTo(lineData.getEntryCount() - 7, 50f, YAxis.AxisDependency.LEFT);
        }
        
        this.invalidate();
    }

    /*
    Modifica el grosor del trazo de la gráfica
    */
    public void setGrosorLinea(float grosorLinea) {
        this.grosorLinea = grosorLinea;
    }

    public void setColorLinea(int colorLinea) {
        this.colorLinea = colorLinea;
    }

    public void setColorFondo(int colorFondo) {
        this.setBackgroundColor(colorFondo);
    }

    public void setColorTextoEjes(int colorTextoEjes) {
        // texto eje y izquierda
        this.getAxisLeft().setTextColor(colorTextoEjes);
        // texto eje y derecha
        this.getAxisRight().setTextColor(colorTextoEjes);
        // texto eje x
        this.getXAxis().setTextColor(colorTextoEjes);
        // texto título eje y (leyenda)
        this.getLegend().setTextColor(colorTextoEjes);
        // texto título eje x (descripción)
        this.getDescription().setTextColor(colorTextoEjes);
    }

    public void setColorValores(int colorValores) {
        this.colorValores = colorValores;
    }

    public void setTituloEjeX(String tituloEjeX) {
        this.getDescription().setText(tituloEjeX);
        this.getDescription().setEnabled(true);
    }

    public void setTituloEjeY(String tituloEjeY) {
        this.tituloEjeY = tituloEjeY;
    }

    public void setColorMarcadores(int colorMarcadores) {
        this.colorMarcadores = colorMarcadores;
    }

    public void limpiarGrafica() {
        this.clear();
        this.invalidate();
    }
}
