package com.curso_simulaciones.midecimacuartaapp.elementos_del_espacio;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Clase Selenita (habitante de la Luna)
 * Hereda de Extraterrestre
 * Implementa dibujese() para dibujar la caricatura del selenita
 */
public class Selenita extends Extraterrestre {

        /**
         * Constructor por defecto
         */
        public Selenita() {
                super();
        }

        /**
         * Constructor con posición del centroide
         */
        public Selenita(float posicionCentroideX, float posicionCentroideY) {
                super(posicionCentroideX, posicionCentroideY);
        }

        @Override
        public void dibujese(Canvas canvas, Paint pincel) {
                canvas.save();

                // magnificar
                canvas.scale(magnificacion, magnificacion, posicionCentroideX, posicionCentroideY);
                // rotar
                canvas.rotate(posicionAngularRotacionEjeXY, posicionEjeRotacionX, posicionEjeRotacionY);

                float ancho = 80f;
                float alto = 100f;
                float radioSemicirculo = 20f;

                // Dibujar semicírculo en la cabeza con relleno rojo (alineado con el
                // rectángulo)
                pincel.setStyle(Paint.Style.FILL);
                pincel.setColor(Color.RED);
                // Usar drawArc para crear un semicírculo (180 grados en la parte superior)
                canvas.drawArc(posicionCentroideX - radioSemicirculo,
                                posicionCentroideY - alto / 2 - radioSemicirculo,
                                posicionCentroideX + radioSemicirculo,
                                posicionCentroideY - alto / 2 + radioSemicirculo,
                                180f, 180f, true, pincel);

                // Dibujar cuerpo rectangular rojo
                pincel.setStyle(Paint.Style.FILL);
                pincel.setColor(Color.RED);
                canvas.drawRect(posicionCentroideX - ancho / 2, posicionCentroideY - alto / 2,
                                posicionCentroideX + ancho / 2, posicionCentroideY + alto / 2, pincel);

                // Dibujar ojos cuadrados blancos (más centrados y más pequeños)
                pincel.setColor(Color.WHITE);
                float tamañoOjo = 12f; // Reducido de 15f a 12f
                float separacionOjos = 10f; // Reducido para centrar más los ojos
                // Ojo izquierdo
                canvas.drawRect(posicionCentroideX - separacionOjos - tamañoOjo,
                                posicionCentroideY - alto / 4 - tamañoOjo,
                                posicionCentroideX - separacionOjos, posicionCentroideY - alto / 4, pincel);
                // Ojo derecho
                canvas.drawRect(posicionCentroideX + separacionOjos, posicionCentroideY - alto / 4 - tamañoOjo,
                                posicionCentroideX + separacionOjos + tamañoOjo, posicionCentroideY - alto / 4, pincel);

                // Dibujar punto como nariz
                pincel.setColor(Color.BLACK);
                canvas.drawCircle(posicionCentroideX, posicionCentroideY, 5f, pincel);

                // Dibujar boca rectangular blanca (alineada con el centro de los ojos, más
                // baja)
                pincel.setColor(Color.WHITE);
                // El ancho de la boca se alinea con el centro de los ojos
                float anchoBoca = (separacionOjos + tamañoOjo / 2) * 2;
                float altoBoca = 10f; // Reducido para hacerla menos gruesa
                float distanciaDesdeCentro = 20f; // Más baja
                canvas.drawRect(posicionCentroideX - anchoBoca / 2,
                                posicionCentroideY + distanciaDesdeCentro - altoBoca / 2,
                                posicionCentroideX + anchoBoca / 2,
                                posicionCentroideY + distanciaDesdeCentro + altoBoca / 2, pincel);

                canvas.restore();
        }
}
