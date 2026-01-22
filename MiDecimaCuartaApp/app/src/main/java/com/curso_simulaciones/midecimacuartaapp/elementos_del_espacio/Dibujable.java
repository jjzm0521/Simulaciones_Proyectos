package com.curso_simulaciones.midecimacuartaapp.elementos_del_espacio;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Interface Dibujable
 * Define el contrato para objetos que pueden ser dibujados en un Canvas
 */
public interface Dibujable {
    /**
     * Método para dibujar el objeto en el canvas
     * 
     * @param canvas El canvas donde se dibujará el objeto
     * @param paint  El objeto Paint con los estilos de dibujo
     */
    void dibujese(Canvas canvas, Paint paint);
}
