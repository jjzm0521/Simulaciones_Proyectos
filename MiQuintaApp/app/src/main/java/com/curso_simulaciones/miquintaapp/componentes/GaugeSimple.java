package com.curso_simulaciones.miquintaapp.componentes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.View;

public class GaugeSimple extends View {

    private float largo;
    private float minimo = -20;
    private float maximo = 40f;
    private float medida = 0.0f;
    private String unidades = "Gauss";

    // Colores por defecto (Basados en Figura 46)
    private int colorFondo = Color.BLACK;
    private int colorBorde = Color.RED;
    private int colorAguja = Color.RED;
    private int colorNumeros = Color.WHITE;
    private int colorUnidades = Color.YELLOW;

    // Colores de los sectores de la escala
    private int colorPrimerTercio = Color.BLUE;
    private int colorSegundoTercio = Color.YELLOW;
    private int colorTercerTercio = Color.GREEN;

    // Angulos de los sectores (Floats para mayor precision)
    private float angPrimertercio = 135;
    private float angSegundoTercio = 90;
    private float angTercerTercio = 45;

    // Angulo de inicio del gauge (0=Este, 90=Sur, 180=Oeste, 270=Norte)
    private float anguloInicio = 135;

    public GaugeSimple(Context context) {
        super(context);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    /**
     * Modifica el rango de medicion
     */
    public void setRango(float minimo, float maximo) {
        this.minimo = minimo;
        this.maximo = maximo;
        invalidate();
    }

    /**
     * Modifica el angulo de inicio
     * 
     * @param angulo Grados donde empieza la escala
     */
    public void setAnguloInicio(float angulo) {
        this.anguloInicio = angulo;
        invalidate();
    }

    /**
     * Modifica el valor medido
     */
    public void setMedida(float medida) {
        this.medida = medida;
        invalidate();
    }

    /**
     * Regresa el valor medido
     */
    public float getMedida() {
        return medida;
    }

    /**
     * Modifica las unidades
     */
    public void setUnidades(String unidades) {
        this.unidades = unidades;
        invalidate();
    }

    /**
     * Modifica los colores de los sectores de la escala
     */
    public void setColorSectores(int colorPrimerTercio, int colorSegundoTercio, int colorTercerTercio) {
        this.colorPrimerTercio = colorPrimerTercio;
        this.colorSegundoTercio = colorSegundoTercio;
        this.colorTercerTercio = colorTercerTercio;
        invalidate();
    }

    /**
     * Modifica los angulos de los sectores circulares
     */
    public void setAngulosSectores(float angPrimerTercio, float angSegundoTercio, float angTercerTercio) {
        this.angPrimertercio = angPrimerTercio;
        this.angSegundoTercio = angSegundoTercio;
        this.angTercerTercio = angTercerTercio;
        invalidate();
    }

    /**
     * Modifica el color de fondo
     */
    public void setColorFondo(int colorFondo) {
        this.colorFondo = colorFondo;
        invalidate();
    }

    /**
     * Modifica el color de la aguja
     */
    public void setColorAguja(int colorAguja) {
        this.colorAguja = colorAguja;
        invalidate();
    }

    /**
     * Modifica el color de los números de la escala
     */
    public void setColorNumeros(int colorNumeros) {
        this.colorNumeros = colorNumeros;
        invalidate();
    }

    /**
     * Modifica el color de las unidades y del recuadro digital
     */
    public void setColorUnidades(int colorUnidades) {
        this.colorUnidades = colorUnidades;
        invalidate();
    }

    /**
     * Modifica el color del borde exterior
     */
    public void setColorBorde(int colorBorde) {
        this.colorBorde = colorBorde;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Asegurar que la vista sea cuadrada o respete las dimensiones dadas
        int ancho = MeasureSpec.getSize(widthMeasureSpec);
        int alto = MeasureSpec.getSize(heightMeasureSpec);
        int dimension = Math.min(ancho, alto);
        setMeasuredDimension(dimension, dimension);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        float ancho = this.getWidth();
        float alto = this.getHeight();

        // Usar la menor dimensión para asegurar aspecto circular
        if (ancho > alto) {
            largo = 0.9f * alto;
        } else {
            largo = 0.9f * ancho;
        }

        canvas.translate(0.5f * ancho, 0.5f * alto);

        Paint pincel = new Paint();
        pincel.setAntiAlias(true);
        pincel.setFilterBitmap(true);
        pincel.setDither(true);

        float radio = largo * 0.5f;

        // 1. Dibujar Fondo Negro
        pincel.setColor(colorFondo);
        pincel.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0, 0, radio, pincel);

        // 2. Dibujar Borde Rojo Externo
        float bordeGrosor = 0.04f * largo;
        pincel.setColor(colorBorde);
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeWidth(bordeGrosor);
        canvas.drawCircle(0, 0, radio - (bordeGrosor / 2f), pincel);

        // 3. Dibujar Arcos de Colores (270 grados)
        // La circunferencia de colores debe estar más alejada del centro
        // y tener el mismo grosor que el borde rojo
        float radioEscalaExterior = radio - bordeGrosor - (0.05f * largo); // Separación del borde rojo
        float radioEscalaInterior = radioEscalaExterior - bordeGrosor; // Mismo grosor que borde rojo

        RectF rectEscalaExt = new RectF(-radioEscalaExterior, -radioEscalaExterior, radioEscalaExterior,
                radioEscalaExterior);

        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeWidth(bordeGrosor);

        // Angulo inicial: 90° (donde está -20)
        // Izquierda (180°): AZUL
        pincel.setColor(colorPrimerTercio);
        canvas.drawArc(rectEscalaExt, 90, 180, false, pincel);

        // Superior derecho (45°): AMARILLO
        pincel.setColor(colorSegundoTercio);
        canvas.drawArc(rectEscalaExt, 270, 45, false, pincel);

        // Superior derecho (45°): VERDE
        pincel.setColor(colorTercerTercio);
        canvas.drawArc(rectEscalaExt, 315, 45, false, pincel);

        // 4. Dibujar Números (dentro del arco de colores)
        pincel.setTextSize(0.08f * largo);
        pincel.setStyle(Paint.Style.FILL);

        // Radio para posicionar números (dentro del arco de colores, más alejados)
        float radioNumeros = radioEscalaInterior - (0.12f * largo);

        // Calcular números dinámicamente basados en el rango
        float[] angulos = { 90, 135, 180, 225, 270, 315, 0 }; // Posiciones cada 45°, empezando en 90°
        int numDivisiones = 7;
        float[] valores = new float[numDivisiones];

        // Calcular valores proporcionalmente al rango
        for (int i = 0; i < numDivisiones; i++) {
            valores[i] = minimo + ((maximo - minimo) / (float) (numDivisiones - 1)) * i;
        }

        for (int i = 0; i < angulos.length; i++) {
            float anguloRad = (float) Math.toRadians(angulos[i]);
            float valor = valores[i];

            // Color rojo para el valor mínimo, blanco para el resto
            if (i == 0) {
                pincel.setColor(colorBorde); // Rojo para el primer valor (mínimo)
            } else {
                pincel.setColor(colorNumeros); // Blanco para el resto
            }

            String textoNum = String.format("%.0f", valor);

            float xNum = (float) (radioNumeros * Math.cos(anguloRad));
            float yNum = (float) (radioNumeros * Math.sin(anguloRad));

            Rect bounds = new Rect();
            pincel.getTextBounds(textoNum, 0, textoNum.length(), bounds);
            canvas.drawText(textoNum, xNum - bounds.width() / 2f, yNum + bounds.height() / 2f, pincel);
        }

        // 5. Dibujar Barras Radiales Blancas (solo en la parte interna del arco de
        // colores)
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeWidth(0.005f * largo);

        float radioBarraExt = radioEscalaInterior;
        float radioBarraInt = radioEscalaInterior - (0.05f * largo);

        for (int i = 0; i < angulos.length; i++) {
            float angulo = angulos[i];
            float valor = valores[i];
            float anguloRad = (float) Math.toRadians(angulo);

            // Determinar color de la barra
            if (Math.abs(valor - 30) < 0.1f) {
                pincel.setColor(Color.YELLOW); // Barra a 30 es amarilla
            } else if (Math.abs(valor - 40) < 0.1f) {
                pincel.setColor(Color.GREEN); // Barra a 40 es verde
            } else {
                pincel.setColor(Color.WHITE); // Resto son blancas
            }

            float x1 = (float) (radioBarraInt * Math.cos(anguloRad));
            float y1 = (float) (radioBarraInt * Math.sin(anguloRad));
            float x2 = (float) (radioBarraExt * Math.cos(anguloRad));
            float y2 = (float) (radioBarraExt * Math.sin(anguloRad));

            canvas.drawLine(x1, y1, x2, y2, pincel);
        }

        // 6. Display Digital (más cerca del centro, esquinas redondeadas)
        float digitalCX = 0.15f * radio; // Más cercano al centro
        float digitalCY = 0.15f * radio;
        float digitalW = 0.45f * radio;
        float digitalH = 0.35f * radio;

        RectF rectDigital = new RectF(
                digitalCX,
                digitalCY,
                digitalCX + digitalW,
                digitalCY + digitalH);

        // Dibujar borde del display con esquinas redondeadas
        pincel.setColor(colorUnidades);
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeWidth(0.005f * largo);
        canvas.drawRoundRect(rectDigital, 0.05f * largo, 0.05f * largo, pincel);

        // Dibujar valor numérico
        pincel.setStyle(Paint.Style.FILL);
        pincel.setTextSize(0.08f * largo);
        String valStr = String.format("%.0f", medida);
        Rect boundsVal = new Rect();
        pincel.getTextBounds(valStr, 0, valStr.length(), boundsVal);

        float textX = rectDigital.centerX() - boundsVal.width() / 2f;
        float textY = rectDigital.top + digitalH * 0.50f;

        canvas.drawText(valStr, textX, textY, pincel);

        // Dibujar unidades (debajo del valor, dentro del rectángulo)
        pincel.setTextSize(0.035f * largo);
        Rect boundsUni = new Rect();
        pincel.getTextBounds(unidades, 0, unidades.length(), boundsUni);
        float unitX = rectDigital.centerX() - boundsUni.width() / 2f;
        float unitY = rectDigital.bottom - digitalH * 0.15f;

        canvas.drawText(unidades, unitX, unitY, pincel);

        // 7. Dibujar Aguja (más larga)
        float totalAngulo = 270; // 270 grados
        float startAngle = 90; // Empieza en 90° donde está -20
        float anguloAguja = startAngle + ((medida - minimo) / (maximo - minimo)) * totalAngulo;
        if (anguloAguja < startAngle)
            anguloAguja = startAngle;
        if (anguloAguja > startAngle + totalAngulo)
            anguloAguja = startAngle + totalAngulo;

        pincel.setColor(colorAguja);
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeWidth(0.01f * largo);

        // Aguja principal (más larga, llega entre el arco de colores y el borde rojo)
        float longitudAgujaPrincipal = radioEscalaExterior + (bordeGrosor / 2f);

        canvas.rotate(anguloAguja, 0, 0);
        canvas.drawLine(0, 0, longitudAgujaPrincipal, 0, pincel);

        // Aguja vestigio (en sentido opuesto, más corta)
        canvas.drawLine(0, 0, -(0.12f * largo), 0, pincel);
        canvas.rotate(-anguloAguja, 0, 0);

        // 8. Círculo rojo alrededor del centro (más grande que el círculo central)
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setColor(colorBorde);
        pincel.setStrokeWidth(0.008f * largo);
        canvas.drawCircle(0, 0, 0.08f * largo, pincel);

        // 9. Círculo central rojo (relleno)
        pincel.setStyle(Paint.Style.FILL);
        pincel.setColor(colorBorde);
        canvas.drawCircle(0, 0, 0.05f * largo, pincel);

        canvas.restore();
    }
}