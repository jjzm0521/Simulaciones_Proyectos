package com.curso_simulaciones.mitrigesimaoctavaapp.actividades_secundarias;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.curso_simulaciones.mitrigesimaoctavaapp.R;
import com.curso_simulaciones.mitrigesimaoctavaapp.comunicaciones.ClienteBluetooth;
import com.curso_simulaciones.mitrigesimaoctavaapp.datos.AlmacenDatosRAM;
import com.curso_simulaciones.mitrigesimaoctavaapp.utilidades.Graficador;
import com.curso_simulaciones.mitrigesimaoctavaapp.utilidades.Luxometro;
import com.curso_simulaciones.mitrigesimaoctavaapp.utilidades.TablaSimple;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONException;
import org.json.JSONObject;

public class ActividadComoClienteBluetooth extends Activity implements Runnable {

    // Objetos GUI necesarios
    private LinearLayout linear_layout_segunda_fila;
    private Luxometro luxometro;
    private TablaSimple tabla;
    private Graficador graficador;
    private TextView text_aviso;
    private Button botonConectar, botonBuscar, boton_tabla_grafica;
    // variable tamaño de las letras basado en resolución de pantalla
    private int tamanoLetraResolucionIncluida;
    // hilos para actualizar tabla y letreros
    private final Handler myHandler = new Handler();

    int n = -1;
    int tiempo_base = 0;
    int tiempo_anterior;
    int tiempo_real;

    private ClienteBluetooth cliente;
    /*
     * hilo resposable de estar pendiente
     * del intercambio de datos con el servidor
     */
    private Thread hilo;
    private boolean corriendo;
    private float medida;
    private int numero_datos = 0;
    private JSONObject obj;

    private LinearLayout.LayoutParams parametros_tabla_grafica;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gestionarResolucion();

        // para crear elementos de la GUI
        crearElementosGUI();

        // para informar cómo se debe pegar el adminitrador de
        // diseño obtenido con el método GUI
        ViewGroup.LayoutParams parametro_layout_principal = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // pegar el contenedor con la GUI
        this.setContentView(crearGUI(), parametro_layout_principal);

