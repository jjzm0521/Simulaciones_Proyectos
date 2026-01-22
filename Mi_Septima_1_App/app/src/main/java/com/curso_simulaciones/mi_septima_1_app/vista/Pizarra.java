package com.curso_simulaciones.mi_septima_1_app.vista;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import com.curso_simulaciones.mi_septima_1_app.objetos_laboratorio.Polea;

public class Pizarra extends View {

    private Polea poleas[];

    /**
     * Constructor
     * @param context
     */
    public Pizarra(Context context) {
        super(context);
    }

    /**
     * Método para cambiar la escena
     * @param poleas
     */
    public void setEstadoEscena(Polea[] poleas) {
        this.poleas = poleas;
    }

    // Método para dibujar la escena
    private void dibujarEscena(Canvas canvas, Paint pincel) {
        for (int i = 0; i < poleas.length; i++) {
            if (poleas[i] != null) {
                poleas[i].dibujese(canvas, pincel);
            }
        }
    }

    // Método principal para dibujar la vista
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint pincel = new Paint();

        // Evita el efecto de sierra (aliasing) para bordes más suaves
        pincel.setAntiAlias(true);

        if (poleas != null) {
            dibujarEscena(canvas, pincel);
        }

        // Necesario para actualizar los dibujos en animaciones
        // Esto fuerza a la vista a redibujarse constantemente
        invalidate();
    }
}