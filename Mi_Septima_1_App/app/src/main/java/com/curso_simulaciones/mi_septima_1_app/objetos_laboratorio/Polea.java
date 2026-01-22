package com.curso_simulaciones.mi_septima_1_app.objetos_laboratorio;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Polea {

    private float radio;
    private float posicionX, posicionY, posicionAngular;
    private int color = Color.rgb(255, 0, 0); // rojo

    /**
     * Constructor por defecto
     * Polea centrada en (0,0), con posición angular 0,
     * de color rojo y de radio 100f
     */
    public Polea() {
        this.radio = 100f;
    }

    /**
     * Constructor de Polea centrada en (posicionX, posicionY),
     * con posición angular cero de color rojo y diámetro igual a 2*radio
     */
    public Polea(float posicionX, float posicionY, float radio) {
        this.posicionX = posicionX;
        this.posicionY = posicionY;
        this.radio = radio;
    }

    /**
     * Modifica el valor del radio de la polea
     * @param radio
     */
    public void setRadioPolea(float radio) {
        this.radio = radio;
    }

    /**
     * Devuelve el valor del radio de la polea
     * @return radio
     */
    public float getRadioPolea() {
        return radio;
    }

    /**
     * Modifica el color de la polea
     * @param color
     */
    public void setColorPolea(int color) {
        this.color = color;
    }

    /**
     * Devuelve el color de la polea
     * @return color
     */
    public int getColorPolea() {
        return color;
    }

    /**
     * Modifica la posición (x,y) de la polea
     * @param posicionX
     * @param posicionY
     */
    public void trasladarPolea(float posicionX, float posicionY) {
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }

    /**
     * Modifica la posición angular en grados de la polea
     * @param posicionAngular
     */
    public void rotarPolea(float posicionAngular) {
        this.posicionAngular = posicionAngular;
    }

    public void dibujese(Canvas canvas, Paint pincel) {
        // Estilo del pincel
        pincel.setStyle(Paint.Style.STROKE);

        // Grosor del pincel
        pincel.setStrokeWidth(2f);

        // Color del pincel
        pincel.setColor(color);

        canvas.save();

        // Rotar según la posición angular
        canvas.rotate(posicionAngular, posicionX, posicionY);

        // Dibujar circunferencia de la polea
        canvas.drawCircle(posicionX, posicionY, radio, pincel);

        // Dibujar las líneas radiales de la polea
        for (int i = 0; i < 12; i = i + 1) {
            // Rotar de a 30 grados (aunque el código original dice 36, mantengo tu lógica)
            canvas.rotate(i * 36f, posicionX, posicionY);
            canvas.drawLine(posicionX, posicionY, posicionX, posicionY - radio, pincel);
            // Retroceder la rotación
            canvas.rotate(-i * 36f, posicionX, posicionY);
        }

        // Regresar el canvas a su estado original
        canvas.restore();
    }
}