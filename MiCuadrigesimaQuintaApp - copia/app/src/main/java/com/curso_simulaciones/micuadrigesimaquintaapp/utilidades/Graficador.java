package com.curso_simulaciones.micuadrigesimaquintaapp.utilidades;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class Graficador extends LineChart {

    private ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
    private LineData lineaData;

    public Graficador(Context context) {
        super(context);

        // Configuración básica del gráfico
        this.setDragEnabled(true);
        this.setScaleEnabled(true);
        this.setPinchZoom(true);
        this.setBackgroundColor(Color.BLACK);

        configurarEjes();
        inicializarDatos();
    }

    private void configurarEjes() {
        XAxis xAxis = this.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGridColor(Color.DKGRAY);

        YAxis leftAxis = this.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.DKGRAY);

        YAxis rightAxis = this.getAxisRight();
        rightAxis.setEnabled(false);

        this.getDescription().setEnabled(false);
        this.getLegend().setTextColor(Color.WHITE);
    }

    private void inicializarDatos() {
        iLineDataSets.clear();

        // Dataset 0: Distancia (Rojo) - Matches GaugeDistancia
        iLineDataSets.add(crearSet("Distancia (cm)", Color.RED));

        // Dataset 1: Temperatura (Verde) - Matches GaugeTemperatura
        iLineDataSets.add(crearSet("Temp (°C)", Color.GREEN));

        // Dataset 2: Humedad (Azul/Cyan) - Matches GaugeHumedad
        iLineDataSets.add(crearSet("Humedad (%)", Color.CYAN));

        lineaData = new LineData(iLineDataSets);
        this.setData(lineaData);
    }

    /**
     * Agrega datos a las 3 series simultáneamente
     */
    public void agregarDatos(float x, float distancia, float temp, float hum) {
        if (lineaData != null) {
            // Añadir entradas a cada dataset
            lineaData.addEntry(new Entry(x, distancia), 0);
            lineaData.addEntry(new Entry(x, temp), 1);
            lineaData.addEntry(new Entry(x, hum), 2);

            lineaData.notifyDataChanged();
            this.notifyDataSetChanged();
            this.setVisibleXRangeMaximum(50); // Mover ventana
            this.moveViewToX(lineaData.getEntryCount());
        }
    }

    // Método para compatibilidad si solo se pasa 1 dato (lo añade al primero)
    public void agregarDato(float x, float y) {
        if (lineaData != null) {
            lineaData.addEntry(new Entry(x, y), 0);
            lineaData.notifyDataChanged();
            this.notifyDataSetChanged();
            this.setVisibleXRangeMaximum(50);
            this.moveViewToX(lineaData.getEntryCount());
        }
    }

    // Método setDatos legacy para compatibilidad con arrays completos
    public void setDatos(ArrayList<Entry> entries) {
        // Este método reemplaza todo.
        // Para mantener compatibilidad con el codigo que usa "setDatos" para graficar
        // solo distancia:
        if (lineaData != null && lineaData.getDataSetCount() > 0) {
            ILineDataSet set = lineaData.getDataSetByIndex(0);
            set.clear();
            for (Entry e : entries) {
                set.addEntry(e);
            }
            lineaData.notifyDataChanged();
            this.notifyDataSetChanged();
            this.invalidate();
        }
    }

    private LineDataSet crearSet(String etiqueta, int color) {
        LineDataSet set = new LineDataSet(null, etiqueta);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(color);
        set.setLineWidth(2f);
        set.setDrawCircles(false);
        set.setDrawValues(false);
        return set;
    }

    public void setTituloEjeX(String titulo) {
        this.getDescription().setText(titulo);
        this.getDescription().setEnabled(true);
        this.getDescription().setTextColor(Color.WHITE);
    }

    public void setTituloEjeY(String titulo) {
        // Opcional
    }

    public void setGrosorLinea(float grosor) {
        // Aplica al primer dataset por defecto
        if (lineaData != null && lineaData.getDataSetCount() > 0)
            ((LineDataSet) lineaData.getDataSetByIndex(0)).setLineWidth(grosor);
    }

    public void setColorLinea(int color) {
        if (lineaData != null && lineaData.getDataSetCount() > 0)
            ((LineDataSet) lineaData.getDataSetByIndex(0)).setColor(color);
    }

    public void setColorValores(int color) {
        if (lineaData != null && lineaData.getDataSetCount() > 0)
            ((LineDataSet) lineaData.getDataSetByIndex(0)).setValueTextColor(color);
    }

    public void setColorMarcadores(int color) {
        if (lineaData != null && lineaData.getDataSetCount() > 0) {
            ((LineDataSet) lineaData.getDataSetByIndex(0)).setCircleColor(color);
            ((LineDataSet) lineaData.getDataSetByIndex(0)).setDrawCircles(true);
        }
    }

    public void setColorFondo(int color) {
        this.setBackgroundColor(color);
    }

    public void setColorTextoEjes(int color) {
        this.getXAxis().setTextColor(color);
        this.getAxisLeft().setTextColor(color);
        this.getLegend().setTextColor(color);
        this.getDescription().setTextColor(color);
    }

    public void limpiarGrafica() {
        if (lineaData != null) {
            lineaData.clearValues();
            this.notifyDataSetChanged();
            this.invalidate();
            this.fitScreen();
        }
    }
}
