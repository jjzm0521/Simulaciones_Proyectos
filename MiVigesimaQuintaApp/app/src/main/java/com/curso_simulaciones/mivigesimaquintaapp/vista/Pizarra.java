package com.curso_simulaciones.mivigesimaquintaapp.vista;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

import com.curso_simulaciones.mivigesimaquintaapp.modelo.AlmacenDatosRAM;
import com.curso_simulaciones.simulphysics.objetos_laboratorio.ObjetoLaboratorio;
import com.curso_simulaciones.simulphysics.objetos_laboratorio.Polea;
import com.curso_simulaciones.simulphysics.objetos_laboratorio.Masa;

/**
 * Vista personalizada que dibuja la escena del polipasto.
 * Dibuja objetos de laboratorio, cuerdas y valores calculados.
 */
public class Pizarra extends View {

    private ObjetoLaboratorio[] objetosLab;
    private Paint pincel;

    // Sistema de coordenadas
    private float origen_x = 0;
    private float origen_y = 0;
    private float m_x = 1;
    private float m_y = 1;

    public Pizarra(Context context) {
        super(context);
        pincel = new Paint();
        pincel.setAntiAlias(true);
    }

    /**
     * Configura el sistema de coordenadas para el canvas.
     */
    public void setSistemaCoordenadas(float origen_x, float origen_y, float m_x, float m_y) {
        this.origen_x = origen_x;
        this.origen_y = origen_y;
        this.m_x = m_x;
        this.m_y = m_y;
    }

    /**
     * Establece los objetos a dibujar en la escena.
     */
    public void setEstadoEscena(ObjetoLaboratorio[] cuerpos) {
        this.objetosLab = cuerpos;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Fondo
        canvas.drawColor(Color.rgb(250, 250, 250));

        if (objetosLab == null)
            return;

        // Dibujar cuerdas primero (debajo de los objetos)
        dibujarCuerdas(canvas);

        // Dibujar objetos de laboratorio
        dibujarEscena(canvas);

        // Dibujar valores calculados y etiquetas
        dibujarLetreros(canvas);

        // Dibujar Eje Coordenado y Gravedad
        dibujarEjes(canvas);

        // Refrescar para animación
        invalidate();
    }

    /**
     * Dibuja todos los objetos de laboratorio.
     */
    private void dibujarEscena(Canvas canvas) {
        for (ObjetoLaboratorio obj : objetosLab) {
            if (obj != null) {
                obj.dibujese(canvas, pincel);
            }
        }
    }

    /**
     * Dibuja las cuerdas que conectan poleas y masas.
     * Sistema responsivo: usa posiciones directas de los objetos.
     */
    private void dibujarCuerdas(Canvas canvas) {
        if (objetosLab == null || objetosLab.length < 7)
            return;

        pincel.setColor(Color.rgb(180, 60, 60)); // Rojo oscuro elegante
        pincel.setStrokeWidth(CR.pcApxL(0.45f));
        pincel.setStyle(Paint.Style.STROKE);

        float r = AlmacenDatosRAM.radio;

        // Obtener posiciones de los objetos
        Polea p1 = (Polea) objetosLab[1];
        Polea p2 = (Polea) objetosLab[2];
        Polea pP = (Polea) objetosLab[3];
        Masa m1 = (Masa) objetosLab[4];
        Masa m2 = (Masa) objetosLab[5];
        Masa m3 = (Masa) objetosLab[6];

        // Cuerda: m1 -> polea 1 (tangente izquierda)
        canvas.drawLine(p1.getPosicionX() - r, p1.getPosicionY(),
                m1.getPosicionX(), m1.getPosicionY(), pincel);

        // Cuerda: polea 1 -> polea 2 (tangente superior horizontal)
        canvas.drawLine(p1.getPosicionX(), p1.getPosicionY() - r,
                p2.getPosicionX(), p2.getPosicionY() - r, pincel);

        // Cuerda: polea 2 (tangente derecha) -> polea P (centro)
        canvas.drawLine(p2.getPosicionX() + r, p2.getPosicionY(),
                pP.getPosicionX(), pP.getPosicionY(), pincel);

        // Cuerdas: polea P -> m2 y m3 (tangentes laterales)
        canvas.drawLine(pP.getPosicionX() - r, pP.getPosicionY(),
                m2.getPosicionX(), m2.getPosicionY(), pincel);
        canvas.drawLine(pP.getPosicionX() + r, pP.getPosicionY(),
                m3.getPosicionX(), m3.getPosicionY(), pincel);

        pincel.setStyle(Paint.Style.FILL);
    }

