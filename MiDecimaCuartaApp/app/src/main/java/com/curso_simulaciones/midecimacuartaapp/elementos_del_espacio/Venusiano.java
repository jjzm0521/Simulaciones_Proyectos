package com.curso_simulaciones.midecimacuartaapp.elementos_del_espacio;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Clase Venusiano (habitante de Venus)
 * Hereda de Extraterrestre
 * Implementa dibujese() para dibujar la caricatura del venusiano
 */
public class Venusiano extends Extraterrestre {

    /**
     * Constructor por defecto
     */
    public Venusiano() {
        super();
    }

    /**
     * Constructor con posición del centroide
     */
    public Venusiano(float posicionCentroideX, float posicionCentroideY) {
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

        // Dibujar cabeza triangular del venusiano
        pincel.setStyle(Paint.Style.FILL);
        pincel.setColor(color);

        // Triángulo para la cabeza
        float[] puntosTriangulo = {
                posicionCentroideX, posicionCentroideY - radio, // Punta superior
                posicionCentroideX - 0.8f * radio, posicionCentroideY + 0.5f * radio, // Esquina izquierda
                posicionCentroideX + 0.8f * radio, posicionCentroideY + 0.5f * radio // Esquina derecha
        };

        android.graphics.Path path = new android.graphics.Path();
        path.moveTo(puntosTriangulo[0], puntosTriangulo[1]);
        path.lineTo(puntosTriangulo[2], puntosTriangulo[3]);
        path.lineTo(puntosTriangulo[4], puntosTriangulo[5]);
        path.close();
        canvas.drawPath(path, pincel);

        // Contorno del triángulo
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeWidth(3f);
        pincel.setColor(Color.BLACK);
        canvas.drawPath(path, pincel);

        // Dibujar tres ojos en línea horizontal
        pincel.setStyle(Paint.Style.FILL);
        pincel.setColor(Color.YELLOW);
        float yOjos = posicionCentroideY - 0.2f * radio;
        float radioOjo = 0.15f * radio;

        // Ojo izquierdo
        canvas.drawCircle(posicionCentroideX - 0.4f * radio, yOjos, radioOjo, pincel);
        // Ojo central
        canvas.drawCircle(posicionCentroideX, yOjos, radioOjo, pincel);
        // Ojo derecho
        canvas.drawCircle(posicionCentroideX + 0.4f * radio, yOjos, radioOjo, pincel);

        // Pupilas
        pincel.setColor(Color.BLACK);
        float radioPupila = 0.07f * radio;
        canvas.drawCircle(posicionCentroideX - 0.4f * radio, yOjos, radioPupila, pincel);
        canvas.drawCircle(posicionCentroideX, yOjos, radioPupila, pincel);
        canvas.drawCircle(posicionCentroideX + 0.4f * radio, yOjos, radioPupila, pincel);

        // Boca sonriente
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeWidth(4f);
        pincel.setColor(Color.BLACK);
        canvas.drawArc(posicionCentroideX - 0.3f * radio, posicionCentroideY + 0.1f * radio,
                posicionCentroideX + 0.3f * radio, posicionCentroideY + 0.5f * radio,
                0, 180, false, pincel);

        canvas.restore();
    }
}
