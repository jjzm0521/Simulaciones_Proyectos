package com.curso_simulaciones.mitrigesimasextaapp.vista;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Magnetometro extends GaugeSimple implements SensorEventListener {

    private SensorManager sensorManager;
    private int componenteCampoMagnetico = 4;

    public Magnetometro(Context context) {
        super(context);
        // estado inicial
        setComponenteMagnetometro(componenteCampoMagnetico);

    }

    public void setComponenteMagnetometro(int componenteCampoMagnetico) {

        this.componenteCampoMagnetico = componenteCampoMagnetico;

        if (componenteCampoMagnetico == 1) {
            this.setUnidades("Bx (uT)");
            this.setRango(-100, 100);
        }

        if (componenteCampoMagnetico == 2) {
            this.setUnidades("By (uT)");
            this.setRango(-100, 100);
        }

        if (componenteCampoMagnetico == 3) {
            this.setUnidades("Bz (uT)");
            this.setRango(-100, 100);
        }

        if (componenteCampoMagnetico == 4) {
            this.setUnidades("B (uT)");
            this.setRango(0, 100);
        }

    }

    public void captarSensor(Context context) {

        // captamos el servicio del sensor
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_FASTEST);

    }

    // se activa sólo cuando hay cambios
    public void onSensorChanged(SensorEvent event) {

        // campo magnetico
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

        if (componenteCampoMagnetico == 1) {
            medida = medida_x;
            this.setUnidades(" Bx (uT)");
            this.setRango(-100, 100);
        }
        if (componenteCampoMagnetico == 2) {
            medida = medida_y;
            this.setUnidades(" By (uT)");
            this.setRango(-100, 100);
        }
        if (componenteCampoMagnetico == 3) {
            medida = medida_z;
            this.setUnidades(" Bz (uT)");
            this.setRango(-100, 100);
        }
        if (componenteCampoMagnetico == 4) {
            medida = B;
            this.setUnidades(" B (uT)");
            this.setRango(0, 100);
        }

        // un decimal
        medida = (float) (Math.round(medida * 10) / 10.0f);
        this.setMedida(medida);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
