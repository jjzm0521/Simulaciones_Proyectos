package com.curso_simulaciones.mivigesimaquintaapp.modelo;

import com.curso_simulaciones.mivigesimaquintaapp.vista.CR;

/**
 * Modelo físico del polipasto compuesto.
 * Calcula posiciones, aceleraciones y tensiones basándose
 * en las ecuaciones de la dinámica del sistema.
 */
public class ModeloFisico {

    // Gravedad en m/s²
    private float g = 9.8f;

    // Factores de conversión
    private float factorConversion_metroApixel;
    private float factorConversion_pixelAmetro;

    public ModeloFisico() {
    }

    /**
     * Calcula el estado del sistema dado el tiempo y las masas.
     * 
     * POLIPASTO COMPUESTO DE ATWOOD:
     * - m1 cuelga de la polea fija 1 (izquierda)
     * - La cuerda pasa por polea 2 (fija) hasta la polea P (móvil)
     * - m2 y m3 cuelgan de la polea móvil P (como un Atwood simple)
     * 
     * Relaciones cinemáticas:
     * - Si m1 baja una distancia d, la polea P sube d/2
     * - m2 y m3 tienen movimiento adicional relativo entre sí (Atwood sobre P)
     */
    public void setCalculos(float tiempo, float m1, float m2, float m3) {

        factorConversion();

        // ================================================================
        // CÁLCULO DE ACELERACIONES SEGÚN EL MODELO DINÁMICO (explicación.md)
        // ================================================================

        // Denominador común de las fórmulas
        float D = m1 * m2 + m1 * m3 + 4f * m2 * m3;

        if (Math.abs(D) < 0.001f)
            D = 0.001f;

        // Aceleración de m1 (hacia abajo es positivo)
        // a1 = g * (m1*m2 + m1*m3 - 4*m2*m3) / D
        float a1 = g * (m1 * m2 + m1 * m3 - 4f * m2 * m3) / D;

        // Aceleración de la polea P (hacia abajo es positivo)
        // Restricción cinemática: a1 + 2*aP = 0 => aP = -a1/2 (pero en este caso es
        // 1:1)
        // Revisando DCL: aP = -a1
        float aP = -a1;

        // Aceleración relativa m2 vs m3 en el marco de la polea P
        // a_rel = (g - aP) * (m2 - m3) / (m2 + m3)
        // (g - aP) es la gravedad efectiva en el marco acelerado de la polea
        float suma_m2_m3 = m2 + m3;
        if (Math.abs(suma_m2_m3) < 0.001f)
            suma_m2_m3 = 0.001f;
        float a_rel = (g - aP) * (m2 - m3) / suma_m2_m3;

        // Aceleraciones absolutas (suelo)
        // a2 = aP + a_rel (componente traslacional de P + componente relativa)
        // a3 = aP - a_rel (componente traslacional de P - componente relativa)
        float a2 = aP + a_rel;
        float a3 = aP - a_rel;

        // ================================================================
        // CÁLCULO DE TENSIONES
        // ================================================================
        // Tensión en el cable 2 (el que pasa por P y sostiene m2, m3)
        // T2 = (4 * g * m1 * m2 * m3) / D
        float T2 = (4f * g * m1 * m2 * m3) / D;

        // Tensión en el cable 1 (el que sostiene m1 y la polea móvil P)
        // T1 = 2 * T2 (polea móvil ideal mP=0)
        float T1 = 2f * T2;

        // ================================================================
        // DESPLAZAMIENTOS (cinemática: d = 0.5 * a * t²)
        // ================================================================
        float desplazamiento_m1_en_metros = 0.5f * a1 * tiempo * tiempo;
        float desplazamiento_P_en_metros = 0.5f * aP * tiempo * tiempo;
        float desplazamiento_m2_en_metros = 0.5f * a2 * tiempo * tiempo;
        float desplazamiento_m3_en_metros = 0.5f * a3 * tiempo * tiempo;

        // Almacenar desplazamientos en metros
        AlmacenDatosRAM.desplazamiento_m1_en_metros = desplazamiento_m1_en_metros;
        AlmacenDatosRAM.desplazamiento_m2_en_metros = desplazamiento_m2_en_metros;
        AlmacenDatosRAM.desplazamiento_m3_en_metros = desplazamiento_m3_en_metros;

        // Convertir a píxeles
        float desplazamiento_m1_en_pixeles = factorConversion_metroApixel * desplazamiento_m1_en_metros;
        float desplazamiento_P_en_pixeles = factorConversion_metroApixel * desplazamiento_P_en_metros;
        float desplazamiento_m2_en_pixeles = factorConversion_metroApixel * desplazamiento_m2_en_metros;
        float desplazamiento_m3_en_pixeles = factorConversion_metroApixel * desplazamiento_m3_en_metros;

        AlmacenDatosRAM.desplazamiento_m1_en_pixeles = desplazamiento_m1_en_pixeles;
        AlmacenDatosRAM.desplazamiento_m2_en_pixeles = desplazamiento_m2_en_pixeles;
        AlmacenDatosRAM.desplazamiento_m3_en_pixeles = desplazamiento_m3_en_pixeles;
        AlmacenDatosRAM.desplazamiento_P_en_pixeles = desplazamiento_P_en_pixeles;

        // Posiciones actuales en píxeles (posición inicial + desplazamiento)
        AlmacenDatosRAM.y1_en_pixeles = AlmacenDatosRAM.yi1_en_pixeles + desplazamiento_m1_en_pixeles;
        AlmacenDatosRAM.yP_en_pixeles = AlmacenDatosRAM.yiP_en_pixeles + desplazamiento_P_en_pixeles;
        AlmacenDatosRAM.y2_en_pixeles = AlmacenDatosRAM.yi2_en_pixeles + desplazamiento_m2_en_pixeles;
        AlmacenDatosRAM.y3_en_pixeles = AlmacenDatosRAM.yi3_en_pixeles + desplazamiento_m3_en_pixeles;

        // ================================================================
        // ROTACIÓN DE POLEAS
        // ================================================================
        float radio = AlmacenDatosRAM.radio;
        if (radio > 0) {
            // Polea 1 (fija izquierda): gira según m1
            AlmacenDatosRAM.teta_1 = (float) Math.toDegrees(desplazamiento_m1_en_pixeles / radio);

            // Polea 2 (fija derecha): gira según la cuerda que sube/baja a P
            // Si P sube (desp_P < 0), la cuerda en la polea 2 se mueve hacia la izquierda
            AlmacenDatosRAM.teta_2 = (float) Math.toDegrees(-desplazamiento_P_en_pixeles / radio);

            // Polea P (móvil): gira según el movimiento relativo entre m2 y m3
            float mov_rel_P = (desplazamiento_m2_en_pixeles - desplazamiento_m3_en_pixeles) / 2f;
            AlmacenDatosRAM.teta_P = (float) Math.toDegrees(mov_rel_P / radio);
        }

        // Posiciones en metros
        AlmacenDatosRAM.y1_en_metros = factorConversion_pixelAmetro * AlmacenDatosRAM.y1_en_pixeles;
        AlmacenDatosRAM.y2_en_metros = factorConversion_pixelAmetro * AlmacenDatosRAM.y2_en_pixeles;
        AlmacenDatosRAM.y3_en_metros = factorConversion_pixelAmetro * AlmacenDatosRAM.y3_en_pixeles;
        AlmacenDatosRAM.yP_en_metros = factorConversion_pixelAmetro * AlmacenDatosRAM.yP_en_pixeles;

        // Almacenar resultados para mostrar en UI
        AlmacenDatosRAM.a1 = a1;
        AlmacenDatosRAM.a2 = a2;
        AlmacenDatosRAM.a3 = a3;
        AlmacenDatosRAM.aP = aP;
        AlmacenDatosRAM.T1 = T1;
        AlmacenDatosRAM.T2 = T2;
        AlmacenDatosRAM.tiempo = tiempo;
    }

    /**
     * Calcula los factores de conversión entre metros y píxeles.
     * Se asume que el alto de la pantalla (landscape) equivale a 2 metros.
     */
    private void factorConversion() {
        // 2 metros = 100% del alto de la pizarra
        factorConversion_metroApixel = CR.pcApxY(100f) / 2f;
        factorConversion_pixelAmetro = 2f / CR.pcApxY(100f);
    }
}
