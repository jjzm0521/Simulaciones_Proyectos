package com.curso_simulaciones.micuadragesimasegundaapp.actividades_secundarias;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.RequiresApi;

import com.curso_simulaciones.micuadragesimasegundaapp.R;
import com.curso_simulaciones.micuadragesimasegundaapp.comunicaciones.ClientePubSubMQTT;
import com.curso_simulaciones.micuadragesimasegundaapp.datos.AlmacenDatosRAM;
import com.curso_simulaciones.micuadragesimasegundaapp.utilidades.Acelerometro;
import com.curso_simulaciones.micuadragesimasegundaapp.utilidades.Boton;
import com.curso_simulaciones.micuadragesimasegundaapp.utilidades.Magnetometro;

import org.json.JSONException;
import org.json.JSONObject;

public class ActividadComoClientePubMQTT extends Activity implements Runnable {

    private int tamanoLetraResolucionIncluida;
    private int COLOR_1 = Color.rgb(220, 156, 80);
    private Boton ax, ay, az, a;
    private Boton bx, by, bz, b; // Botones para magnetómetro
    private Acelerometro acelerometro;
    private Magnetometro magnetometro;

    protected TextView textviewRol;
    private TextView textviewAviso;

    protected Button botonConectar;
    private float medida_acelerometro, medida_magnetometro;
    private int componente_aceleracion = 4;
    private int componente_magnetica = 4;

    private ClientePubSubMQTT cliente;

    // Use a flag for the loop instead of while(true) to allow proper cleanup
    private boolean corriendo = false;
    private Thread hilo;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gestionarResolucion();

        // para crear elementos de la GUI
        crearElementosGUI();

        /*
         * Para informar cómo se debe pegar el administrador de
         * diseño LinearLayout obtenido con el método crearGui()
         */
        ViewGroup.LayoutParams parametro_layout_principal = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // pegar el contenedor con la GUI
        this.setContentView(crearGUI(), parametro_layout_principal);

        eventos();

        crearCliente();

