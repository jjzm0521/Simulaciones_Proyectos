package com.curso_simulaciones.midecimaterceraapp.objeto_laboratorio;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Polea extends CuerpoRigido {

    protected float radio;

    public Polea() {
        super();
        this.radio = 50f;
    }

    public Polea(float posicionInicialCentroMasaX, float posicionInicialCentroMasaY, float radio) {
        super(posicionInicialCentroMasaX, posicionInicialCentroMasaY);
        this.radio = radio;
    }

    public void setRadio(float radio) {
        this.radio = radio;
    }

    public float getRadio() {
        return radio;
    }

    public void dibujese(Canvas canvas, Paint pincel) {

        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeWidth(2f);
        pincel.setColor(color);

        canvas.save();
        canvas.rotate(posicionAngularRotacionEjeXY, posicionEjeRotacionX, posicionEjeRotacionY);
        canvas.drawCircle(posicionCentroMasaX, posicionCentroMasaY, radio, pincel);

        for (int i = 0; i < 12; i = i + 1) {
            canvas.rotate(i * 36f, posicionCentroMasaX, posicionCentroMasaY);
            canvas.drawLine(posicionCentroMasaX, posicionCentroMasaY, posicionCentroMasaX, posicionCentroMasaY - radio,
                    pincel);
            canvas.rotate(-i * 36f, posicionCentroMasaX, posicionCentroMasaY);
        }

        canvas.restore();
    }
}
