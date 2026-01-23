package com.curso_simulaciones.midecimacuartaapp.elementos_del_espacio;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Clase Marciano
 * Hereda de Extraterrestre
 * Implementa dibujese() para dibujar la caricatura del marciano
 * Código basado en la ayuda proporcionada en el taller
 */
public class Marciano extends Extraterrestre {

    /**
     * Constructor por defecto
     * del marciano con "centroide" en (0,0)
     * y de radio 50f
     */
    public Marciano() {
        super();
    }

    /**
     * Constructor del marciano con "centroide"
     * en (posicionCentroideX,posicionCentroideY)
     * y diámetro igual a 2*radio
     */
    public Marciano(float posicionCentroideX, float posicionCentroideY) {
        super(posicionCentroideX, posicionCentroideY);
    }

    // se implementó este método que en su clase madre es abstracto
    public void dibujese(Canvas canvas, Paint pincel) {

        // Activar anti-aliasing para bordes suaves
        pincel.setAntiAlias(true);
        // estilo del pincel
        pincel.setStyle(Paint.Style.STROKE);
        // grosor del pincel
        pincel.setStrokeWidth(2f);
        // color del pincel
        pincel.setColor(color);

        canvas.save();

        // magnificar
        canvas.scale(magnificacion, magnificacion, posicionCentroideX, posicionCentroideY);
        // rotar
        canvas.rotate(posicionAngularRotacionEjeXY, posicionEjeRotacionX, posicionEjeRotacionY);
        // dibujar círculo de la cara del marciano
        float radio = 50f;
        pincel.setStyle(Paint.Style.FILL);
        canvas.drawCircle(posicionCentroideX, posicionCentroideY, radio, pincel);
        pincel.setColor(Color.BLACK);
        // dibujar circunferencia de la cara del marciano
        pincel.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(posicionCentroideX, posicionCentroideY, radio, pincel);

        // dibujar los ojos del marcianito (más grandes y más separados)
        float separacion = 0.45f * radio; // Más separados horizontalmente
        float radioOjo = 0.28f * radio; // Ojos un poco más cortos

        // Ajuste de posición: el borde inferior del ojo toca la línea central (Y del
        // centroide)
        // y_centro_ojo = y_centroide - radio_ojo
        float posicionOjoCentroIzquierdoX = posicionCentroideX - separacion;
        float posicionOjoCentroIzquierdoY = posicionCentroideY - radioOjo;
        float posicionOjoCentroDerechoX = posicionCentroideX + separacion;
        float posicionOjoCentroDerechoY = posicionCentroideY - radioOjo;

        pincel.setStyle(Paint.Style.FILL);
        pincel.setColor(Color.WHITE);
        canvas.drawCircle(posicionOjoCentroIzquierdoX, posicionOjoCentroIzquierdoY, radioOjo, pincel);
        canvas.drawCircle(posicionOjoCentroDerechoX, posicionOjoCentroDerechoY, radioOjo, pincel);
        // dibujar las pupilas en forma de rombo verde
        pincel.setColor(color);
        float tamañoRombo = 0.12f * radio;

        // Rombo izquierdo (usando Path para crear el diamante)
        android.graphics.Path romboIzq = new android.graphics.Path();
        romboIzq.moveTo(posicionOjoCentroIzquierdoX, posicionOjoCentroIzquierdoY - tamañoRombo); // arriba
        romboIzq.lineTo(posicionOjoCentroIzquierdoX + tamañoRombo, posicionOjoCentroIzquierdoY); // derecha
        romboIzq.lineTo(posicionOjoCentroIzquierdoX, posicionOjoCentroIzquierdoY + tamañoRombo); // abajo
        romboIzq.lineTo(posicionOjoCentroIzquierdoX - tamañoRombo, posicionOjoCentroIzquierdoY); // izquierda
        romboIzq.close();
        canvas.drawPath(romboIzq, pincel);

        // Rombo derecho
        android.graphics.Path romboDer = new android.graphics.Path();
        romboDer.moveTo(posicionOjoCentroDerechoX, posicionOjoCentroDerechoY - tamañoRombo); // arriba
        romboDer.lineTo(posicionOjoCentroDerechoX + tamañoRombo, posicionOjoCentroDerechoY); // derecha
        romboDer.lineTo(posicionOjoCentroDerechoX, posicionOjoCentroDerechoY + tamañoRombo); // abajo
        romboDer.lineTo(posicionOjoCentroDerechoX - tamañoRombo, posicionOjoCentroDerechoY); // izquierda
        romboDer.close();
        canvas.drawPath(romboDer, pincel);

        // dibujar nariz negra (punto pequeño al centro)
        pincel.setColor(Color.BLACK);
        canvas.drawCircle(posicionCentroideX, posicionCentroideY, 3f, pincel); // Más pequeña

        // dibujar la boca del marcianito (más fina y alargada, menos redondeada)
        float altoBoca = 0.12f * radio; // Más fina (reducido de 0.2f)
        float anchoBoca = 0.65f * radio; // Un poco más ancha (aumentado de 0.6f)
        pincel.setColor(Color.WHITE);
        float descenso = 0.45f * radio; // Más abajo
        float posicionBocaCentroX = posicionCentroideX;
        float posicionBocaCentroY = posicionCentroideY + descenso;
        float xs_izquierda = posicionBocaCentroX - 0.5f * anchoBoca;
        float ys_izquierda = posicionBocaCentroY - 0.5f * altoBoca;
        float xi_derecha = posicionBocaCentroX + 0.5f * anchoBoca;
        float yi_derecha = posicionBocaCentroY + 0.5f * altoBoca;
        // Usar drawRoundRect con esquinas menos redondeadas
        canvas.drawRoundRect(xs_izquierda, ys_izquierda, xi_derecha, yi_derecha, 4f, 4f, pincel);

        // dibujar las antenas del marcianito (grosor igual al diámetro del círculo)
        float radioBolitaAntena = 8f;
        pincel.setColor(color);
        pincel.setStrokeWidth(radioBolitaAntena * 2); // Grosor = diámetro del círculo
        pincel.setStrokeCap(Paint.Cap.ROUND); // Puntas redondeadas
        pincel.setStyle(Paint.Style.STROKE);

        // Configuración de antenas
        float longitudAntena = radio * 0.8f;
        float anguloInclinacion = (float) Math.toRadians(35); // Ángulo de la antena respecto a la vertical
        float anguloBase = (float) Math.toRadians(25); // Ángulo de separación de la base respecto a la vertical

        // Antena Derecha
        // Punto de inicio en la circunferencia de la cabeza
        float x_i_antena_derecha = posicionCentroideX + radio * (float) Math.sin(anguloBase);
        float y_i_antena_derecha = posicionCentroideY - radio * (float) Math.cos(anguloBase);
        // Punto final proyectado
        float x_s_antena_derecha = x_i_antena_derecha + longitudAntena * (float) Math.sin(anguloInclinacion);
        float y_s_antena_derecha = y_i_antena_derecha - longitudAntena * (float) Math.cos(anguloInclinacion);

        canvas.drawLine(x_i_antena_derecha, y_i_antena_derecha, x_s_antena_derecha, y_s_antena_derecha, pincel);

        // Antena Izquierda
        // Punto de inicio en la circunferencia (simétrico)
        float x_i_antena_izquierda = posicionCentroideX - radio * (float) Math.sin(anguloBase);
        float y_i_antena_izquierda = posicionCentroideY - radio * (float) Math.cos(anguloBase);
        // Punto final proyectado (simétrico)
        float x_s_antena_izquierda = x_i_antena_izquierda - longitudAntena * (float) Math.sin(anguloInclinacion);
        float y_s_antena_izquierda = y_i_antena_izquierda - longitudAntena * (float) Math.cos(anguloInclinacion);

        canvas.drawLine(x_i_antena_izquierda, y_i_antena_izquierda, x_s_antena_izquierda, y_s_antena_izquierda, pincel);

        // Dibujar círculos sólidos en las puntas de las antenas (mismo diámetro que la
        // línea)
        pincel.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x_s_antena_derecha, y_s_antena_derecha, radioBolitaAntena, pincel);
        canvas.drawCircle(x_s_antena_izquierda, y_s_antena_izquierda, radioBolitaAntena, pincel);

        // regresa la rotación
        canvas.restore();
    }
}
