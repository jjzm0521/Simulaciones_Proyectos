package com.curso_simulaciones.mivigesimasegundaapp.vista;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import com.curso_simulaciones.mivigesimasegundaapp.datos.AlmacenDatosRAM;
import com.curso_simulaciones.simulphysics.objetos_laboratorio.*;

public class Pizarra extends View {
    private ObjetoLaboratorio[] objetos;
    private Paint pincel = new Paint();

    public Pizarra(Context context, AlmacenDatosRAM datos) {
        super(context);
        pincel.setAntiAlias(true);
    }

    public void setEstadoEscena(ObjetoLaboratorio[] objetos) {
        this.objetos = objetos;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        pincel.setColor(Color.BLACK);
        canvas.drawRect(CR.pcApxX(10), CR.pcApxY(80), CR.pcApxX(90), CR.pcApxY(81.5f), pincel);

        if (objetos == null)
            return;

        Polea p1 = (Polea) objetos[1];
        Polea p2 = (Polea) objetos[2];
        Polea pP = (Polea) objetos[3];
        Masa m1 = (Masa) objetos[4];
        Masa m2 = (Masa) objetos[5];
        Masa m3 = (Masa) objetos[6];
        float r = CR.pcApxL(2.5f);

        pincel.setColor(Color.RED);
        pincel.setStrokeWidth(CR.pcApxL(0.45f));

        // Cuerda m1 -> p1 (Tangente izquierda)
        canvas.drawLine(p1.getPosicionX() - r, p1.getPosicionY(), m1.getPosicionX(), m1.getPosicionY(), pincel);

        // Cuerda p1 -> p2 (Tangente superior horizontal)
        canvas.drawLine(p1.getPosicionX(), p1.getPosicionY() - r, p2.getPosicionX(), p2.getPosicionY() - r, pincel);

        // AJUSTE: Cuerda p2 (Tangente derecha) -> pP (Centro)
        // La cuerda sale del borde derecho de p2 y llega exactamente al centro de pP
        canvas.drawLine(p2.getPosicionX() + r, p2.getPosicionY(), pP.getPosicionX(), pP.getPosicionY(), pincel);

        // Cuerdas de la polea verde P a sus masas (tangentes laterales)
        canvas.drawLine(pP.getPosicionX() - r, pP.getPosicionY(), m2.getPosicionX(), m2.getPosicionY(), pincel);
        canvas.drawLine(pP.getPosicionX() + r, pP.getPosicionY(), m3.getPosicionX(), m3.getPosicionY(), pincel);

        for (ObjetoLaboratorio obj : objetos) {
            if (obj != null)
                obj.dibujese(canvas, pincel);
        }

        pincel.setColor(Color.BLACK);
        pincel.setTextSize(CR.pcApxL(3.8f));
        canvas.drawText("m1", m1.getPosicionX() - 3.5f * r, m1.getPosicionY() + r, pincel);
        canvas.drawText("P", pP.getPosicionX() + 1.5f * r, pP.getPosicionY(), pincel);
        canvas.drawText("m2", m2.getPosicionX() + 1.2f * r, m2.getPosicionY() + r, pincel);
        canvas.drawText("m3", m3.getPosicionX() + 1.2f * r, m3.getPosicionY() + r, pincel);
    }
}
