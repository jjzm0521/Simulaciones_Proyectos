package com.curso_simulaciones.micuadragesimasegundaapp.utilidades;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Magnetometro extends GaugeSimple implements SensorEventListener {

    private SensorManager sensorManager;
    private int componenteMagnetica = 4;

    public Magnetometro(Context context) {
        super(context);
        // estado inicial
        setComponenteMagnetica(componenteMagnetica);

    }

    public void setComponenteMagnetica(int componenteMagnetica) {

        this.componenteMagnetica = componenteMagnetica;

        // Rango aproximado 0-100 uT (campo magnético terrestre ~25-65 uT)
        // Ajustar rangos según necesidad
        if (componenteMagnetica == 1) {
            this.setUnidades("Bx (uT)");
            this.setRango(-100, 100);
        }

        if (componenteMagnetica == 2) {
            this.setUnidades("By (uT)");
            this.setRango(-100, 100);
        }

        if (componenteMagnetica == 3) {
            this.setUnidades("Bz (uT)");
            this.setRango(-100, 100);
        }

        if (componenteMagnetica == 4) {
            this.setUnidades("B (uT)");
            this.setRango(0, 150);
        }

    }

    public void captarSensor(Context context) {

        // captamos el servicio del sensor
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        }

    }

    // se activa sólo cuando hay cambios
    public void onSensorChanged(SensorEvent event) {

        if (event == null || event.values == null || event.values.length < 3) {
            return;
        }

        // en x
        float b = 0;
        float medida_x = 0;
        float medida_y = 0;
        float medida_z = 0;
        float medida = 0;

        medida_x = event.values[SensorManager.DATA_X];
        medida_y = event.values[SensorManager.DATA_Y];
        medida_z = event.values[SensorManager.DATA_Z];
        float resultado = medida_x * medida_x + medida_y * medida_y + medida_z * medida_z;
        b = (float) (Math.sqrt(resultado));

        if (componenteMagnetica == 1) {
            medida = medida_x;
            this.setUnidades(" Bx (uT)");
            this.setRango(-100, 100);
        }
        if (componenteMagnetica == 2) {
            medida = medida_y;
            this.setUnidades(" By (uT)");
            this.setRango(-100, 100);
        }
        if (componenteMagnetica == 3) {
            medida = medida_z;
            this.setUnidades(" Bz (uT)");
            this.setRango(-100, 100);
        }
        if (componenteMagnetica == 4) {
            medida = b;
            this.setUnidades(" B (uT)");
            this.setRango(0, 150);
        }

        // un decimal
        medida = (float) (Math.round(medida * 10) / 10.0f);
        this.setMedida(medida);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
