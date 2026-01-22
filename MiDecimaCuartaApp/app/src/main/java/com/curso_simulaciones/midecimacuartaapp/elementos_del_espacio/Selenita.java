package com.curso_simulaciones.midecimacuartaapp.elementos_del_espacio;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Clase Selenita (habitante de la Luna)
 * Hereda de Extraterrestre
 * Implementa dibujese() para dibujar la caricatura del selenita
 */
public class Selenita extends Extraterrestre {

    /**
     * Constructor por defecto
     */
    public Selenita() {
        super();
    }

    /**
     * Constructor con posición del centroide
     */
    public Selenita(float posicionCentroideX, float posicionCentroideY) {
        super(posicionCentroideX, posicionCentroideY);
    }

    @Override
    public void dibujese(Canvas canvas, Paint pincel) {
        canvas.save();

        // magnificar
        canvas.scale(magnificacion, magnificacion, posicionCentroideX, posicionCentroideY);
        // rotar
        canvas.rotate(posicionAngularRotacionEjeXY, posicionEjeRotacionX, posicionEjeRotacionY);

        float radio = 50f;

        // Dibujar cuerpo del selenita (forma ovalada)
        pincel.setStyle(Paint.Style.FILL);
        pincel.setColor(Color.LTGRAY);
        canvas.drawOval(posicionCentroideX - radio * 0.7f, posicionCentroideY - radio,
                posicionCentroideX + radio * 0.7f, posicionCentroideY + radio, pincel);

        // Contorno
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeWidth(3f);
        pincel.setColor(Color.BLACK);
        canvas.drawOval(posicionCentroideX - radio * 0.7f, posicionCentroideY - radio,
                posicionCentroideX + radio * 0.7f, posicionCentroideY + radio, pincel);

        // Dibujar ojos grandes típicos del selenita
        pincel.setStyle(Paint.Style.FILL);
        pincel.setColor(Color.BLACK);
        float separacionOjos = 0.25f * radio;
        // Ojo izquierdo
        canvas.drawOval(posicionCentroideX - separacionOjos - 0.3f * radio, posicionCentroideY - 0.4f * radio,
                posicionCentroideX - separacionOjos + 0.1f * radio, posicionCentroideY + 0.2f * radio, pincel);
        // Ojo derecho
        canvas.drawOval(posicionCentroideX + separacionOjos - 0.1f * radio, posicionCentroideY - 0.4f * radio,
                posicionCentroideX + separacionOjos + 0.3f * radio, posicionCentroideY + 0.2f * radio, pincel);

        // Antenas
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeWidth(5f);
        pincel.setColor(color);
        // Antena izquierda
        canvas.drawLine(posicionCentroideX - 0.3f * radio, posicionCentroideY - radio,
                posicionCentroideX - 0.5f * radio, posicionCentroideY - 1.5f * radio, pincel);
        // Antena derecha
        canvas.drawLine(posicionCentroideX + 0.3f * radio, posicionCentroideY - radio,
                posicionCentroideX + 0.5f * radio, posicionCentroideY - 1.5f * radio, pincel);

        // Bolitas en las antenas
        pincel.setStyle(Paint.Style.FILL);
        canvas.drawCircle(posicionCentroideX - 0.5f * radio, posicionCentroideY - 1.5f * radio, 8f, pincel);
        canvas.drawCircle(posicionCentroideX + 0.5f * radio, posicionCentroideY - 1.5f * radio, 8f, pincel);

        canvas.restore();
    }
}
