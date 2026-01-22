package com.curso_simulaciones.misextaapp.componentes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Reloj extends View {

    private float largo;
    private int colorLineasDivisonesPequenas = Color.BLACK;
    private int colorLineasDivisonesGrandes = Color.BLACK;
    private int colorMarco = Color.BLACK;
    private int colorFondo = Color.WHITE;
    private int colorNumeros = Color.BLACK;
    private int colorAgujaSegundero = Color.BLACK;
    private int colorAgujaMinutero = Color.BLACK;
    private int colorAgujaHorario = Color.BLACK;
    private int colorMarcaEmpresa = Color.BLACK;
    private int colorHoraDigital = Color.BLACK;
    private Calendar calendario;
    private SimpleDateFormat hora;
    private String horaFormatoDigital = "";
    private int segundos, minutos, horas;

    /**
     * Constructor de la clase Reloj
     */
    public Reloj(Context context) {
        super(context);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    /**
     * Calcula la hora actual
     */
    private void obtenerHora() {
        /*
         * Calendar es una clase abstracta y por lo tanto con ella no se pueden crear objetos.
         * La forma de acceder a ella para ser usada es a través del método getInstance()
         */
        calendario = Calendar.getInstance();
        // segundos dentro del minuto
        segundos = calendario.get(Calendar.SECOND);
        // minutos dentro de la hora
        minutos = calendario.get(Calendar.MINUTE);
        // hora dentro del día
        horas = calendario.get(Calendar.HOUR);
    }

    /**
     * Modifica el color de la aguja del segundero
     */
    public void setColorAgujaSegundero(int colorAgujaSegundero) {
        this.colorAgujaSegundero = colorAgujaSegundero;
    }

    /**
     * Modifica el color de la aguja del minutero
     */
    public void setColorAgujaMinutero(int colorAgujaMinutero) {
        this.colorAgujaMinutero = colorAgujaMinutero;
    }

    /**
     * Modifica el color de la aguja del horario
     */
    public void setColorAgujaHorario(int colorAgujaHorario) {
        this.colorAgujaHorario = colorAgujaHorario;
    }

    /**
     * Modifica el color del marco
     */
    public void setColorMarcoo(int colorMarco) {
        this.colorMarco = colorMarco;
    }

    /**
     * Modifica el color del fondo
     */
    public void setColorFondo(int colorFondo) {
        this.colorFondo = colorFondo;
    }

    /**
     * Modifica el color de los números
     */
    public void setColorNumeros(int colorNumeros) {
        this.colorNumeros = colorNumeros;
    }

    /**
     * Modifica el color de las rayas pequeñas
     */
    public void setColorLineasDivisonesPequenas(int colorLineasDivisonesPequenas) {
        this.colorLineasDivisonesPequenas = colorLineasDivisonesPequenas;
    }

    /**
     * Modifica el color de las rayas grandes
     */
    public void setColorLineasDivisonesGrandes(int colorLineasDivisonesGrandes) {
        this.colorLineasDivisonesGrandes = colorLineasDivisonesGrandes;
    }

    /**
     * Modifica el color de las letras que dan el nombre a la empresa
     */
    public void setColorMarcaEmpresa(int colorMarcaEmpresa) {
        this.colorMarcaEmpresa = colorMarcaEmpresa;
    }

    public void setColorHoraDigital(int colorHoraDigital) {
        this.colorHoraDigital = colorHoraDigital;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // configurando el pincel
        Paint pincel = new Paint();
        pincel.setAntiAlias(true); // evita efecto sierra
        pincel.setTextSize(0.05f * largo); // tamaño texto
        pincel.setLinearText(true); // para mejor manejo de la métrica de texto
        pincel.setFilterBitmap(true); // para efectos de buen escalado de bitmaps
        pincel.setDither(true); // para buen manejo de gradientes de color

        /*
         * se graba el estado actual del canvas para al final restaurarlo
         */
        canvas.save();

        /*
         * La vista tendrá las mismas dimensiones de su contenedor
         */
        float ancho = this.getWidth(); // ancho de la vista
        float alto = this.getHeight(); // alto de la vista

        /*
         * Se define la variable largo como el 80% del menor valor entre alto y largo del contenedor
         */
        if (ancho > alto) {
            largo = 0.8f * alto;
        } else {
            largo = 0.8f * ancho;
        }

        /*
         * se hace traslación del (0,0) al centro del contenedor
         */
        canvas.translate(0.5f * ancho, 0.5f * alto);

        // dibujar círculo de fondo
        float radio = (float) (0.5 * largo);
        pincel.setColor(colorFondo);
        pincel.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0, 0, radio, pincel);

        // dibujar marco (perímetro)
        pincel.setColor(colorMarco);
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeWidth(0.02f * largo);
        canvas.drawCircle(0, 0, radio, pincel);

        // dibujar divisiones del reloj
        float indent = (float) (0.05 * largo);
        float posicionY = (float) (0.5 * largo);

        for (int i = 0; i < 60; i = i + 1) {
            int anguloRotacion = 6 * i;
            canvas.save();
            canvas.rotate(anguloRotacion, 0, 0);
            if (anguloRotacion % 30 == 0) {
                // marcas más largas cada 30 grados
                pincel.setStrokeWidth(0.02f * largo); // grueso de marcas largas
                pincel.setColor(colorLineasDivisonesGrandes);
                canvas.drawLine(0, -posicionY, 0, -posicionY + 1.5f * indent, pincel);
            } else {
                // marcas pequeñas cada 6 grados (1 minuto)
                pincel.setStrokeWidth(0.005f * largo); // grueso de marcas pequeñas
                pincel.setColor(colorLineasDivisonesPequenas);
                canvas.drawLine(0, -posicionY, 0, -posicionY + indent, pincel);
            }
            canvas.restore();
        }

        // dibujar los números
        pincel.setStyle(Paint.Style.FILL);
        for (int i = 1; i < 13; i = i + 1) {
            int anguloRotacion = 30 * i;
            canvas.save();
            // rota el lienzo para posicionar el número
            canvas.rotate(anguloRotacion, 0, 0);
            // endereza el número para que no quede inclinado
            canvas.rotate(-anguloRotacion, 0, -posicionY + 2.5f * indent);

            float anchoCadenaNumero = pincel.measureText("" + i);
            pincel.setColor(colorNumeros);
            canvas.drawText("" + i, -0.5f * anchoCadenaNumero, -posicionY + 2.5f * indent, pincel);
            canvas.restore();
        }

        obtenerHora();

        // dibuja segundero (hay 60 divisiones)
        int anguloRotacionSegundero = segundos * 360 / 60;
        pincel.setStrokeWidth(0.005f * largo);
        canvas.save();
        canvas.rotate(anguloRotacionSegundero, 0, 0);
        pincel.setColor(colorAgujaSegundero);
        canvas.drawLine(0, -0.7f * radio, 0, 2 * indent, pincel);
        canvas.restore();

        // dibuja minutero
        int anguloRotacionMinutero = (int) ((minutos + (float) (segundos / 60.0f)) * 360 / 60);
        pincel.setStrokeWidth(0.015f * largo);
        canvas.save();
        canvas.rotate(anguloRotacionMinutero, 0, 0);
        pincel.setColor(colorAgujaMinutero);
        canvas.drawLine(0, -0.6f * radio, 0, 2 * indent, pincel);
        canvas.restore();

        // dibuja horario
        int anguloRotacionHorario = (int) ((horas + (float) minutos / 60.0f) * 360 / 12);
        pincel.setStrokeWidth(0.025f * largo);
        canvas.save();
        canvas.rotate(anguloRotacionHorario, 0, 0);
        pincel.setColor(colorAgujaHorario);
        canvas.drawLine(0, -0.5f * radio, 0, 2 * indent, pincel);
        canvas.restore();

        // dibuja punto central
        pincel.setColor(Color.RED);
        pincel.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0, 0, 0.05f * radio, pincel);

        // marcar empresa
        String empresa = "IoT.PhysicsSensor";
        pincel.setTextSize(0.05f * largo);
        float anchoCadenaNombreEmpresa = pincel.measureText(empresa);
        pincel.setColor(colorMarcaEmpresa);
        canvas.drawText(empresa, -0.5f * anchoCadenaNombreEmpresa, -0.20f * largo, pincel);

        // dibuja despliegue forma digital de la hora
        horaFormatoDigital = String.format("%02d:%02d:%02d", horas, minutos, segundos);
        pincel.setTextSize(0.08f * largo);
        float anchoCadenaHoraDigital = pincel.measureText(horaFormatoDigital);
        pincel.setColor(colorHoraDigital);
        canvas.drawText(horaFormatoDigital, -0.5f * anchoCadenaHoraDigital, 0.20f * largo, pincel);

        // se restaura el canvas al estado inicial
        canvas.restore();

        // para efectos de animación (vuelve a llamar a onDraw)
        invalidate();
    }
}