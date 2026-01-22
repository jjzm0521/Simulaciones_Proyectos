package com.curso_simulaciones.midecimaterceraapp.objeto_laboratorio;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Rueda extends CuerpoRigido {

    protected float radio;

    public Rueda() {
        super();
        this.radio = 50f;
    }

    public Rueda(float posicionInicialCentroMasaX, float posicionInicialCentroMasaY, float radio) {
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

        pincel.setStyle(Paint.Style.FILL);
        pincel.setColor(color);

        canvas.save();
        canvas.rotate(posicionAngularRotacionEjeXY, posicionEjeRotacionX, posicionEjeRotacionY);
        canvas.drawCircle(posicionCentroMasaX, posicionCentroMasaY, radio, pincel);
        canvas.restore();
    }
}