        // hilo = new Thread(this); // Move to start

    }// fin del método onCreate

    private void gestionarResolucion() {

        // tamano de letra para usar acomodado a la resolución de pantalla
        tamanoLetraResolucionIncluida = (int) (0.8 * AlmacenDatosRAM.tamanoLetraResolucionIncluida);

    }

    private void crearElementosGUI() {

        // botones acelerómetro
        ax = new Boton(this);
        ax.setImagen(R.drawable.ax);
        ay = new Boton(this);
        ay.setImagen(R.drawable.ay);
        az = new Boton(this);
        az.setImagen(R.drawable.az);
        a = new Boton(this);
        a.setImagen(R.drawable.a);

        // botones magnetómetro (necesitará recursos drawable bx, by, bz, b si no
        // existen, usaré placeholder o reutilizaré con texto si es posible, pero aquí
        // asumo existen o usaré los mismos y cambiaré texto si la clase Boton lo
        // permite.
        // Como el profesor pide "pegar elementos", asumo que debo usar lo que hay. Si
        // no tengo imagenes para B, usaré las de A temporalmente o texto.
        // La clase Boton tiene setText.
        bx = new Boton(this);
        bx.setImagen(R.drawable.bx, true);
        bx.setText("Bx");
        by = new Boton(this);
        by.setImagen(R.drawable.by, true);
        by.setText("By");
        bz = new Boton(this);
        bz.setImagen(R.drawable.bz, true);
        bz.setText("Bz");
        b = new Boton(this);
        b.setImagen(R.drawable.b, true);
        b.setText("B");

        acelerometro = new Acelerometro(this);
        acelerometro.setUnidades(" m.s-2");
        acelerometro.setRango(0, 20);
        acelerometro.setAngulosSectores(50, 100, 100);
        acelerometro.setColorFranjaDinamica(Color.rgb(0, 255, 0));
        acelerometro.captarSensor(this);

        magnetometro = new Magnetometro(this);
        magnetometro.setUnidades(" uT");
        magnetometro.setRango(0, 100);
        magnetometro.captarSensor(this);

        textviewRol = new TextView(this);
        textviewRol.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) (2f * tamanoLetraResolucionIncluida));
        textviewRol.setBackgroundColor(Color.YELLOW);
        textviewRol.setText("PUB");
        textviewRol.setTextColor(Color.RED);
        textviewRol.setGravity(Gravity.CENTER);
        textviewRol.setEnabled(false);

        textviewAviso = new TextView(this);
        textviewAviso.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) (0.8 * tamanoLetraResolucionIncluida));
        textviewAviso.setBackgroundColor(Color.YELLOW);
        textviewAviso.setTextColor(Color.RED);
        textviewAviso.setGravity(Gravity.CENTER);

        botonConectar = new Button(this);
        botonConectar.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        botonConectar.setText("CONECTAR");
        botonConectar.getBackground().setColorFilter(COLOR_1, PorterDuff.Mode.MULTIPLY);
        botonConectar.setTextSize(tamanoLetraResolucionIncluida);

    }

    private LinearLayout crearGUI() {

        // LinearLayoutPrincipal
        LinearLayout linearLayoutPrincipal = new LinearLayout(this);
        linearLayoutPrincipal.setOrientation(LinearLayout.VERTICAL);
        linearLayoutPrincipal.setBackgroundColor(Color.BLACK);
        linearLayoutPrincipal.setWeightSum(10f);

        // Fila 1: Título/Rol
        LinearLayout linearLayoutFilaUno = new LinearLayout(this);
        linearLayoutFilaUno.setBackgroundColor(Color.WHITE);

        // Fila 2: Acelerómetro + Botones
        LinearLayout linearLayoutFilaDos = new LinearLayout(this);
        linearLayoutFilaDos.setBackgroundColor(Color.WHITE);
        linearLayoutFilaDos.setWeightSum(10f);

        // Fila 3: Magnetómetro + Botones
        LinearLayout linearLayoutFilaTres = new LinearLayout(this);
        linearLayoutFilaTres.setBackgroundColor(Color.WHITE);
        linearLayoutFilaTres.setWeightSum(10f);
        // linearLayoutFilaTres.setOrientation(LinearLayout.HORIZONTAL); // Already
        // horizontal by default? No, layoutParams matter.

        // Fila 4: Aviso
        LinearLayout linearLayoutFilaCuatro = new LinearLayout(this);
        linearLayoutFilaCuatro.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutFilaCuatro.setBackgroundColor(Color.WHITE);

        // Fila 5: Botón Conectar
        LinearLayout linearLayoutFilaCinco = new LinearLayout(this);
        linearLayoutFilaCinco.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutFilaCinco.setBackgroundColor(Color.WHITE);

        // Params Filas
        LinearLayout.LayoutParams parametrosFilaUno = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                0);
        parametrosFilaUno.weight = 1.0f;
        linearLayoutFilaUno.setLayoutParams(parametrosFilaUno);

        LinearLayout.LayoutParams parametrosFilaDos = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                0);
        parametrosFilaDos.weight = 3.75f;
        linearLayoutFilaDos.setLayoutParams(parametrosFilaDos);

        LinearLayout.LayoutParams parametrosFilaTres = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametrosFilaTres.weight = 3.75f;
        linearLayoutFilaTres.setLayoutParams(parametrosFilaTres);

        LinearLayout.LayoutParams parametrosFilaCuatro = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametrosFilaCuatro.weight = 0.5f;
        linearLayoutFilaCuatro.setLayoutParams(parametrosFilaCuatro);

        LinearLayout.LayoutParams parametrosFilaCinco = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametrosFilaCinco.weight = 1.0f;
        linearLayoutFilaCinco.setLayoutParams(parametrosFilaCinco);

        linearLayoutPrincipal.addView(linearLayoutFilaUno);
        linearLayoutPrincipal.addView(linearLayoutFilaDos);
        linearLayoutPrincipal.addView(linearLayoutFilaTres);
        linearLayoutPrincipal.addView(linearLayoutFilaCuatro);
        linearLayoutPrincipal.addView(linearLayoutFilaCinco);

        // --- Fila 1 Content ---
        LinearLayout.LayoutParams parametrosPegadoEditTextRol = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayoutFilaUno.setGravity(Gravity.CENTER);
        textviewRol.setLayoutParams(parametrosPegadoEditTextRol);
        linearLayoutFilaUno.addView(textviewRol);

        // --- Fila 2 Content (Acelerómetro) ---
        // linear segunda columna izquierda (Gauge)
        LinearLayout linearLayoutColumnaDosIzquierda = new LinearLayout(this);
        // linear segunda columna derecha (Botones)
        LinearLayout linearLayoutColumnaDosDerecha = new LinearLayout(this);
        linearLayoutColumnaDosDerecha.setOrientation(LinearLayout.VERTICAL);
        linearLayoutColumnaDosDerecha.setWeightSum(4f);

        LinearLayout.LayoutParams parametrosColumnaDosIzquierda = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT);
        parametrosColumnaDosIzquierda.weight = 8.0f;
        linearLayoutFilaDos.addView(linearLayoutColumnaDosIzquierda, parametrosColumnaDosIzquierda);

        LinearLayout.LayoutParams parametrosColumnaDosDerecha = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT);
        parametrosColumnaDosDerecha.weight = 2.0f;
        linearLayoutFilaDos.addView(linearLayoutColumnaDosDerecha, parametrosColumnaDosDerecha);

        // Adicionar acelerometro
        linearLayoutColumnaDosIzquierda.addView(acelerometro);

        // Adicionar botones aceleracion
        LinearLayout.LayoutParams parametrosPegadoBotones = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametrosPegadoBotones.weight = 1.0f;
        linearLayoutColumnaDosDerecha.addView(ax, parametrosPegadoBotones);
        linearLayoutColumnaDosDerecha.addView(ay, parametrosPegadoBotones);
        linearLayoutColumnaDosDerecha.addView(az, parametrosPegadoBotones);
        linearLayoutColumnaDosDerecha.addView(a, parametrosPegadoBotones);

        // --- Fila 3 Content (Magnetómetro) ---
        // Same structure as Fila 2
        LinearLayout linearLayoutColumnaTresIzquierda = new LinearLayout(this);
        LinearLayout linearLayoutColumnaTresDerecha = new LinearLayout(this);
        linearLayoutColumnaTresDerecha.setOrientation(LinearLayout.VERTICAL);
        linearLayoutColumnaTresDerecha.setWeightSum(4f);

        LinearLayout.LayoutParams parametrosColumnaTresIzquierda = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT);
        parametrosColumnaTresIzquierda.weight = 8.0f;
        linearLayoutFilaTres.addView(linearLayoutColumnaTresIzquierda, parametrosColumnaTresIzquierda);

        LinearLayout.LayoutParams parametrosColumnaTresDerecha = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT);
        parametrosColumnaTresDerecha.weight = 2.0f;
        linearLayoutFilaTres.addView(linearLayoutColumnaTresDerecha, parametrosColumnaTresDerecha);

        // Adicionar magnetometro
        linearLayoutColumnaTresIzquierda.addView(magnetometro);

        // Adicionar botones magnetometro
        linearLayoutColumnaTresDerecha.addView(bx, parametrosPegadoBotones);
        linearLayoutColumnaTresDerecha.addView(by, parametrosPegadoBotones);
        linearLayoutColumnaTresDerecha.addView(bz, parametrosPegadoBotones);
        linearLayoutColumnaTresDerecha.addView(b, parametrosPegadoBotones);

        // --- Fila 4 Content (Aviso) ---
        LinearLayout.LayoutParams parametrosPegadoAviso = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayoutFilaCuatro.addView(textviewAviso, parametrosPegadoAviso);

        // --- Fila 5 Content (Conectar) ---
        LinearLayout.LayoutParams parametrosPegadoBotonesConnect = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT);
        parametrosPegadoBotonesConnect.weight = 1.0f;
        linearLayoutFilaCinco.addView(botonConectar, parametrosPegadoBotonesConnect);

        return linearLayoutPrincipal;
    }

    protected void eventos() {

        // Acelerómetro listeners
        ax.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                acelerometro.setComponenteAcelerometro(1);
                componente_aceleracion = 1;
            }
        });
        ay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                acelerometro.setComponenteAcelerometro(2);
                componente_aceleracion = 2;
            }
        });
        az.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                acelerometro.setComponenteAcelerometro(3);
                componente_aceleracion = 3;
            }
        });
        a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                acelerometro.setComponenteAcelerometro(4);
                componente_aceleracion = 4;
            }
        });

        // Magnetómetro listeners
        bx.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                magnetometro.setComponenteMagnetica(1);
                componente_magnetica = 1;
            }
        });
        by.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                magnetometro.setComponenteMagnetica(2);
                componente_magnetica = 2;
            }
        });
        bz.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                magnetometro.setComponenteMagnetica(3);
                componente_magnetica = 3;
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                magnetometro.setComponenteMagnetica(4);
                componente_magnetica = 4;
            }
        });

        // evento cliente
        botonConectar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (botonConectar.getText() == "CONECTAR") {
                    botonConectar.setText("EMPEZAR");
                    // conectar cliente
                    cliente.conectar();

                } else {

                    if (hilo == null || !corriendo) {
                        corriendo = true;
                        hilo = new Thread(ActividadComoClientePubMQTT.this);
                        hilo.start();
                    }
                    botonConectar.setEnabled(false);

                }

            }
        });

    }

    public void crearCliente() {

        cliente = new ClientePubSubMQTT(this);

    }

    protected void onResume() {
        super.onResume();

    }

    protected void onPause() {
        super.onPause();
        corriendo = false;
        hilo = null;
        if (cliente != null)
            cliente.desconectar();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void run() {

        while (corriendo) {

            try {
                Thread.sleep(200);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // escribir IoT
            escribir();
            // actualizar aqui estado de conexión
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    actualizarAviso();
                }
            });

        }

    }

    /*
     * comunicaciones IoT PUB
     */

    private void escribir() {

        // escribir (enviar) datos al cliente
        String dato = getStringJSON();
        byte[] dato_en_byte = dato.getBytes();
        if (dato_en_byte != null) {
            cliente.setEnviarMensajes(dato_en_byte);

        }
    }

    private String getStringJSON() {

        JSONObject obj = new JSONObject();

        try {

            medida_acelerometro = acelerometro.getMedida();
            medida_magnetometro = magnetometro.getMedida();

            // Sending generic structure
            obj.put("componente_a", componente_aceleracion);
            obj.put("valor_acelerometro", medida_acelerometro);

            obj.put("componente_m", componente_magnetica);
            obj.put("valor_magnetometro", medida_magnetometro);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // convertir a String
        return obj.toString();

    }

    private void actualizarAviso() {

        textviewAviso.setText("Estado conexión IoT:" + AlmacenDatosRAM.conectado_PubSub);

    }

}
