package com.curso_simulaciones.midecimaterceraapp.objeto_laboratorio;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class ObjetoLaboratorio implements Dibujable {

    /**
     * Método para que se dibujen los objetos Dibujables,
     * es decir, los objetos que implementan la interface Dibujable
     * 
     * @param canvas
     * @param pincel
     */
    public abstract void dibujese(Canvas canvas, Paint pincel);
}
