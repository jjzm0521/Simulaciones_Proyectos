package com.curso_simulaciones.mivigesimanovenaapp.utilidades;

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

    private LineDataSet datosSet;
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
        datosSet = new LineDataSet(null, "Datos Sensor");
        datosSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        datosSet.setColor(Color.GREEN);
        datosSet.setLineWidth(2f);
        datosSet.setDrawCircles(false);
        datosSet.setDrawValues(false);

        iLineDataSets.add(datosSet);
        lineaData = new LineData(iLineDataSets);
        this.setData(lineaData);
    }

    public void agregarDato(float x, float y) {
        if (lineaData != null) {
            ILineDataSet set = lineaData.getDataSetByIndex(0);
            if (set == null) {
                set = crearSet();
                lineaData.addDataSet(set);
            }

            lineaData.addEntry(new Entry(x, y), 0);
            lineaData.notifyDataChanged();
            this.notifyDataSetChanged();
            this.setVisibleXRangeMaximum(100);
            this.moveViewToX(lineaData.getEntryCount());
        }
    }

    private LineDataSet crearSet() {
        LineDataSet set = new LineDataSet(null, "Datos Sensor");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.GREEN);
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
    }

    public void setGrosorLinea(float grosor) {
        if (datosSet != null)
            datosSet.setLineWidth(grosor);
    }

    public void setColorLinea(int color) {
        if (datosSet != null)
            datosSet.setColor(color);
    }

    public void setColorValores(int color) {
        if (datosSet != null)
            datosSet.setValueTextColor(color);
    }

    public void setColorMarcadores(int color) {
        if (datosSet != null) {
            datosSet.setCircleColor(color);
            datosSet.setDrawCircles(true);
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
