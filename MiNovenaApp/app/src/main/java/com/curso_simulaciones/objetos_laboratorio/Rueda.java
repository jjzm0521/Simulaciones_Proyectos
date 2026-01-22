package com.curso_simulaciones.objetos_laboratorio;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Clase Rueda
 * Representa una rueda que puede dibujarse, rotarse y trasladarse
 */
public class Rueda {

    private float radio;
    private float posicionInicialX, posicionInicialY;
    private float posicionX, posicionY, posicionAngular;
    private int color = Color.rgb(255, 0, 0);// rojo

    /**
     * Constructor por defecto
     * Rueda centrada en (0,0),
     * con posición angular 0,
     * de color rojo
     * y de radio 20f
     */
    public Rueda() {
        this.radio = 20f;
    }

    /**
     * Constructor de Rueda centrada
     * en (posicionX,posicionY),
     * con posición angular cero
     * de color rojo
     * y diámetro igual a 2*radio
     */

    public Rueda(float posicionInicialX, float posicionInicialY, float radio) {
        this.posicionX = posicionInicialX;
        this.posicionY = posicionInicialY;

        this.posicionInicialX = posicionInicialX;
        this.posicionInicialY = posicionInicialY;

        this.radio = radio;
    }

    /**
     * Modifica el valor del radio de la rueda
     *
     * @param radio
     */
    public void setRadioRueda(float radio) {
        this.radio = radio;
    }

    /**
     * Devuelve el valor del radio de la rueda
     *
     * @return radio
     */
    public float getRadioRueda() {
        return radio;
    }

    /**
     * Modifica el color de la rueda
     *
     * @param color
     */
    public void setColorRueda(int color) {
        this.color = color;
    }

    /**
     * Devuelve el color de la rueda
     *
     * @return color
     */
    public int getColorRueda() {
        return color;
    }

    /**
     * Modifica la posición (x,y) de la rueda
     * POLIMORFISMO: Solo traslación
     *
     * @param desplazamientoX
     * @param desplazamientoY
     */
    public void moverRueda(float desplazamientoX, float desplazamientoY) {

        this.posicionX = this.posicionInicialX + desplazamientoX;
        this.posicionY = this.posicionInicialY + desplazamientoY;

    }

    /**
     * Modifica la posición angular en grados de la rueda
     * POLIMORFISMO: Solo rotación
     *
     * @param posicionAngular
     */
    public void moverRueda(float posicionAngular) {
        this.posicionX = this.posicionInicialX;
        this.posicionY = this.posicionInicialY;

        this.posicionAngular = posicionAngular;
    }

    /**
     * Modifica la posición (x,y) y genera
     * una rotación alrededor del eje que pasa por (x,y)
     * POLIMORFISMO: Traslación y rotación
     *
     * @param desplazamientoX
     * @param desplazamientoY
     * @param posicionAngular
     */

    public void moverRueda(float desplazamientoX, float desplazamientoY, float posicionAngular) {
        this.posicionX = this.posicionInicialX + desplazamientoX;
        this.posicionY = this.posicionInicialY + desplazamientoY;

        this.posicionAngular = posicionAngular;
    }

    /**
     * Método para dibujar la rueda en el canvas
     * Diseño: Llanta negra, disco de color, centro blanco,
     * 4 radios blancos en cruz, círculos blancos en extremos
     *
     * @param canvas
     * @param pincel
     */
    public void dibujese(Canvas canvas, Paint pincel) {

        canvas.save();
        // Rotar todo el dibujo según posicionAngular
        canvas.rotate(posicionAngular, posicionX, posicionY);

        // 1. Dibujar DISCO CENTRAL de color (relleno) - primero, más pequeño
        pincel.setStyle(Paint.Style.FILL);
        pincel.setColor(color);
        canvas.drawCircle(posicionX, posicionY, radio * 0.92f, pincel);

        // 2. Dibujar LLANTA NEGRA (círculo exterior con grosor) - encima
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeWidth(radio * 0.15f); // Grosor de la llanta
        pincel.setColor(Color.BLACK);
        canvas.drawCircle(posicionX, posicionY, radio, pincel);

        // 3. Dibujar 4 RADIOS BLANCOS en cruz (0°, 90°, 180°, 270°)
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeWidth(radio * 0.1f); // Grosor de los radios
        pincel.setColor(Color.WHITE);

        // Radio hacia arriba (0°) - hasta la mitad del radio
        canvas.drawLine(posicionX, posicionY, posicionX, posicionY - radio * 0.5f, pincel);
        // Radio hacia derecha (90°)
        canvas.drawLine(posicionX, posicionY, posicionX + radio * 0.5f, posicionY, pincel);
        // Radio hacia abajo (180°)
        canvas.drawLine(posicionX, posicionY, posicionX, posicionY + radio * 0.5f, pincel);
        // Radio hacia izquierda (270°)
        canvas.drawLine(posicionX, posicionY, posicionX - radio * 0.5f, posicionY, pincel);

        // 4. Dibujar CÍRCULOS BLANCOS en los extremos de los radios (más pequeños)
        pincel.setStyle(Paint.Style.FILL);
        pincel.setColor(Color.WHITE);
        float radioCirculo = radio * 0.08f; // Tamaño de los círculos en los extremos (más pequeños)

        // Círculo arriba
        canvas.drawCircle(posicionX, posicionY - radio * 0.5f, radioCirculo, pincel);
        // Círculo derecha
        canvas.drawCircle(posicionX + radio * 0.5f, posicionY, radioCirculo, pincel);
        // Círculo abajo
        canvas.drawCircle(posicionX, posicionY + radio * 0.5f, radioCirculo, pincel);
        // Círculo izquierda
        canvas.drawCircle(posicionX - radio * 0.5f, posicionY, radioCirculo, pincel);

        // 5. Dibujar CÍRCULO BLANCO CENTRAL (hub/centro) - MÁS GRANDE
        pincel.setColor(Color.WHITE);
        canvas.drawCircle(posicionX, posicionY, radio * 0.25f, pincel);

        // Regresar la rotación
        canvas.restore();
    }

}
