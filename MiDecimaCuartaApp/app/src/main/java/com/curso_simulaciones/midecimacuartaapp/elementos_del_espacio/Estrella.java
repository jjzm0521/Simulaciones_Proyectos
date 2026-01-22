package com.curso_simulaciones.midecimacuartaapp.elementos_del_espacio;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Clase Estrella (renombrada de EstrellaFija)
 * Hereda de ObjetoEspacial
 * Implementa dibujese() para dibujar una estrella de 5 puntas
 */
public class Estrella extends ObjetoEspacial {

    private float posicionX;
    private float posicionY;
    private float radio;
    private int color;

    /**
     * Constructor
     * 
     * @param posicionX Posición X en píxeles
     * @param posicionY Posición Y en píxeles
     * @param radio     Radio de la estrella en píxeles
     * @param color     Color de la estrella
     */
    public Estrella(float posicionX, float posicionY, float radio, int color) {
        this.posicionX = posicionX;
        this.posicionY = posicionY;
        this.radio = radio;
        this.color = color;
    }

    /**
     * Dibuja la estrella de 5 puntas
     * 
     * @param canvas El canvas donde se dibujará
     * @param paint  El objeto Paint con los estilos de dibujo
     */
    @Override
    public void dibujese(Canvas canvas, Paint paint) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);

        // Dibujar estrella de 5 puntas
        Path path = new Path();

        // Calcular puntos de la estrella
        double angle = Math.PI / 2 - Math.PI / 5; // Comenzar desde arriba
        double angleIncrement = 2 * Math.PI / 5;

        // Radio interno (50% del radio externo)
        float radioInterno = radio * 0.4f;

        // Punto inicial (primera punta)
        float x = (float) (posicionX + radio * Math.cos(angle));
        float y = (float) (posicionY - radio * Math.sin(angle));
        path.moveTo(x, y);

        // Dibujar los 5 picos alternando entre radio externo e interno
        for (int i = 0; i < 5; i++) {
            // Punto interno
            angle += angleIncrement / 2;
            x = (float) (posicionX + radioInterno * Math.cos(angle));
            y = (float) (posicionY - radioInterno * Math.sin(angle));
            path.lineTo(x, y);

            // Punto externo (punta)
            angle += angleIncrement / 2;
            x = (float) (posicionX + radio * Math.cos(angle));
            y = (float) (posicionY - radio * Math.sin(angle));
            path.lineTo(x, y);
        }

        path.close();
        canvas.drawPath(path, paint);
    }
}
