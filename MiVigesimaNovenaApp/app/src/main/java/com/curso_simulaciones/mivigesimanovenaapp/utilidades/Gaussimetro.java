package com.curso_simulaciones.mivigesimanovenaapp.utilidades;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Clase Gaussimetro
 * 
 * Esta clase implementa un instrumento virtual (gauge) tipo velocímetro
 * para visualizar mediciones del sensor de campo magnético (magnetómetro).
 * 
 * Características:
 * - Arco de 270° con gradiente de colores (verde → amarillo → rojo)
 * - Aguja animada que se mueve suavemente hacia el valor medido
 * - Marcas de escala con números cada 2 divisiones
 * - Muestra valor numérico y unidades (µT - microteslas)
 * - Soporta rangos personalizables (positivos y negativos)
 * 
 * El diseño simula un instrumento analógico profesional con
 * estética moderna (fondo oscuro, gradientes, sombras).
 */
public class Gaussimetro extends View {

    // Valor actual de la medición
    private float medida = 0.0f;

    // Rango de medición
    private float min = 0f;
    private float max = 100f;

    // Unidad de medida (microteslas)
    private String unidad = "µT";

    // ========== PINCELES (Paints) ==========

    // Pincel para el arco con gradiente
    private Paint paintArco;

    // Pincel para el fondo circular oscuro
    private Paint paintFondo;

    // Pincel para mostrar el valor numérico
    private Paint paintTextoValor;

    // Pincel para mostrar la unidad
    private Paint paintTextoUnidad;

    // Pincel para la aguja indicadora
    private Paint paintAguja;

    // Pincel para el pivote central
    private Paint paintPivote;

    // Pincel para las marcas de escala
    private Paint paintMarcas;

    // Rectángulo que define el área del arco
    private RectF rectFArco;

    // ========== DIMENSIONES ==========

    // Centro y radio del gauge
    private float centerX, centerY, radio;

    // ========== ANIMACIÓN ==========

    // Ángulo actual de la aguja (para animación suave)
    private float anguloActual = 135f; // Ángulo inicial (posición mínima)

    /**
     * Constructor de Gaussimetro
     * 
     * @param context Contexto de la aplicación
     */
    public Gaussimetro(Context context) {
        super(context);
        init();
    }

    /**
     * Inicializa todos los pinceles y configuraciones
     */
    private void init() {
        // Usar renderizado por software para compatibilidad con gradientes
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        // Configurar pincel del arco
        paintArco = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintArco.setStyle(Paint.Style.STROKE);
        paintArco.setStrokeCap(Paint.Cap.ROUND); // Extremos redondeados

        // Configurar pincel del fondo
        paintFondo = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintFondo.setStyle(Paint.Style.FILL);
        paintFondo.setColor(Color.parseColor("#121212")); // Fondo oscuro elegante

        // Configurar pincel para el valor numérico
        paintTextoValor = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTextoValor.setColor(Color.WHITE);
        paintTextoValor.setTextAlign(Paint.Align.CENTER);
        paintTextoValor.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        // Configurar pincel para la unidad
        paintTextoUnidad = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTextoUnidad.setColor(Color.LTGRAY);
        paintTextoUnidad.setTextAlign(Paint.Align.CENTER);

        // Configurar pincel para la aguja
        paintAguja = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintAguja.setColor(Color.RED);
        paintAguja.setStyle(Paint.Style.FILL);
        paintAguja.setShadowLayer(4f, 2f, 2f, Color.BLACK); // Sombra para efecto 3D

        // Configurar pincel para el pivote central
        paintPivote = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintPivote.setColor(Color.DKGRAY);

        // Configurar pincel para las marcas de escala
        paintMarcas = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintMarcas.setColor(Color.WHITE);
        paintMarcas.setStrokeWidth(2f);

        rectFArco = new RectF();
    }

