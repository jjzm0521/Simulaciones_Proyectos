package com.curso_simulaciones.mivigesimanovenaapp.vista;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.curso_simulaciones.mivigesimanovenaapp.datos.AlmacenDatosRAM;

public class Gaussimetro extends GaugeSimple implements SensorEventListener {

    private SensorManager sensorManager;
    private int componenteCampo = 1;

    public Gaussimetro(Context context) {
        super(context);

        captarSensor(context);

        this.setRango(-100, 100); // Standard range for microtesla in typical environments

    }

    public void setComponenteCampo(int componenteCampo) {

        this.componenteCampo = componenteCampo;

    }

    private void captarSensor(Context context) {

        // captamos el servicio del sensor
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_FASTEST);

    }

    // se activa sólo cuando hay cambios
    public void onSensorChanged(SensorEvent event) {

        // en x
        float B = 0;
        float medida_x = 0;
        float medida_y = 0;
        float medida_z = 0;
        float medida = 0;

        medida_x = event.values[SensorManager.DATA_X];
        medida_y = event.values[SensorManager.DATA_Y];
        medida_z = event.values[SensorManager.DATA_Z];
        float resultado = medida_x * medida_x + medida_y * medida_y + medida_z * medida_z;
        B = (float) (Math.sqrt(resultado));

        if (componenteCampo == 1) {
            medida = medida_x;
            this.setUnidades(" Bx (µT)");
        }
        if (componenteCampo == 2) {
            medida = medida_y;
            this.setUnidades(" By (µT)");
        }
        if (componenteCampo == 3) {
            medida = medida_z;
            this.setUnidades(" Bz (µT)");
        }
        if (componenteCampo == 4) {
            medida = B;
            this.setUnidades(" B (µT)");
        }

        // un decimal
        medida = (float) (Math.round(medida * 10) / 10.0f);
        this.setMedida(medida);
        // almacenar dato actual
        AlmacenDatosRAM.datoActual = medida;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
