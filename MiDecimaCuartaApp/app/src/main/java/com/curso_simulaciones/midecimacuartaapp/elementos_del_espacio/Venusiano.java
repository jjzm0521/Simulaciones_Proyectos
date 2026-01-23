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

        // Activar anti-aliasing
        pincel.setAntiAlias(true);

        // magnificar
        canvas.scale(magnificacion, magnificacion, posicionCentroideX, posicionCentroideY);
        // rotar
        canvas.rotate(posicionAngularRotacionEjeXY, posicionEjeRotacionX, posicionEjeRotacionY);

        float radioCuerpo = 50f;

        // 1. Dibujar cuerpo circular principal (Cara)
        pincel.setStyle(Paint.Style.FILL);
        pincel.setColor(Color.BLUE);
        canvas.drawCircle(posicionCentroideX, posicionCentroideY, radioCuerpo, pincel);

        // Borde negro fino para la cara
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeWidth(2f);
        pincel.setColor(Color.BLACK);
        canvas.drawCircle(posicionCentroideX, posicionCentroideY, radioCuerpo, pincel);

        // 2. Dibujar el gorro/óvalo en la parte superior (Más grande y sin cuernos)
        pincel.setStyle(Paint.Style.FILL);
        pincel.setColor(Color.BLUE);

        // Aumentamos el tamaño del gorro
        float anchoGorro = radioCuerpo * 1.8f; // Antes 1.4f
        float altoGorro = 35f; // Antes 25f
        float yCentroGorro = posicionCentroideY - radioCuerpo + 5f;

        // El óvalo
        canvas.drawOval(posicionCentroideX - anchoGorro / 2,
                yCentroGorro - altoGorro / 2,
                posicionCentroideX + anchoGorro / 2,
                yCentroGorro + altoGorro / 2,
                pincel);

        // (Se eliminaron las antenas/cuernitos)

        // 4. Ojos (Círculos Blancos grandes separados)
        pincel.setColor(Color.WHITE);
        float radioOjo = 18f;
        float separacionOjos = 22f;

        float xOjoIzq = posicionCentroideX - separacionOjos;
        float yOjo = posicionCentroideY - 5f;
        float xOjoDer = posicionCentroideX + separacionOjos;

        canvas.drawCircle(xOjoIzq, yOjo, radioOjo, pincel);
        canvas.drawCircle(xOjoDer, yOjo, radioOjo, pincel);

        // 5. Pupilas (Rombos)
        pincel.setColor(Color.BLUE);
        float radioPupila = 6f;

        android.graphics.Path pathPupilaIzq = new android.graphics.Path();
        pathPupilaIzq.moveTo(xOjoIzq, yOjo - radioPupila);
        pathPupilaIzq.lineTo(xOjoIzq + radioPupila, yOjo);
        pathPupilaIzq.lineTo(xOjoIzq, yOjo + radioPupila);
        pathPupilaIzq.lineTo(xOjoIzq - radioPupila, yOjo);
        pathPupilaIzq.close();
        canvas.drawPath(pathPupilaIzq, pincel);

        android.graphics.Path pathPupilaDer = new android.graphics.Path();
        pathPupilaDer.moveTo(xOjoDer, yOjo - radioPupila);
        pathPupilaDer.lineTo(xOjoDer + radioPupila, yOjo);
        pathPupilaDer.lineTo(xOjoDer, yOjo + radioPupila);
        pathPupilaDer.lineTo(xOjoDer - radioPupila, yOjo);
        pathPupilaDer.close();
        canvas.drawPath(pathPupilaDer, pincel);

        // 6. Nariz
        pincel.setColor(Color.BLACK);
        canvas.drawCircle(posicionCentroideX, posicionCentroideY + 10f, 2f, pincel);

        // 7. Boca (Recta/Rectángulo redondeado fino y ancho)
        pincel.setColor(Color.WHITE);
        float anchoBoca = 30f;
        float altoBoca = 4f;
        float yBoca = posicionCentroideY + 30f;

        canvas.drawRoundRect(posicionCentroideX - anchoBoca / 2,
                yBoca - altoBoca / 2,
                posicionCentroideX + anchoBoca / 2,
                yBoca + altoBoca / 2,
                10f, 10f, pincel);

        canvas.restore();
    }
}