    /**
     * Dibuja los valores calculados en la zona izquierda.
     */
    private void dibujarLetreros(Canvas canvas) {
        pincel.setTextSize(CR.pcApxL(2.8f));
        pincel.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));
        pincel.setColor(Color.rgb(60, 60, 60));

        float xTexto = CR.pcApxX(2f);
        float yInicio = CR.pcApxY(10f);
        float espaciado = CR.pcApxY(3.8f);

        // Título
        pincel.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        canvas.drawText("VALORES", xTexto, yInicio, pincel);
        pincel.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));

        // Masas
        canvas.drawText("m1 = " + String.format("%.1f", AlmacenDatosRAM.m1) + " kg", xTexto, yInicio + espaciado,
                pincel);
        canvas.drawText("m2 = " + String.format("%.1f", AlmacenDatosRAM.m2) + " kg", xTexto, yInicio + 2 * espaciado,
                pincel);
        canvas.drawText("m3 = " + String.format("%.1f", AlmacenDatosRAM.m3) + " kg", xTexto, yInicio + 3 * espaciado,
                pincel);

        // Aceleraciones
        canvas.drawText("a1 = " + String.format("%.2f", AlmacenDatosRAM.a1) + " m/s2", xTexto, yInicio + 5 * espaciado,
                pincel);
        canvas.drawText("aP = " + String.format("%.2f", AlmacenDatosRAM.aP) + " m/s2", xTexto, yInicio + 6 * espaciado,
                pincel);
        canvas.drawText("a2 = " + String.format("%.2f", AlmacenDatosRAM.a2) + " m/s2", xTexto, yInicio + 7 * espaciado,
                pincel);
        canvas.drawText("a3 = " + String.format("%.2f", AlmacenDatosRAM.a3) + " m/s2", xTexto, yInicio + 8 * espaciado,
                pincel);

        // Tensiones
        canvas.drawText("T1 = " + String.format("%.1f", AlmacenDatosRAM.T1) + " N", xTexto, yInicio + 10 * espaciado,
                pincel);
        canvas.drawText("T2 = " + String.format("%.1f", AlmacenDatosRAM.T2) + " N", xTexto, yInicio + 11 * espaciado,
                pincel);

        // Posiciones
        canvas.drawText("y1 = " + String.format("%.3f", AlmacenDatosRAM.y1_en_metros) + " m", xTexto,
                yInicio + 13 * espaciado, pincel);
        canvas.drawText("yP = " + String.format("%.3f", AlmacenDatosRAM.yP_en_metros) + " m", xTexto,
                yInicio + 14 * espaciado, pincel);
        canvas.drawText("y2 = " + String.format("%.3f", AlmacenDatosRAM.y2_en_metros) + " m", xTexto,
                yInicio + 15 * espaciado, pincel);
        canvas.drawText("y3 = " + String.format("%.3f", AlmacenDatosRAM.y3_en_metros) + " m", xTexto,
                yInicio + 16 * espaciado, pincel);

        // Tiempo
        pincel.setColor(Color.rgb(0, 100, 180));
        canvas.drawText("t = " + String.format("%.2f", AlmacenDatosRAM.tiempo) + " s", xTexto, yInicio + 18 * espaciado,
                pincel);

        // Etiquetas de las masas en el dibujo
        pincel.setColor(Color.BLACK);
        pincel.setTextSize(CR.pcApxL(3f));
        pincel.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));

        if (objetosLab != null && objetosLab.length >= 7) {
            Polea pP = (Polea) objetosLab[3];
            Masa m1 = (Masa) objetosLab[4];
            Masa m2 = (Masa) objetosLab[5];
            Masa m3 = (Masa) objetosLab[6];
            float r = AlmacenDatosRAM.radio;

            // m1 - etiqueta a la izquierda
            if (m1 != null) {
                float x = m1.getPosicionX() - 3.5f * r;
                float y = m1.getPosicionY() + r;
                canvas.drawText("m1", x, y, pincel);
            }
            // m2 - etiqueta a la derecha
            if (m2 != null) {
                float x = m2.getPosicionX() + 1.2f * r;
                float y = m2.getPosicionY() + r;
                canvas.drawText("m2", x, y, pincel);
            }
            // m3 - etiqueta a la derecha
            if (m3 != null) {
                float x = m3.getPosicionX() + 1.2f * r;
                float y = m3.getPosicionY() + r;
                canvas.drawText("m3", x, y, pincel);
            }
            // P - etiqueta a la derecha
            if (pP != null) {
                float x = pP.getPosicionX() + 1.5f * r;
                float y = pP.getPosicionY();
                canvas.drawText("P", x, y, pincel);
            }
        }

        // Eje Y eliminado

        // Copyright
        pincel.setTextSize(CR.pcApxL(1.8f));
        pincel.setColor(Color.GRAY);
        canvas.drawText("Polipasto Compuesto - Simulacion Fisica", CR.pcApxX(25f), CR.pcApxY(98f), pincel);
    }

    /**
     * Dibuja un pequeño eje de coordenadas indicando la dirección positiva de Y
     * (abajo).
     * Se ubica en la zona derecha, entre la animación y el panel de controles.
     */
    private void dibujarEjes(Canvas canvas) {
        float xEje = CR.pcApxX(92f); // Zona derecha
        float yInicio = CR.pcApxY(10f);
        float yFin = CR.pcApxY(25f);
        float tamFlecha = CR.pcApxL(1.5f);

        pincel.setColor(Color.DKGRAY);
        pincel.setStrokeWidth(CR.pcApxL(0.3f));
        pincel.setStyle(Paint.Style.STROKE);

        // Línea vertical
        canvas.drawLine(xEje, yInicio, xEje, yFin, pincel);

        // Cabeza de flecha (apuntando abajo)
        pincel.setStyle(Paint.Style.FILL);
        android.graphics.Path flecha = new android.graphics.Path();
        flecha.moveTo(xEje, yFin);
        flecha.lineTo(xEje - tamFlecha / 2, yFin - tamFlecha);
        flecha.lineTo(xEje + tamFlecha / 2, yFin - tamFlecha);
        flecha.close();
        canvas.drawPath(flecha, pincel);

        // Etiqueta "y (+)"
        pincel.setTextSize(CR.pcApxL(2.5f));
        pincel.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        pincel.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("y (+)", xEje, yFin + tamFlecha * 2.5f, pincel);

        // Etiqueta "g" con flechita pequeña
        canvas.drawText("g", xEje + tamFlecha * 2, yInicio + (yFin - yInicio) / 2, pincel);

        pincel.setTextAlign(Paint.Align.LEFT); // Restaurar alineación
    }
}
