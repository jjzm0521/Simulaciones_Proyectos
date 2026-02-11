package com.curso_simulaciones.micuadragesimasegundaapp.utilidades;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;

public class Boton extends ImageView {

    private Bitmap imagen;
    Bitmap imagen_escalada = null;
    private float tamamano_letra;
    private String cadena = "";
    private boolean recortar = false;

    public Boton(Context context) {

        super(context);

    }

    public void setText(String cadena) {

        this.cadena = cadena;

    }

    public String getText() {
        return cadena;
    }

    public void setImagen(int imagen_importada) {
        setImagen(imagen_importada, false);
    }

    public void setImagen(int imagen_importada, boolean recortar) {
        this.recortar = recortar;
        this.imagen = BitmapFactory.decodeResource(getResources(), imagen_importada);
        this.imagen_escalada = null;
        invalidate();
    }

    protected void onDraw(Canvas canvas) {

        Paint pincel = new Paint();
        pincel.setAntiAlias(true);
        pincel.setLinearText(true);

        float ancho = getWidth();
        float alto = getHeight();
        float escala = 1;
        if (ancho > alto) {
            escala = alto;
        } else {
            escala = ancho;
        }

        if (imagen != null) {

            if (imagen_escalada == null) {
                if (recortar) {
                    // Recortar el centro de la imagen (zoom)
                    int minDim = Math.min(imagen.getWidth(), imagen.getHeight());
                    int cropSize = (int) (minDim / 1.2); // Usar factor menor para menos zoom (área más grande)
                    Bitmap cropped = cropBitmapCenter(imagen, cropSize, cropSize);
                    imagen_escalada = escalarImagen(cropped, (int) (0.9f * escala), (int) (0.9f * escala));
                } else {
                    imagen_escalada = escalarImagen(imagen, (int) (0.9f * escala), (int) (0.9f * escala));
                }
            }

            if (imagen_escalada == null)
                return;

            int ex = (getWidth() - imagen_escalada.getWidth()) / 2;
            int ey = (getHeight() - imagen_escalada.getHeight()) / 2;

            tamamano_letra = 0.08f * getWidth();

            canvas.drawBitmap(imagen_escalada, ex, ey, null);

            pincel.setTextSize(tamamano_letra);
            float anchoCadenaUnidades = pincel.measureText(cadena);
            float posicion_x_letra = (getWidth() - anchoCadenaUnidades) / 2;
            float posicion_y_letra = ey + imagen_escalada.getHeight() + tamamano_letra;

            pincel.setColor(Color.BLACK);
            canvas.drawText(cadena, posicion_x_letra, posicion_y_letra, pincel);

        } else {
            // Modo solo texto (setParametros)
            tamamano_letra = 0.5f * escala; // Texto más grande para el botón sin imagen
            pincel.setTextSize(tamamano_letra);
            pincel.setColor(Color.BLACK);

            float anchoCadena = pincel.measureText(cadena);
            float x = (getWidth() - anchoCadena) / 2;
            float y = (getHeight() / 2) + (tamamano_letra / 3); // Ajuste vertical aproximado

            canvas.drawText(cadena, x, y, pincel);
        }

    }

    /*
     * Se escala con Matrix para no dañar la resolución
     */

    public Bitmap escalarImagen(Bitmap bitmap, int newWidth, int newHeight) {
        if (newWidth <= 0 || newHeight <= 0) {
            return null;
        }
        Bitmap.Config config = bitmap.getConfig();
        if (config == null) {
            config = Bitmap.Config.ARGB_8888;
        }
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, config);

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

    /**
     * Recorta el centro de un bitmap y lo hace circular
     */
    public Bitmap cropBitmapCenter(Bitmap bitmap, int width, int height) {
        int sourceWidth = bitmap.getWidth();
        int sourceHeight = bitmap.getHeight();

        // Calcular coordenadas de inicio para el recorte centrado
        int x = (sourceWidth - width) / 2;
        int y = (sourceHeight - height) / 2;

        // Asegurar que no nos salimos de los límites
        if (x < 0)
            x = 0;
        if (y < 0)
            y = 0;
        if (width > sourceWidth)
            width = sourceWidth;
        if (height > sourceHeight)
            height = sourceHeight;

        Bitmap squaredBitmap = Bitmap.createBitmap(bitmap, x, y, width, height);

        // Crear bitmap de salida circular
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // Dibujar círculo
        canvas.drawCircle(width / 2, height / 2, width / 2, paint);

        // Modificar modo de transferencia
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(squaredBitmap, rect, rect, paint);

        return output;
    }

}
