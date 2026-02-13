package com.curso_simulaciones.micuadrigesimaquintaapp.utilidades;

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
 * Clase Gauge (Basada en Gaussimetro)
 * 
 * Implementa un instrumento virtual (gauge) tipo velocímetro.
 * 
 * Características:
 * - Arco de 270° con gradiente de colores.
 * - Aguja animada que se mueve suavemente hacia el valor medido.
 * - Marcas de escala con números.
 * - Muestra valor numérico y unidades.
 */
public class Gauge extends View {

    // Valor actual de la medición
    private float medida = 0.0f;

    // Rango de medición
    private float min = 0f;
    private float max = 100f;

    // Unidad de medida
    private String unidad = "";

    // ========== PINCELES (Paints) ==========

    private Paint paintArco;
    private Paint paintFondo;
    private Paint paintTextoValor;
    private Paint paintTextoUnidad;
    private Paint paintAguja;
    private Paint paintPivote;
    private Paint paintMarcas;

    private RectF rectFArco;

    // ========== DIMENSIONES ==========
    private float centerX, centerY, radio;

    // ========== ANIMACIÓN ==========
    // Ángulo actual de la aguja (para animación suave)
    private float anguloActual;

    // ========== CONFIGURACIÓN DE ÁNGULOS ==========
    private float angInicio = 135f;
    private float angBarrido = 270f;

    public Gauge(Context context) {
        super(context);
        init();
    }

    private void init() {
        // Inicializar ángulo actual al inicio del rango
        anguloActual = angInicio;

        // Usar renderizado por software para compatibilidad con gradientes
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        // Configurar pincel del arco
        paintArco = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintArco.setStyle(Paint.Style.STROKE);
        paintArco.setStrokeCap(Paint.Cap.ROUND);

        // Configurar pincel del fondo
        paintFondo = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintFondo.setStyle(Paint.Style.FILL);
        paintFondo.setColor(Color.parseColor("#121212")); // Fondo oscuro

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
        paintAguja.setShadowLayer(4f, 2f, 2f, Color.BLACK);

        // Configurar pincel para el pivote central
        paintPivote = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintPivote.setColor(Color.DKGRAY);

        // Configurar pincel para las marcas de escala
        paintMarcas = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintMarcas.setColor(Color.WHITE);
        paintMarcas.setStrokeWidth(2f);

        rectFArco = new RectF();

        // Ensure angle sync
        anguloActual = angInicio;
    }

    /* Método para personalizar ángulos */
    public void setAngulos(float inicio, float barrido) {
        this.angInicio = inicio;
        this.angBarrido = barrido;

        // Recalcular shader
        if (radio > 0) {
            updateShader();
        }

        // Forzar actualización de la posición de la aguja con los nuevos ángulos
        setMedida(this.medida);
    }

