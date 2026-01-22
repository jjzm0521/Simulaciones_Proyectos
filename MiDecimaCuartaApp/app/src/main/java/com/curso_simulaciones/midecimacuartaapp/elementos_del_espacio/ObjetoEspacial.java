package com.curso_simulaciones.midecimacuartaapp.elementos_del_espacio;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Clase base abstracta ObjetoEspacial
 * Representa un objeto espacial genérico que puede ser dibujado
 * Implementa la interface Dibujable pero NO implementa el método dibujese()
 */
public abstract class ObjetoEspacial implements Dibujable {

    /**
     * Método abstracto que debe ser implementado por las clases derivadas
     * o por clases intermedias abstractas
     */
    @Override
    public abstract void dibujese(Canvas canvas, Paint paint);
}
