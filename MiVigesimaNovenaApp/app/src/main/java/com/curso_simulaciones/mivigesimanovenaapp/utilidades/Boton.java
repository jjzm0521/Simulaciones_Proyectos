package com.curso_simulaciones.mivigesimanovenaapp.utilidades;

import com.curso_simulaciones.mivigesimanovenaapp.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

/**
 * Clase Boton
 * 
 * Esta clase extiende View para crear botones personalizados.
 * Permite mostrar imágenes escaladas o botones circulares con texto.
 * 
 * Características:
 * - Soporta imágenes desde recursos drawable
 * - Dibuja botones circulares programáticamente con texto centrado
 * - Escala automáticamente según el tamaño del contenedor
 */
public class Boton extends View {

    // Imagen original y escalada para el botón
    private Bitmap imagen, imagen_escalada;

    // Texto que se muestra debajo del botón
    private String cadena = "";

    /**
     * Constructor de Boton
     * 
     * @param context Contexto de la aplicación
     */
    public Boton(Context context) {
        super(context);
    }

    /**
     * Establece el texto externo que aparece debajo del botón
     * 
     * @param cadena Texto a mostrar
     */
    public void setText(String cadena) {
        this .cadena = cadena;
    }

    /**
     * Obtiene el texto del botón
     * 
     * @return Texto actual del botón
     */
    public String getText() {
        return cadena;
    }

    /**
     * Establece la imagen del botón desde un recurso drawable
     * 
     * @param imagen_importada ID del recurso drawable
     */
    public void setImagen(int imagen_importada) {
        imagen = BitmapFactory.decodeResource(getResources(), imagen_importada);
    }

    // Color de fondo para botones programáticos
    private int colorFondo = Color.GRAY;

    // Texto que aparece dentro del botón circular
    private String textoBoton = "";

    /**
     * Configura los parámetros para un botón circular programático
     * 
     * @param texto Texto a mostrar dentro del círculo (ej: "Bx", "By")
     * @param color Color de fondo del círculo
     */
    public void setParametros(String texto, int color) {
        this.textoBoton = texto;
        this.colorFondo = color;
        this.imagen = null; // Cambiar a modo programático
        invalidate();
    }

    /**
     * Método onDraw - responsable de dibujar el botón
     * 
     * Este método maneja dos modos de dibujo:
     * 1. Modo imagen: Dibuja una imagen escalada desde recursos
     * 2. Modo programático: Dibuja un círculo coloreado con texto centrado
     */
    protected void onDraw(Canvas canvas) {

        // Crear pincel con anti-aliasing para suavizado
        Paint pincel = new Paint();
        pincel.setAntiAlias(true);
        pincel.setLinearText(true);

        // Calcular dimensiones para escalar proporcionalmente
        float ancho = getWidth();
        float alto = getHeight();
        float escala;

        // Usar la dimensión menor para mantener proporciones
        if (ancho > alto) {
            escala = alto;
        } else {
            escala = ancho;
        }

        // Radio del círculo (80% de la escala)
        float radio = (0.8f * escala) / 2;
        float cx = getWidth() / 2f; // Centro X
        float cy = getHeight() / 2f; // Centro Y

        if (imagen != null) {
            // Modo imagen: escalar y dibujar bitmap
            imagen_escalada = escalarImagen(imagen, (int) (0.8f * escala), (int) (0.8f * escala));
            int ex = (getWidth() - imagen_escalada.getWidth()) / 2;
            int ey = (getHeight() - imagen_escalada.getHeight()) / 2;
            canvas.drawBitmap(imagen_escalada, ex, ey, null);
        } else {
            // Modo programático: dibujar círculo con color
            pincel.setColor(colorFondo);
            pincel.setStyle(Paint.Style.FILL);
            canvas.drawCircle(cx, cy, radio, pincel);

            // Dibujar borde del círculo
            pincel.setColor(Color.BLACK);
            pincel.setStyle(Paint.Style.STROKE);
            pincel.setStrokeWidth(2f);
            canvas.drawCircle(cx, cy, radio, pincel);

            // Dibujar texto dentro del círculo (Bx, By, Bz, B)
            if (!textoBoton.isEmpty()) {
                pincel.setStyle(Paint.Style.FILL);
                pincel.setColor(Color.BLACK);
                pincel.setTextSize(radio); // Tamaño proporcional al radio
                pincel.setTextAlign(Paint.Align.CENTER);

                // Centrar verticalmente usando métricas de fuente
                Paint.FontMetrics metrics = pincel.getFontMetrics();
                float dy = -(metrics.descent + metrics.ascent) / 2;

                canvas.drawText(textoBoton, cx, cy + dy, pincel);
            }
        }

        // Dibujar etiqueta externa (cadena) si está definida
        if (!cadena.isEmpty()) {
            float tamano_letra = 0.08f * getWidth();
            pincel.setTextSize(tamano_letra);
            pincel.setTextAlign(Paint.Align.CENTER);
            pincel.setColor(Color.BLACK);
            pincel.setStyle(Paint.Style.FILL);

            // Posicionar debajo del círculo
            float posicion_y_letra = cy + radio + tamano_letra * 1.5f;
            canvas.drawText(cadena, cx, posicion_y_letra, pincel);
        }

        invalidate(); // Redibujar continuamente

    }

    /**
     * Escala una imagen usando Matrix para preservar la calidad
     * 
     * Se usa Matrix en lugar de createScaledBitmap para
     * evitar pérdida de resolución en el escalado.
     * 
     * @param bitmap    Imagen original a escalar
     * @param newWidth  Nuevo ancho deseado
     * @param newHeight Nueva altura deseada
     * @return Bitmap escalado
     */
    public Bitmap escalarImagen(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, bitmap.getConfig());

        // Calcular factores de escala
        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();

        // Crear matriz de transformación
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, 0, 0);

        // Dibujar imagen escalada en el nuevo Canvas
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);

        // Usar filtros para mejor calidad
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return scaledBitmap;

    }

}
