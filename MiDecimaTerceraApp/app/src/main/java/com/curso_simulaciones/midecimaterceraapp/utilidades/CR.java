package com.curso_simulaciones.midecimaterceraapp.utilidades;

public class CR {

    // ancho y largo de pizarra una vez
    // se establezca la responsividad
    public static float anchoPizarra = 0f;
    public static float altoPizarra = 0f;

    /**
     * pasar de por ciento del ancho de pizarra a pixeles en X
     *
     * @param pc_x
     * @return
     */
    public static float pcApxX(float pc_x) {
        return (anchoPizarra * pc_x / 100f);
    }

    /**
     * pasar de por ciento del alto de pizarra a pixeles en Y
     *
     * @param pc_y
     * @return
     */
    public static float pcApxY(float pc_y) {
        return (altoPizarra * pc_y / 100f);
    }

    /**
     * pasar de por ciento de la menor
     * de anchoPizarra y altoPizarra a pixeles
     *
     * @param pc_l
     * @return
     */
    public static float pcApxL(float pc_l) {
        float menor;
        if (anchoPizarra < altoPizarra)
            menor = anchoPizarra;
        else
            menor = altoPizarra;

        return (menor * pc_l / 100f);
    }
}
