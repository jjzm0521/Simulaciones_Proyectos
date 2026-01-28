package com.curso_simulaciones.mivigesimasegundaapp.controlador;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.curso_simulaciones.mivigesimasegundaapp.datos.AlmacenDatosRAM;
import com.curso_simulaciones.mivigesimasegundaapp.vista.CR;
import com.curso_simulaciones.mivigesimasegundaapp.vista.Pizarra;

import com.curso_simulaciones.simulphysics.objetos_laboratorio.Cuerda;
import com.curso_simulaciones.simulphysics.objetos_laboratorio.CuerpoRectangular;
import com.curso_simulaciones.simulphysics.objetos_laboratorio.Masa;
import com.curso_simulaciones.simulphysics.objetos_laboratorio.Marca;
import com.curso_simulaciones.simulphysics.objetos_laboratorio.ObjetoLaboratorio;
import com.curso_simulaciones.simulphysics.objetos_laboratorio.Polea;

public class ActividadControladora extends Activity {

        private Pizarra pizarra;
        private ObjetoLaboratorio[] objetos = new ObjetoLaboratorio[25];

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                gestionarResolucion();
                crearElementosGUI();

                ViewGroup.LayoutParams parametro_layout_principal = new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                this.setContentView(crearGUI(), parametro_layout_principal);
        }

        private void gestionarResolucion() {
                CR.anchoPizarra = AlmacenDatosRAM.ancho_pantalla;
                CR.altoPizarra = AlmacenDatosRAM.alto_pantalla;
        }

        private void crearElementosGUI() {
                pizarra = new Pizarra(this);
                pizarra.setBackgroundColor(Color.WHITE);
                crearObjetosLaboratorio();
        }

        private LinearLayout crearGUI() {
                LinearLayout linear_principal = new LinearLayout(this);
                linear_principal.setOrientation(LinearLayout.VERTICAL);
                linear_principal.addView(pizarra);
                return linear_principal;
        }

        private void crearObjetosLaboratorio() {

                int idx = 0;

                // ========== REFERENCIAS BASE ==========
                float W = CR.anchoPizarra;
                float H = CR.altoPizarra;

                // ========== RADIOS ==========
                float R = 0.025f * W; // Radio poleas azules
                float Rp = 0.022f * W; // Radio polea móvil P

                // ========== DIMENSIONES MASAS ==========
                float ancho_masa = 0.04f * W;
                float alto_masa = 0.02f * H;

                // ========== CENTRADO HORIZONTAL ==========
                // Panel centrado: de 0.25W a 0.65W (ancho 40% del W, centrado en 0.45W)
                float panelLeftX = 0.25f * W;
                float panelRightX = 0.65f * W;
                float panelCenterX = (panelLeftX + panelRightX) / 2; // 0.45W

                float xL = panelLeftX; // Polea izquierda
                float xR = panelRightX; // Polea derecha
                float xP = xR; // Polea P (misma X que polea derecha)

                // ========== POSICIONES VERTICALES (subir todo) ==========
                float topY = 0.05f * H; // Margen superior pequeño

                float yPulley = topY + R;

                // Panel amarillo (reducido y subido)
                float panelTopY = topY + 0.01f * H;
                float panelBottomY = 0.55f * H; // Mucho más pequeño
                float panelCenterY = (panelTopY + panelBottomY) / 2;
                float panelHeight = panelBottomY - panelTopY;
                float panelWidth = panelRightX - panelLeftX;

                // Polea móvil P: 45% de la altura del panel
                float yP = panelTopY + 0.45f * panelHeight;

                // ========== RAMAS DE P ==========
                float x2 = xP - Rp; // Rama izquierda (m2)
                float x3 = xP + Rp; // Rama derecha (m3)

                // ========== MASAS m2 y m3 ==========
                float xm3 = x3;
                float ym3 = yP + 2.0f * Rp;

                float xm2 = x2;
                float ym2 = ym3 + 1.8f * Rp;

                // ========== MASA m1 ==========
                float xm1 = xL;
                float ym1 = panelTopY + 0.55f * panelHeight;

                // ========== BASE VERDE ==========
                float baseTopY = panelBottomY;
                float baseHeight = 0.04f * H;
                float baseLeftX = panelLeftX - 0.05f * W;
                float baseRightX = panelRightX + 0.05f * W;
                float baseWidth = baseRightX - baseLeftX;
                float baseCenterX = (baseLeftX + baseRightX) / 2;
                float baseCenterY = baseTopY + baseHeight / 2;

                // ==================== CREACIÓN DE OBJETOS ====================

                // CAPA 1: Panel amarillo
                CuerpoRectangular panel = new CuerpoRectangular(panelCenterX, panelCenterY, panelWidth, panelHeight);
                panel.setColor(Color.rgb(230, 230, 50));
                objetos[idx++] = panel;

                // CAPA 2: Base verde oscuro
                CuerpoRectangular base = new CuerpoRectangular(baseCenterX, baseCenterY, baseWidth, baseHeight);
                base.setColor(Color.rgb(40, 70, 40));
                objetos[idx++] = base;

                // CAPA 3: CUERDAS

                // Cuerda horizontal superior
                Cuerda cuerda_horizontal = new Cuerda(xL, topY, xR, topY);
                cuerda_horizontal.setColor(Color.RED);
                cuerda_horizontal.setGrosorLinea(0.003f * W);
                objetos[idx++] = cuerda_horizontal;

                // Cuerda vertical izquierda
                Cuerda cuerda_izq = new Cuerda(xL, yPulley + R, xm1, ym1 - alto_masa / 2);
                cuerda_izq.setColor(Color.RED);
                cuerda_izq.setGrosorLinea(0.003f * W);
                objetos[idx++] = cuerda_izq;

                // Cuerda soporte P (VERTICAL)
                Cuerda cuerda_soporte_P = new Cuerda(xR, yPulley + R, xP, yP);
                cuerda_soporte_P.setColor(Color.RED);
                cuerda_soporte_P.setGrosorLinea(0.003f * W);
                objetos[idx++] = cuerda_soporte_P;

                // Cuerda P a m3
                Cuerda cuerda_P_m3 = new Cuerda(xP + Rp, yP, xm3, ym3 - alto_masa / 2);
                cuerda_P_m3.setColor(Color.RED);
                cuerda_P_m3.setGrosorLinea(0.003f * W);
                objetos[idx++] = cuerda_P_m3;

                // Cuerda P a m2
                Cuerda cuerda_P_m2 = new Cuerda(xP - Rp, yP, xm2, ym2 - alto_masa / 2);
                cuerda_P_m2.setColor(Color.RED);
                cuerda_P_m2.setGrosorLinea(0.003f * W);
                objetos[idx++] = cuerda_P_m2;

                // CAPA 4: POLEAS

                // Polea izquierda (azul)
                Polea polea_izq = new Polea(xL, yPulley, R);
                polea_izq.setColor(Color.BLUE);
                polea_izq.setGrosorLinea(0.003f * W);
                polea_izq.setSoportePolea(true);
                polea_izq.rotarEje(180);
                objetos[idx++] = polea_izq;

                // Polea derecha (azul)
                Polea polea_der = new Polea(xR, yPulley, R);
                polea_der.setColor(Color.BLUE);
                polea_der.setGrosorLinea(0.003f * W);
                polea_der.setSoportePolea(true);
                polea_der.rotarEje(180);
                objetos[idx++] = polea_der;

                // Polea móvil P (verde)
                Polea polea_P = new Polea(xP, yP, Rp);
                polea_P.setColor(Color.rgb(100, 180, 100));
                polea_P.setGrosorLinea(0.003f * W);
                objetos[idx++] = polea_P;

                // CAPA 5: MASAS

                Masa masa_1 = new Masa(xm1, ym1, ancho_masa, alto_masa);
                masa_1.setColor(Color.rgb(100, 200, 100));
                masa_1.setColorMarca(Color.BLACK);
                masa_1.setMarca("m1");
                objetos[idx++] = masa_1;

                Masa masa_3 = new Masa(xm3, ym3, ancho_masa, alto_masa);
                masa_3.setColor(Color.rgb(100, 200, 100));
                masa_3.setColorMarca(Color.BLACK);
                masa_3.setMarca("m3");
                objetos[idx++] = masa_3;

                Masa masa_2 = new Masa(xm2, ym2, ancho_masa, alto_masa);
                masa_2.setColor(Color.rgb(100, 200, 100));
                masa_2.setColorMarca(Color.BLACK);
                masa_2.setMarca("m2");
                objetos[idx++] = masa_2;

                // CAPA 6: ETIQUETAS

                Marca marca_m1 = new Marca("m1", xm1 - ancho_masa, ym1);
                marca_m1.setColor(Color.BLACK);
                marca_m1.setTamano(0.018f * W);
                objetos[idx++] = marca_m1;

                Marca marca_P = new Marca("P", xP + 1.8f * Rp, yP);
                marca_P.setColor(Color.BLACK);
                marca_P.setTamano(0.018f * W);
                objetos[idx++] = marca_P;

                Marca marca_m3 = new Marca("m3", xm3 + ancho_masa, ym3);
                marca_m3.setColor(Color.BLACK);
                marca_m3.setTamano(0.018f * W);
                objetos[idx++] = marca_m3;

                Marca marca_m2 = new Marca("m2", xm2 + ancho_masa, ym2);
                marca_m2.setColor(Color.BLACK);
                marca_m2.setTamano(0.018f * W);
                objetos[idx++] = marca_m2;

                pizarra.setEstadoEscena(objetos);
        }

}
