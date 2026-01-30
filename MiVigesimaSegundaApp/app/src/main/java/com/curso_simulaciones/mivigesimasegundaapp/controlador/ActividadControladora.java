package com.curso_simulaciones.mivigesimasegundaapp.controlador;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import com.curso_simulaciones.mivigesimasegundaapp.datos.AlmacenDatosRAM;
import com.curso_simulaciones.mivigesimasegundaapp.vista.CR;
import com.curso_simulaciones.mivigesimasegundaapp.vista.Pizarra;
import com.curso_simulaciones.simulphysics.objetos_laboratorio.*;

public class ActividadControladora extends Activity {

        private Pizarra pizarra;
        private AlmacenDatosRAM datos;
        private ObjetoLaboratorio[] objetos = new ObjetoLaboratorio[7];

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                // Inicializamos datos básicos
                datos = new AlmacenDatosRAM();

                // Creamos la Pizarra
                pizarra = new Pizarra(this, datos);
                pizarra.setBackgroundColor(Color.WHITE);

                // Listener para RESPONSIVIDAD:
                // Se ejecuta cuando la vista cambia de tamaño (rotación, multi-ventana, inicio)
                pizarra.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                        int oldLeft, int oldTop, int oldRight, int oldBottom) {

                                int width = right - left;
                                int height = bottom - top;

                                // Solo recalculamos si hay dimensiones válidas y hubo cambio
                                if (width > 0 && height > 0
                                                && (width != (oldRight - oldLeft) || height != (oldBottom - oldTop))) {
                                        actualizarDimensiones(width, height);
                                        crearObjetosLaboratorio(); // Recrear objetos con nuevas coordenadas
                                        pizarra.invalidate(); // Redibujar
                                }
                        }
                });

                // Establecemos la vista
                setContentView(pizarra);
        }

        private void actualizarDimensiones(int w, int h) {
                // Actualizamos CR y AlmacenDatosRAM con las dimensiones REALES de la vista
                CR.anchoPizarra = w;
                CR.altoPizarra = h;
                AlmacenDatosRAM.ancho_pantalla = w;
                AlmacenDatosRAM.alto_pantalla = h;
        }

        private void crearObjetosLaboratorio() {
                // Escala
                float r = CR.pcApxL(2.5f);

                // Layout standard centrado (20% - 80%)
                float yTopeBarra = CR.pcApxY(20);
                float ySuelo = CR.pcApxY(80);

                float anchoBarra = CR.pcApxL(45);
                float desplazamiento = r * 1.1f;

                // 1. Barra Amarilla
                objetos[0] = new CuerpoRectangular(CR.pcApxX(50), (yTopeBarra + ySuelo) / 2f, anchoBarra,
                                ySuelo - yTopeBarra);
                objetos[0].setColor(Color.YELLOW);

                float xEsquinaIzq = CR.pcApxX(50) - anchoBarra / 2f;
                float xEsquinaDer = CR.pcApxX(50) + anchoBarra / 2f;

                // 2. Poleas Azules
                Polea p1 = new Polea(xEsquinaIzq - desplazamiento, yTopeBarra - desplazamiento, r);
                p1.setColor(Color.BLUE);
                p1.setSoportePolea(true);
                p1.rotarEje(-45);
                objetos[1] = p1;

                Polea p2 = new Polea(xEsquinaDer + desplazamiento, yTopeBarra - desplazamiento, r);
                p2.setColor(Color.BLUE);
                p2.setSoportePolea(true);
                p2.rotarEje(45);
                objetos[2] = p2;

                // 3. Polea Verde P
                Polea pP = new Polea(p2.getPosicionX() + r, CR.pcApxY(40), r);
                pP.setColor(Color.GREEN);
                objetos[3] = pP;

                // 4. Masas
                objetos[4] = new Masa(p1.getPosicionX() - r, CR.pcApxY(60), r * 1.3f, r * 2.0f);
                objetos[4].setColor(Color.GREEN);

                objetos[5] = new Masa(pP.getPosicionX() - r, CR.pcApxY(67), r * 1.2f, r * 1.8f);
                objetos[5].setColor(Color.GREEN);

                objetos[6] = new Masa(pP.getPosicionX() + r, CR.pcApxY(53), r * 1.2f, r * 1.8f);
                objetos[6].setColor(Color.GREEN);

                pizarra.setEstadoEscena(objetos);
        }
}