    /**
     * Se llama cuando cambia el tamaño del View
     * Recalcula dimensiones y configura el gradiente del arco
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Calcular centro y radio
        centerX = w / 2f;
        centerY = h / 2f;
        radio = Math.min(w, h) / 2f * 0.9f; // 90% del tamaño disponible

        // Grosor del arco proporcional al radio
        float strokeWidth = radio * 0.15f;
        paintArco.setStrokeWidth(strokeWidth);

        // Definir rectángulo para el arco
        rectFArco.set(centerX - radio + strokeWidth / 2,
                centerY - radio + strokeWidth / 2,
                centerX + radio - strokeWidth / 2,
                centerY + radio - strokeWidth / 2);

        // Crear gradiente de colores: verde → amarillo → rojo
        int[] colors = { Color.GREEN, Color.YELLOW, Color.RED };
        float[] positions = { 0.0f, 0.5f, 1.0f };
        SweepGradient shader = new SweepGradient(centerX, centerY, colors, positions);

        // Rotar el gradiente para que comience en la posición correcta
        Matrix gradientMatrix = new Matrix();
        gradientMatrix.preRotate(135f, centerX, centerY);
        shader.setLocalMatrix(gradientMatrix);

        paintArco.setShader(shader);
    }

    /**
     * Método onDraw - responsable de dibujar el gauge completo
     * 
     * Orden de dibujo:
     * 1. Fondo circular oscuro
     * 2. Arco de fondo (gris oscuro)
     * 3. Arco con gradiente de colores
     * 4. Marcas de escala con números
     * 5. Texto con valor numérico
     * 6. Texto con unidad
     * 7. Aguja indicadora
     * 8. Pivote central
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 1. Dibujar fondo circular
        canvas.drawCircle(centerX, centerY, radio, paintFondo);

        // 2. Dibujar arco de fondo (gris oscuro)
        paintArco.setShader(null);
        paintArco.setColor(Color.DKGRAY);
        canvas.drawArc(rectFArco, 135, 270, false, paintArco);

        // 3. Dibujar arco con gradiente de colores
        // Re-aplicar shader para el arco coloreado
        int[] colors = { Color.GREEN, Color.YELLOW, Color.RED };
        float[] positions = { 0.0f, 0.5f, 1.0f };
        SweepGradient shader = new SweepGradient(centerX, centerY, colors, positions);
        Matrix gradientMatrix = new Matrix();
        gradientMatrix.preRotate(135f, centerX, centerY);
        shader.setLocalMatrix(gradientMatrix);
        paintArco.setShader(shader);

        canvas.drawArc(rectFArco, 135, 270, false, paintArco);

        // 4. Dibujar marcas de escala
        drawTicks(canvas);

        // 5. Dibujar valor numérico
        paintTextoValor.setTextSize(radio * 0.3f);
        // Posicionado abajo para evitar solapamiento con la aguja
        canvas.drawText(String.format("%.1f", medida), centerX, centerY + radio * 0.4f, paintTextoValor);

        // 6. Dibujar unidad
        paintTextoUnidad.setTextSize(radio * 0.1f);
        canvas.drawText(unidad, centerX, centerY + radio * 0.55f, paintTextoUnidad);

        // 7. Dibujar aguja indicadora
        drawNeedle(canvas);

        // 8. Dibujar pivote central
        canvas.drawCircle(centerX, centerY, radio * 0.05f, paintPivote);
    }

    /**
     * Dibuja las marcas de escala y los números
     * Se dibujan 10 divisiones principales con números en posiciones pares
     */
    private void drawTicks(Canvas canvas) {
        float r1 = radio * 0.85f; // Radio interno de las marcas
        float r2 = radio * 0.95f; // Radio externo de las marcas
        float rText = radio * 0.75f; // Radio para los números

        // Pincel para los números de la escala
        Paint paintNumerosEscala = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintNumerosEscala.setColor(Color.LTGRAY);
        paintNumerosEscala.setTextSize(radio * 0.08f);
        paintNumerosEscala.setTextAlign(Paint.Align.CENTER);

        // Dibujar 11 marcas (0 a 10)
        for (int i = 0; i <= 10; i++) {
            // Calcular valor correspondiente a esta marca
            float value = min + (max - min) * i / 10f;
            float angle = mapValueToAngle(value);

            // Calcular posiciones de la línea de marca
            float x1 = (float) (centerX + r1 * Math.cos(Math.toRadians(angle)));
            float y1 = (float) (centerY + r1 * Math.sin(Math.toRadians(angle)));
            float x2 = (float) (centerX + r2 * Math.cos(Math.toRadians(angle)));
            float y2 = (float) (centerY + r2 * Math.sin(Math.toRadians(angle)));

            // Dibujar línea de marca
            canvas.drawLine(x1, y1, x2, y2, paintMarcas);

            // Dibujar números solo en posiciones pares (0, 2, 4, 6, 8, 10)
            if (i % 2 == 0) {
                float xText = (float) (centerX + rText * Math.cos(Math.toRadians(angle)));
                float yText = (float) (centerY + rText * Math.sin(Math.toRadians(angle)));
                // Ajustar posición vertical del texto
                yText += paintNumerosEscala.getTextSize() / 3;
                canvas.drawText(String.format("%.0f", value), xText, yText, paintNumerosEscala);
            }
        }
    }