    private void updateShader() {
        // Ajustar gradiente para que cubra exactamente el ángulo de barrido
        int[] colors = { Color.GREEN, Color.YELLOW, Color.RED };

        // Mapear los colores al porcentaje del círculo que representa el barrido
        float ratio = angBarrido / 360f;
        float[] positions = { 0.0f, ratio / 2f, ratio };

        SweepGradient shader = new SweepGradient(centerX, centerY, colors, positions);

        // Rotar el gradiente para que comience en angInicio
        Matrix gradientMatrix = new Matrix();
        gradientMatrix.preRotate(angInicio, centerX, centerY);
        shader.setLocalMatrix(gradientMatrix);

        paintArco.setShader(shader);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Calcular centro y radio
        centerX = w / 2f;
        centerY = h / 2f;
        radio = Math.min(w, h) / 2f * 0.9f;

        // Grosor del arco proporcional al radio
        float strokeWidth = radio * 0.15f;
        paintArco.setStrokeWidth(strokeWidth);

        // Definir rectángulo para el arco
        rectFArco.set(centerX - radio + strokeWidth / 2,
                centerY - radio + strokeWidth / 2,
                centerX + radio - strokeWidth / 2,
                centerY + radio - strokeWidth / 2);

        updateShader();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 1. Dibujar fondo circular
        canvas.drawCircle(centerX, centerY, radio, paintFondo);

        // 2. Dibujar arco de fondo (gris oscuro)
        paintArco.setShader(null);
        paintArco.setColor(Color.DKGRAY);
        canvas.drawArc(rectFArco, angInicio, angBarrido, false, paintArco);

        // 3. Dibujar arco con gradiente de colores
        // Re-asignar shader (se limpia arriba al dibujar gris)
        updateShader();

        canvas.drawArc(rectFArco, angInicio, angBarrido, false, paintArco);

        // 4. Dibujar marcas de escala
        drawTicks(canvas);

        // 5. Dibujar valor numérico
        paintTextoValor.setTextSize(radio * 0.3f);
        canvas.drawText(String.format(java.util.Locale.US, "%.1f", medida), centerX, centerY + radio * 0.4f,
                paintTextoValor);

        // 6. Dibujar unidad
        paintTextoUnidad.setTextSize(radio * 0.1f);
        canvas.drawText(unidad, centerX, centerY + radio * 0.55f, paintTextoUnidad);

        // 7. Dibujar aguja indicadora
        drawNeedle(canvas);

        // 8. Dibujar pivote central
        canvas.drawCircle(centerX, centerY, radio * 0.05f, paintPivote);
    }

    private void drawTicks(Canvas canvas) {
        float r1 = radio * 0.85f;
        float r2 = radio * 0.95f;
        float rText = radio * 0.75f;

        Paint paintNumerosEscala = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintNumerosEscala.setColor(Color.LTGRAY);
        paintNumerosEscala.setTextSize(radio * 0.08f);
        paintNumerosEscala.setTextAlign(Paint.Align.CENTER);

        for (int i = 0; i <= 10; i++) {
            float value = min + (max - min) * i / 10f;
            float angle = mapValueToAngle(value);

            float x1 = (float) (centerX + r1 * Math.cos(Math.toRadians(angle)));
            float y1 = (float) (centerY + r1 * Math.sin(Math.toRadians(angle)));
            float x2 = (float) (centerX + r2 * Math.cos(Math.toRadians(angle)));
            float y2 = (float) (centerY + r2 * Math.sin(Math.toRadians(angle)));

            canvas.drawLine(x1, y1, x2, y2, paintMarcas);

            if (i % 2 == 0) {
                float xText = (float) (centerX + rText * Math.cos(Math.toRadians(angle)));
                float yText = (float) (centerY + rText * Math.sin(Math.toRadians(angle)));
                yText += paintNumerosEscala.getTextSize() / 3;
                canvas.drawText(String.format(java.util.Locale.US, "%.0f", value), xText, yText, paintNumerosEscala);
            }
        }
    }

    private void drawNeedle(Canvas canvas) {
        // Asegurar visualmente que la aguja nunca salga de los límites definidos
        float angle = Math.max(angInicio, Math.min(angInicio + angBarrido, anguloActual));
        float needleLen = radio * 0.75f;

        float x = (float) (centerX + needleLen * Math.cos(Math.toRadians(angle)));
        float y = (float) (centerY + needleLen * Math.sin(Math.toRadians(angle)));

        paintAguja.setStrokeWidth(Math.max(4f, radio * 0.02f));
        canvas.drawLine(centerX, centerY, x, y, paintAguja);
    }

    private float mapValueToAngle(float value) {
        float clampedValue = Math.max(min, Math.min(max, value));
        float normalized = (clampedValue - min) / (max - min);
        // De angInicio a angInicio + angBarrido
        return angInicio + (normalized * angBarrido);
    }

    public void setMedida(float valor) {
        float targetAngle = mapValueToAngle(valor);

        ValueAnimator anim = ValueAnimator.ofFloat(anguloActual, targetAngle);
        anim.setDuration(300);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                anguloActual = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        anim.start();

        this.medida = valor;
    }

    public void setRango(float min, float max) {
        this.min = min;
        this.max = max;
        invalidate();
        setMedida(this.medida); // Re-animar a la nueva posición relativa
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
        invalidate();
    }
}
