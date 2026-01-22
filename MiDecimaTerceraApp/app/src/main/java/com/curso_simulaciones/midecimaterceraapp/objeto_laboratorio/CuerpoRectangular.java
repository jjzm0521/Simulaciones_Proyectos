package com.curso_simulaciones.midecimaterceraapp.objeto_laboratorio;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class CuerpoRectangular extends CuerpoRigido {

    protected float ancho;
    protected float alto;

    public CuerpoRectangular() {
        super();
        this.ancho = 100f;
        this.alto = 50f;
    }

    public CuerpoRectangular(float posicionInicialCentroMasaX, float posicionInicialCentroMasaY, float ancho,
            float alto) {
        super(posicionInicialCentroMasaX, posicionInicialCentroMasaY);
        this.ancho = ancho;
        this.alto = alto;
    }

    public void setAncho(float ancho) {
        this.ancho = ancho;
    }

    public float getAncho() {
        return ancho;
    }

    public void setAlto(float alto) {
        this.alto = alto;
    }

    public float getAlto() {
        return alto;
    }

    public void dibujese(Canvas canvas, Paint pincel) {

        pincel.setStyle(Paint.Style.FILL);
        pincel.setColor(color);

        canvas.save();
        canvas.rotate(posicionAngularRotacionEjeXY, posicionEjeRotacionX, posicionEjeRotacionY);

        RectF rectangulo = new RectF(
                posicionCentroMasaX - ancho / 2,
                posicionCentroMasaY - alto / 2,
                posicionCentroMasaX + ancho / 2,
                posicionCentroMasaY + alto / 2);

        canvas.drawRect(rectangulo, pincel);
        canvas.restore();
    }
}
