package com.curso_simulaciones.midecimacuartaapp.vista;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.curso_simulaciones.midecimacuartaapp.elementos_del_espacio.ObjetoEspacial;

/**
 * Clase Pizarra
 * Vista personalizada donde se dibujarán los objetos espaciales
 */
public class Pizarra extends View {
    // Array de objetos espaciales
    private ObjetoEspacial[] objetosEspaciales;

    // Paint para dibujar
    private Paint paint;

    /**
     * Constructor
     * 
     * @param context Contexto de la aplicación
     */
    public Pizarra(Context context) {
        super(context);
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    /**
     * Establece el estado de la escena con los objetos a dibujar
     * 
     * @param objetosEspaciales Array de objetos espaciales
     */
    public void setEstadoEscena(ObjetoEspacial[] objetosEspaciales) {
        this.objetosEspaciales = objetosEspaciales;
        // Forzar redibujado
        invalidate();
    }

    /**
     * Método onDraw - dibuja todos los objetos en el canvas
     * 
     * @param canvas Canvas donde se dibujará
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Fondo negro (espacio)
        canvas.drawColor(Color.BLACK);

        // Dibujar todos los objetos
        if (objetosEspaciales != null) {
            for (ObjetoEspacial objeto : objetosEspaciales) {
                if (objeto != null) {
                    objeto.dibujese(canvas, paint);
                }
            }
        }
    }

    /**
     * Se llama cuando cambia el tamaño de la vista
     * Inicializa las coordenadas relativas
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Inicializar el sistema de coordenadas relativas
        CR.anchoPizarra = w;
        CR.altoPizarra = h;
    }
}