    /**
     * Dibuja la aguja indicadora
     * La aguja apunta desde el centro hacia el ángulo actual
     */
    private void drawNeedle(Canvas canvas) {
        float angle = anguloActual;
        float needleLen = radio * 0.75f; // Longitud de la aguja

        // Calcular punto final de la aguja
        float x = (float) (centerX + needleLen * Math.cos(Math.toRadians(angle)));
        float y = (float) (centerY + needleLen * Math.sin(Math.toRadians(angle)));

        // Dibujar línea de la aguja
        paintAguja.setStrokeWidth(8f);
        canvas.drawLine(centerX, centerY, x, y, paintAguja);
    }

    /**
     * Mapea un valor de medición a un ángulo en el arco
     * 
     * El arco va de 135° (mínimo) a 405° (máximo), cubriendo 270°
     * 
     * @param value Valor a mapear
     * @return Ángulo correspondiente en grados
     */
    private float mapValueToAngle(float value) {
        // Limitar valor al rango válido
        float clampedValue = Math.max(min, Math.min(max, value));

        // Normalizar a rango 0.0 - 1.0
        float normalized = (clampedValue - min) / (max - min);

        // Mapear a ángulo: 135° a 405° (barrido de 270°)
        return 135f + (normalized * 270f);
    }

    /**
     * Establece el valor de medición con animación suave
     * 
     * Usa ValueAnimator con interpolador DecelerateInterpolator
     * para un movimiento natural de la aguja
     * 
     * @param valor Nuevo valor de medición
     */
    public void setMedida(float valor) {
        float targetAngle = mapValueToAngle(valor);

        // Crear animación de la aguja
        ValueAnimator anim = ValueAnimator.ofFloat(anguloActual, targetAngle);
        anim.setDuration(300); // Duración de 300ms
        anim.setInterpolator(new DecelerateInterpolator()); // Desaceleración natural
        anim.addUpdateListener(animation -> {
            anguloActual = (float) animation.getAnimatedValue();
            invalidate(); // Redibujar con cada frame
        });
        anim.start();

        this.medida = valor;
    }

    /**
     * Modifica el rango de medición del gauge
     * 
     * @param min Valor mínimo de la escala
     * @param max Valor máximo de la escala
     */
    public void setRango(float min, float max) {
        this.min = min;
        this.max = max;
        // Redibujar para actualizar las marcas de escala
        invalidate();
        // Actualizar posición de la aguja en el nuevo rango
        setMedida(this.medida);
    }

    /**
     * Establece la unidad de medida a mostrar
     * 
     * @param unidad Texto de la unidad (ej: "µT", "lx", "m/s²")
     */
    public void setUnidad(String unidad) {
        this.unidad = unidad;
        invalidate();
    }
}
