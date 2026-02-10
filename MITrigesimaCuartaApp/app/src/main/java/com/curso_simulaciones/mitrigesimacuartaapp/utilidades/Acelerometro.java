package com.curso_simulaciones.mitrigesimacuartaapp.utilidades;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.curso_simulaciones.mitrigesimacuartaapp.datos.AlmacenDatosRAM;

/**
 * Clase que extiende GaugeSimple para capturar datos del acelerómetro.
 * Captura ax, ay, az y calcula a = sqrt(ax² + ay² + az²)
 */
public class Acelerometro extends GaugeSimple implements SensorEventListener {

    private SensorManager sensorManager;

    public Acelerometro(Context context) {
        super(context);

        captarSensor(context);

    }

    private void captarSensor(Context context) {

        // captamos el servicio del sensor
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);

    }

    // se activa sólo cuando hay cambios
    public void onSensorChanged(SensorEvent event) {

        // Obtener valores del acelerómetro
        float ax = event.values[0]; // Aceleración en X
        float ay = event.values[1]; // Aceleración en Y
        float az = event.values[2]; // Aceleración en Z

        // Calcular magnitud total: a = sqrt(ax² + ay² + az²)
        float a = (float) Math.sqrt(ax * ax + ay * ay + az * az);

        // solo con dos decimales
        ax = (float) (Math.round(ax * 100) / 100f);
        ay = (float) (Math.round(ay * 100) / 100f);
        az = (float) (Math.round(az * 100) / 100f);
        a = (float) (Math.round(a * 100) / 100f);

        // Mostrar magnitud total en el gauge
        this.setMedida(a);
        cambiarEscala(a);

        // almacenar datos actuales
        AlmacenDatosRAM.ax = ax;
        AlmacenDatosRAM.ay = ay;
        AlmacenDatosRAM.az = az;
        AlmacenDatosRAM.a = a;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void cambiarEscala(float medida) {

        // Escala ajustada para mejor visualización del acelerómetro
        // El rango inicial es 0-20 para ver mejor las variaciones
        float maximo = 20f;
        float minimo = 0f;

        if (medida >= 0 && medida <= 20) {

            maximo = 20f;
            minimo = 0f;

        }

        if (medida > 20 && medida <= 40) {

            maximo = 40f;
            minimo = 0f;

        }

        if (medida > 40 && medida <= 60) {

            maximo = 60f;
            minimo = 0f;

        }

        if (medida > 60 && medida <= 100) {

            maximo = 100f;
            minimo = 0f;

        }

        this.setRango(minimo, maximo);

    }

}
