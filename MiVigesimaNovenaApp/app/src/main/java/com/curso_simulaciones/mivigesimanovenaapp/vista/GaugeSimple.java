package com.curso_simulaciones.mivigesimanovenaapp.vista;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class GaugeSimple extends View {

    private float largo;
    private float minimo = 0;
    private float maximo = 100f;
    private float medida = 0.0f;// tomar como medida inicial
    private String unidades = "UNIDADES";

    // color de los sectores
    private int colorPrimerTercio = Color.rgb(200, 200, 0);
    private int colorSegundoTercio = Color.rgb(0, 180, 0);
    private int colorTercerTercio = Color.RED;

    // color del marco
    private int colorFondoTacometro = Color.rgb(240, 240, 240);
    private int colorBordeTacometro = Color.BLACK;

    // color franja dinámica
    private int colorFranjaDinamica = Color.RED;

    private int angPrimertercio = 100;
    private int angSegundoTercio = 100;
    private int angTercerTercio = 40;

    private int colorLineas = Color.BLACK;
    private int colorNumeros = Color.BLACK;

    private int colorNumerosDesplieggue = Color.BLACK;

    private int numeroDivisiones = 25;
    private int separacionDivisionesGrandes = 5;

    /**
     * Constructor de GaugeSimple
     */
    public GaugeSimple(Context context) {

        super(context);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        }
    }

    /**
     * Modifica el rango de medicion
     * desde minimo hasta maximo
     *
     * @param minimo
     * @param maximo
     */
    public void setRango(float minimo, float maximo) {

        this.minimo = minimo;
        this.maximo = maximo;

    }

    public void setSeparacionesDivisionesGrandes(int separacionDivisionesGrandes) {

        this.separacionDivisionesGrandes = separacionDivisionesGrandes;

    }

    private void setNumeroDivisiones(int numeroDivisiones) {

        this.numeroDivisiones = numeroDivisiones;

    }

    /**
     * Modifica el valor medido
     *
     * @param medida
     */
    public void setMedida(float medida) {

        this.medida = medida;

    }

    /**
     * Regresa el valor medido
     *
     * @return medida
     */
    public float getMedida() {

        return medida;
    }

    /**
     * Modifica las unidades del instrumento virtual
     *
     * @param unidades
     */
    public void setUnidades(String unidades) {

        this.unidades = unidades;

    }

    /**
     * Modifica el color del borde del marco
     * 
     * @param colorBordeMarco
     */

    public void setColorBordeTacometro(int colorBordeMarco) {

        this.colorBordeTacometro = colorBordeMarco;

    }

    /**
     * Modifica los colores de los sectores circulares
     *
     * @param colorPrimerTercio
     * @param colorSegundoTercio
     * @param colorTercerTercio
     */
    public void setColorSectores(int colorPrimerTercio, int colorSegundoTercio, int colorTercerTercio) {

        this.colorPrimerTercio = colorPrimerTercio;
        this.colorSegundoTercio = colorSegundoTercio;
        this.colorTercerTercio = colorTercerTercio;

    }

    /**
     * Modifica los angulos de los sectores circulares
     * Deben sumar 250 grados
     *
     * @param angPrimerTercio
     * @param angSegundoTercio
     * @param angTercerTercio
     */
    public void setAngulosSectores(int angPrimerTercio, int angSegundoTercio, int angTercerTercio) {
        this.angPrimertercio = angPrimerTercio;
        this.angSegundoTercio = angSegundoTercio;
        this.angTercerTercio = angTercerTercio;

    }

    /**
     * Modifica el color de fondo del tacometro
     *
     * @param colorFondoTacometro
     */
    public void setColorFondoTacometro(int colorFondoTacometro) {

        this.colorFondoTacometro = colorFondoTacometro;

    }

    /**
     * Modifica el color de las lineas del tacometro
     *
     * @param color_lineas
     */
    public void setColorLineasTacometro(int color_lineas) {

        this.colorLineas = color_lineas;

    }

    public void setColorNumeros(int colorNumeros) {

        this.colorNumeros = colorNumeros;

    }

    /**
     * Modifica el color del numero que se despliega
     *
     * @param colorNumerosDesplieggue
     */

    public void setColorNumeroDespliegue(int colorNumerosDesplieggue) {

        this.colorNumerosDesplieggue = colorNumerosDesplieggue;

    }

    /**
     * Modifica el color de la franja dinámica
     * 
     * @param colorFranjaDinamica
     */
    public void setColorFranjaDinámica(int colorFranjaDinamica) {

        this.colorFranjaDinamica = colorFranjaDinamica;

    }

    /**
     * @param canvas
     */

    // método para dibujar
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        float ancho = this.getWidth();
        float alto = this.getHeight();

        float largo;
        if (ancho > alto) {
            largo = 0.8f * alto;
        } else {
            largo = 0.8f * ancho;
        }

        canvas.translate(0.5f * ancho, 0.5f * alto);

        Paint pincel = new Paint();
        pincel.setAntiAlias(true);
        pincel.setLinearText(true);
        pincel.setFilterBitmap(true);
        pincel.setDither(true);

        // 1. DIBUJAR EL DIAL (Círculo superor)
        float radioDial = 0.35f * largo;
        float centroDialY = -0.15f * largo;

        // Borde del dial
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeWidth(0.015f * largo);
        pincel.setColor(Color.BLACK); 
        canvas.drawCircle(0, centroDialY, radioDial, pincel);

        // Fondo del dial (blanco o transparente según diseño)
        pincel.setStyle(Paint.Style.FILL);
        pincel.setColor(Color.WHITE);
        canvas.drawCircle(0, centroDialY, radioDial - 0.01f * largo, pincel);

        // 2. DIBUJAR LA AGUJA (ESTILO VECTOR)
        // Flecha: Linea recta + Punta triangular
        // Mapeamos medida a rotación.
        float rotacion = (medida / (maximo - minimo)) * 180f; 
        float anguloFinal = -45 + rotacion; 

        canvas.save();
        canvas.translate(0, centroDialY);
        canvas.rotate(anguloFinal);
        
        // Estilo para el cuerpo del vector (Line)
        pincel.setColor(Color.BLACK);
        pincel.setStyle(Paint.Style.FILL_AND_STROKE);
        pincel.setStrokeWidth(0.015f * largo); // Linea fina pero visible
        
        float longitudAguja = radioDial * 0.9f;
        canvas.drawLine(0, 0, 0, -longitudAguja, pincel); // Cuerpo del vector
        
        // Estilo para la punta del vector (Triangulo)
        pincel.setStyle(Paint.Style.FILL);
        android.graphics.Path pathPunta = new android.graphics.Path();
        float tamanoPunta = 0.05f * largo; // Tamaño de la cabeza de flecha
        
        // La punta está en (0, -longitudAguja)
        // Dibujamos un triángulo centrado en ese punto
        pathPunta.moveTo(0, -longitudAguja - tamanoPunta); // Punta extrema
        pathPunta.lineTo(tamanoPunta/2, -longitudAguja + tamanoPunta/2); // Base derecha
        pathPunta.lineTo(-tamanoPunta/2, -longitudAguja + tamanoPunta/2); // Base izquierda
        pathPunta.close();
        
        canvas.drawPath(pathPunta, pincel);
        
        // Centro de la aguja
        canvas.drawCircle(0, 0, 0.03f * largo, pincel);
        
        canvas.restore();

        // 3. PANTALLA DIGITAL "00.00" (DENTRO DEL GAUGE)
        // El usuario pide que esté "dentro del gauge" (dentro del círculo/dial).
        // Lo ponemos en la mitad inferior del dial.
        
        float anchoPantalla = 0.3f * largo; // Más pequeño
        float altoPantalla = 0.10f * largo;
        // Posición Y: Un poco más abajo del centro del dial
        float topPantalla = centroDialY + 0.1f * largo; 
        
        RectF rectPantalla = new RectF(-anchoPantalla/2, topPantalla, anchoPantalla/2, topPantalla + altoPantalla);
        
        // Fondo / Borde Pantalla
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setColor(Color.RED);
        pincel.setStrokeWidth(0.01f * largo);
        canvas.drawRect(rectPantalla, pincel);
        
        // Texto Digital
        pincel.setStyle(Paint.Style.FILL);
        pincel.setColor(Color.BLACK);
        pincel.setTextSize(0.08f * largo); // Texto ajustado
        pincel.setTextAlign(Paint.Align.CENTER); // Alineación horizontal central
        
        // Cálculo correcto para centrado VERTICAL de texto
        Paint.FontMetrics fontMetrics = pincel.getFontMetrics();
        // La distancia desde el centro vertical hasta la línea base de la fuente
        // ascent es negativo (arriba), descent es positivo (abajo)
        float dy = (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent;
        float yDatos = rectPantalla.centerY() + dy;
        
        String textoMedida = String.format("%05.2f", Math.abs(medida));
        canvas.drawText(textoMedida, 0, yDatos, pincel);

        // 4. ETIQUETA "GAUGE PERSONAL" (Debajo del gauge)
        float yEtiqueta = centroDialY + radioDial + 0.15f * largo;
        pincel.setColor(Color.BLACK);
        pincel.setTextSize(0.06f * largo);
        canvas.drawText("GAUGE PERSONAL", 0, yEtiqueta, pincel);


        canvas.restore();
        invalidate();

    }// fin onDraw

}
