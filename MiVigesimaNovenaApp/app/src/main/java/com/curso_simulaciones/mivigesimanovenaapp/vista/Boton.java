package com.curso_simulaciones.mivigesimanovenaapp.vista;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.widget.ImageView;

public class Boton extends ImageView {

    private Bitmap imagen;
    Bitmap imagen_escalada = null;
    private float tamamano_letra;
    private String cadena = "";

    public Boton(Context context) {

        super(context);

    }

    public void setText(String cadena) {

        this.cadena = cadena;

    }

    public String getText() {

        // this.cadena = cadena;

        return cadena;

    }

    public void setImagen(int imagen_importada) {

        imagen = BitmapFactory.decodeResource(getResources(), imagen_importada);

    }

    protected void onDraw(Canvas canvas) {

        Paint pincel = new Paint();
        pincel.setAntiAlias(true);
        pincel.setLinearText(true);

        float ancho = getWidth();
        float alto = getHeight();
        float radio = Math.min(ancho, alto) * 0.4f; // Radio del círculo

        // Determinar color basado en el texto (Bx, By, Bz -> Verde; B -> Naranja)
        if (cadena.equals("B")) {
            pincel.setColor(Color.parseColor("#FFA500")); // Naranja
        } else {
            pincel.setColor(Color.GREEN); // Verde brillante
        }
        
        // Dibujar círculo
        pincel.setStyle(Paint.Style.FILL);
        canvas.drawCircle(ancho / 2, alto / 2, radio, pincel);
        
        // Borde opcional para que se vea mejor en fondo blanco
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setColor(Color.BLACK);
        pincel.setStrokeWidth(2);
        canvas.drawCircle(ancho / 2, alto / 2, radio, pincel);

        // Dibujar texto
        pincel.setStyle(Paint.Style.FILL);
        pincel.setColor(Color.BLACK);
        tamamano_letra = 0.6f * radio; // Letra más grande (antes 0.4f)
        pincel.setTextSize(tamamano_letra);
        pincel.setTextAlign(Paint.Align.CENTER);
        
        // Centrar verticalmente
        float textHeight = pincel.descent() - pincel.ascent();
        float textOffset = (textHeight / 2) - pincel.descent();

        canvas.drawText(cadena, ancho / 2, alto / 2 + textOffset, pincel);

        invalidate();

    }

    /*
     * Se escala con Matrix para no dañar la resolución
     */

    public Bitmap escalarImagen(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, bitmap.getConfig());

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, 0, 0);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return scaledBitmap;

    }

}