        // para administrar los eventos
        eventos();

    }// fin onCreate

    private void gestionarResolucion() {

        // tamano de letra para usar acomodado a la resolución de pantalla
        tamanoLetraResolucionIncluida = (int) (0.8 * AlmacenDatosRAM.tamanoLetraResolucionIncluida);

    }

    /* método responsable de la creación de los elementos de la GUI */
    private void crearElementosGUI() {

        // tacometro
        luxometro = new Luxometro(this);
        luxometro.setRango(0, 100);
        luxometro.setUnidades("lx");

        // tabla
        tabla = new TablaSimple(this);
        tabla.setEtiquetaColumnas("Tiempo (s)", AlmacenDatosRAM.unidades);// "Medida");

        // graficador
        graficador = new Graficador(this);
        // se está muestreando cada segundo (1000 ms)
        graficador.setTituloEjeX("Tiempo (s)");
        graficador.setTituloEjeY(AlmacenDatosRAM.unidades);
        graficador.setGrosorLinea(2f);
        graficador.setColorLinea(Color.RED);
        graficador.setColorValores(Color.YELLOW);
        graficador.setColorMarcadores(Color.GREEN);
        graficador.setColorFondo(Color.BLACK);
        graficador.setColorTextoEjes(Color.WHITE);

        botonBuscar = new Button(this);
        botonBuscar.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        botonBuscar.setText("BUSCAR");
        botonBuscar.getBackground().setColorFilter(Color.rgb(183, 216, 199), PorterDuff.Mode.MULTIPLY);

        botonConectar = new Button(this);
        botonConectar.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        botonConectar.setText("CONECTAR");
        botonConectar.getBackground().setColorFilter(Color.rgb(183, 216, 199), PorterDuff.Mode.MULTIPLY);
        botonConectar.setEnabled(false);

        boton_tabla_grafica = new Button(this);
        boton_tabla_grafica.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        boton_tabla_grafica.setText("GRAFICA");
        boton_tabla_grafica.getBackground().setColorFilter(Color.rgb(183, 216, 199), PorterDuff.Mode.MULTIPLY);
        boton_tabla_grafica.setEnabled(false);

        text_aviso = new TextView(this);
        text_aviso.setGravity(Gravity.FILL_VERTICAL);
        text_aviso.setBackgroundColor(Color.rgb(183, 216, 199));
        text_aviso.setTextSize(0.8f * tamanoLetraResolucionIncluida);
        text_aviso.setText(" Buscar dispositivo fuente de sensores para emparejar...");
        text_aviso.setTextColor(Color.BLACK);

    }// fin crearElemnetosGUI

    /* método responsable de administrar el diseño de la GUI */
    private LinearLayout crearGUI() {

        LinearLayout linear_layout_principal = new LinearLayout(this);
        linear_layout_principal.setOrientation(LinearLayout.VERTICAL);
        linear_layout_principal.setBackgroundColor(Color.rgb(183, 216, 199));
        linear_layout_principal.setWeightSum(10.0f);

        // LinearLayout primera fila
        LinearLayout linear_layout_primera_fila = new LinearLayout(this);
        linear_layout_primera_fila.setOrientation(LinearLayout.VERTICAL);
        linear_layout_primera_fila.setGravity(Gravity.FILL);
        linear_layout_primera_fila.setBackgroundColor(Color.rgb(245, 245, 245));

        // LinearLayout segunda fila
        linear_layout_segunda_fila = new LinearLayout(this);
        linear_layout_segunda_fila.setOrientation(LinearLayout.VERTICAL);
        linear_layout_segunda_fila.setGravity(Gravity.FILL);
        linear_layout_segunda_fila.setBackgroundColor(Color.rgb(245, 245, 245));
        linear_layout_segunda_fila.setWeightSum(1.0f);

        // LinearLayout tercera fila
        LinearLayout linear_layout_tercera_fila = new LinearLayout(this);
        linear_layout_tercera_fila.setBackgroundColor(Color.RED);

        // LinearLayout cuarta fila
        LinearLayout linear_layout_cuarta_fila = new LinearLayout(this);
        linear_layout_cuarta_fila.setOrientation(LinearLayout.HORIZONTAL);
        linear_layout_cuarta_fila.setBackgroundColor(Color.rgb(183, 216, 199));
        linear_layout_cuarta_fila.setWeightSum(3.0f);

        // pegar primera fila al principal
        LinearLayout.LayoutParams parametros_primera_fila = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametros_primera_fila.weight = 4.25f;
        parametros_primera_fila.setMargins(20, 20, 20, 20);
        linear_layout_primera_fila.setLayoutParams(parametros_primera_fila);
        linear_layout_principal.addView(linear_layout_primera_fila);

        // pegar segunda fila al principal
        LinearLayout.LayoutParams parametros_segunda_fila = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametros_segunda_fila.weight = 4.25f;
        parametros_segunda_fila.setMargins(20, 20, 20, 20);
        linear_layout_segunda_fila.setLayoutParams(parametros_segunda_fila);
        linear_layout_principal.addView(linear_layout_segunda_fila);

        // pegar tercera fila al principal
        LinearLayout.LayoutParams parametros_tercera_fila = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametros_tercera_fila.weight = 0.5f;
        parametros_tercera_fila.setMargins(20, 0, 20, 0);
        linear_layout_tercera_fila.setLayoutParams(parametros_tercera_fila);
        linear_layout_principal.addView(linear_layout_tercera_fila);

        // pegar cuarta fila al principal
        LinearLayout.LayoutParams parametros_cuarta_fila = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametros_cuarta_fila.weight = 1.0f;
        parametros_cuarta_fila.setMargins(20, 20, 20, 20);
        linear_layout_cuarta_fila.setLayoutParams(parametros_cuarta_fila);
        linear_layout_principal.addView(linear_layout_cuarta_fila);

        // pegar gauge en primera fila
        linear_layout_primera_fila.addView(luxometro);
        // pegar tabla en segunda fila
        parametros_tabla_grafica = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametros_tabla_grafica.weight = 1.0f;
        linear_layout_segunda_fila.addView(tabla, parametros_tabla_grafica);

        // pegado textView en tercera fila
        LinearLayout.LayoutParams parametrosPegadoTextView = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT);
        parametrosPegadoTextView.weight = 1.0f;
        linear_layout_tercera_fila.addView(text_aviso, parametrosPegadoTextView);

        // pegar los botones en la cuarta fila
        LinearLayout.LayoutParams parametros_botones_cuarta_fila = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT);
        parametros_botones_cuarta_fila.weight = 1.0f;
        botonBuscar.setLayoutParams(parametros_botones_cuarta_fila);
        botonConectar.setLayoutParams(parametros_botones_cuarta_fila);
        boton_tabla_grafica.setLayoutParams(parametros_botones_cuarta_fila);

        linear_layout_cuarta_fila.addView(botonBuscar);
        linear_layout_cuarta_fila.addView(botonConectar);
        linear_layout_cuarta_fila.addView(boton_tabla_grafica);

        return linear_layout_principal;
    }// fin gui

    private void eventos() {

        // eventos botones
        botonBuscar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                lanzarBuscandoDispositivos();
                AlmacenDatosRAM.estado_conexion_bluetooth = 1;
                botonConectar.setEnabled(true);
                botonBuscar.setEnabled(false);

            }
        });

        botonConectar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (botonConectar.getText() == "CONECTAR") {
                    botonConectar.setText("EMPEZAR");
                    botonBuscar.setEnabled(false);
                    crearCliente();
                    cliente.conectarSocketCliente();
                    AlmacenDatosRAM.estado_conexion_bluetooth = 2;
                    hacerTrabajoDuro_2();

                } else {
                    borrarDatos();
                    botonConectar.setEnabled(false);
                    boton_tabla_grafica.setEnabled(true);
                    empezarHilo();

                }

            }
        });

        // eventos botones
        boton_tabla_grafica.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (boton_tabla_grafica.getText() == "TABLA") {
                    boton_tabla_grafica.setText("GRAFICA");
                    linear_layout_segunda_fila.removeView(graficador);
                    // pegar tabla en segunda fila
                    linear_layout_segunda_fila.addView(tabla, parametros_tabla_grafica);

                } else {

                    boton_tabla_grafica.setText("TABLA");
                    linear_layout_segunda_fila.removeView(tabla);
                    // pegar graficador en segunda fila
                    linear_layout_segunda_fila.addView(graficador, parametros_tabla_grafica);

                }

            }
        });

    }// fin eventos

    private void lanzarBuscandoDispositivos() {

        Intent intent = new Intent(this, ActividadEscaneoDispositivos.class);
        startActivity(intent);

    }

    public void empezarHilo() {

        // numero_datos=0;
        // n=-1;

        hilo = new Thread(this);
        hilo.start();

    }

    private void crearCliente() {

        // dirección del dispositivo elegido para emparejar
        String direccion = AlmacenDatosRAM.direccion;
        cliente = new ClienteBluetooth();

        /*
         * el cliente instancia un dispositivo Bluetooth
         * (BluetoohDevice) a partir de la dirección
         * de este.Luego abre un socket de tipo BuletoothSocket
         * a partir del dispositivo enlazado.
         */
        cliente.abrirSocketCliente(direccion);
        empezarComunicacionConServidor();

    }

    private void empezarComunicacionConServidor() {

        /*
         * Aceptada la conexión abre los flujos de entrada
         * y salida de este socket por donde fluirán (tubería)
         * los datos desde y hacia el servidor
         */

        cliente.abrirFlujoEntrada();
        cliente.abrirFlujoSalida();

    }

    private void terminarComunicacionConServidor() {

        cliente.cerrarFlujoEntrada();
        cliente.cerrarFlujoSalida();
        cliente.cerrarSocketCliente();

    }

    // no estaba
    private void borrarDatos() {

        AlmacenDatosRAM.datos.clear();
        n = -1;
        numero_datos = 0;
        tabla.borrar();

    }

    @Override
    public void run() {
        corriendo = true;

        while (corriendo) {

            try {
                Thread.sleep(AlmacenDatosRAM.periodo_muestreo);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            leer();

        }

    }

    private void leer() {

        String nuevo_dato_string = cliente.leerString();// String JSON

        if (nuevo_dato_string != null) {
            convertirStrigJson(nuevo_dato_string);
            actualizarTacometroTabla();
            luxometro.setMedida(medida);
            // actualizar tabla y gráfica
            hacerTrabajoDuro_1();
            // avisar que se están recibiendo datso
            AlmacenDatosRAM.estado_conexion_bluetooth = 3;
            AlmacenDatosRAM.conexion_bluetooth = "  Recibiendo datos ...";// de " + AlmacenDatosRAM.tipo_sensor;
            hacerTrabajoDuro_2();
        }

    }

    // obtener la información del JSON
    public void convertirStrigJson(String datoString) {

        // convertir String a JSON
        try {
            obj = new JSONObject(datoString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // obtener la información
        // la medida
        try {
            AlmacenDatosRAM.unidades = obj.getString("unidad");
            // AlmacenDatosRAM.tipo_sensor= obj.getString("sensor");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            AlmacenDatosRAM.tiempo = "" + obj.getInt("tiempo");
            // muestrear a más del doble de frecuencia
            AlmacenDatosRAM.periodo_muestreo = (int) (0.4 * obj.getInt("periodo"));
            medida = (float) obj.getDouble("valor");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void actualizarTacometroTabla() {

        luxometro.setUnidades(AlmacenDatosRAM.unidades);
        luxometro.cambiarEscala(medida);
        tabla.setEtiquetaColumnas("Tiempo en s", "Medida en " + AlmacenDatosRAM.unidades);

    }

    protected void onResume() {
        super.onResume();

        if (AlmacenDatosRAM.estado_conexion_bluetooth == 1) {

            if (AlmacenDatosRAM.direccion == "NO CONECTADO") {

                AlmacenDatosRAM.conexion_bluetooth = "  Buscar dispositivo fuente de sensores para emparejar...";

            } else {

                AlmacenDatosRAM.conexion_bluetooth = "  Emparejado con " + AlmacenDatosRAM.direccion;

            }

            hacerTrabajoDuro_2();
            // actualizarAviso();

        }

    }

    protected void onPause() {
        super.onPause();

    }

    protected void onDestroy() {
        super.onDestroy();

        if (hilo != null) {
            detener();
            hilo = null;
        }

        if (cliente != null) {
            terminarComunicacionConServidor();
        }

    }

    public void detener() {
        corriendo = false;
        hilo = null;

    }

    /*
     * Para el asunto de la table es mejor usar un manejador
     * para que no se reviente la aplicación
     */

    private void hacerTrabajoDuro_1() {
        // .... realizar el trabajo duro

        // Actualiza la UI usando el handler y el runnable
        myHandler.post(updateRunnable_1);

    }

    int t_r_base;

    final Runnable updateRunnable_1 = new Runnable() {
        public void run() {

            n = n + 1;

            if (n == 0) {

                tiempo_base = Integer.parseInt(AlmacenDatosRAM.tiempo);// convertir a entero (ms)

            }

            // el tiempo es el real recibido
            tiempo_real = Integer.parseInt(AlmacenDatosRAM.tiempo) - tiempo_base;

            if (tiempo_real != tiempo_anterior & numero_datos < AlmacenDatosRAM.nDatos) {
                // if(numero_datos<AlmacenDatosRAM.nDatos) {

                /*
                 * Cuando comineza puede pasar que no tome bien datos mientras
                 * sincroniza bien, y por lo tanto no se ha entrado en este if
                 * y el tiempo se toma ananzando, por lo que se debe refdefinir el cero
                 */

                if (numero_datos == 0) {

                    t_r_base = tiempo_real;

                }

                tiempo_real = tiempo_real - t_r_base;
                // Log.d(TAG, "t_r"+tiempo_real);

                // ya redeinido de nuevo el cero procedemos a procesar los datos
                numero_datos = numero_datos + 1;
                // convertir a segundos con dos decimales
                float t_r = (float) Math.round(tiempo_real * 0.001 * 100f) / 100f;

                // actualizar la tabla
                tabla.enviarDatos(t_r, (medida));

                // actualizar la gráfica
                graficador.setTituloEjeY(AlmacenDatosRAM.unidades);
                AlmacenDatosRAM.datos.add((new Entry(t_r, medida)));
                graficador.setDatos(AlmacenDatosRAM.datos);

                tiempo_anterior = tiempo_real;

            }

            if (numero_datos + 1 > AlmacenDatosRAM.nDatos) {

                botonConectar.setEnabled(true);

            }

        }
    };

    /*
     * Para el asunto de actualizar avisos
     * hilo manejador para que no se reviente la aplicación
     */
    public void hacerTrabajoDuro_2() {
        // .... realizar el trabajo duro

        // Actualiza la UI usando el handler y el runnable
        myHandler.post(updateRunnable_2);

    }

    final Runnable updateRunnable_2 = new Runnable() {
        public void run() {

            actualizarAviso();

        }
    };

    private void actualizarAviso() {

        if (AlmacenDatosRAM.estado_conexion_bluetooth == 1) {
            String aviso = AlmacenDatosRAM.conexion_bluetooth;
            text_aviso.setText(aviso);
        }

        if (AlmacenDatosRAM.estado_conexion_bluetooth == 2) {
            String aviso = AlmacenDatosRAM.conexion_bluetooth;
            text_aviso.setText(aviso);
        }

        if (AlmacenDatosRAM.estado_conexion_bluetooth == 3) {
            String aviso = AlmacenDatosRAM.conexion_bluetooth;
            text_aviso.setText(aviso);
        }

    }

}
