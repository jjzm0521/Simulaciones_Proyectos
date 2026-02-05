package com.curso_simulaciones.mitrigesimaterceraapp.modelo;

import com.curso_simulaciones.mitrigesimaterceraapp.AlmacenDatosRAM;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StreamTokenizer;

public class LeerDatosTXT {

    public LeerDatosTXT() {

    }

    public void leer(String path) {

        int i = 0;

        try {
            FileReader fr = new FileReader(path);
            BufferedReader text = new BufferedReader(fr);
            StreamTokenizer streamtokenizer = new StreamTokenizer(text);

            while (streamtokenizer.nextToken() != StreamTokenizer.TT_EOF) {

                if (streamtokenizer.ttype == StreamTokenizer.TT_NUMBER) {
                    double x = streamtokenizer.nval;

                    if (streamtokenizer.nextToken() == StreamTokenizer.TT_NUMBER) {
                        double y = streamtokenizer.nval;

                        if (i < AlmacenDatosRAM.x.length) {
                            AlmacenDatosRAM.x[i] = x;
                            AlmacenDatosRAM.y[i] = y;
                            i++;
                        } else {
                            break; // Evitar desbordamiento del arreglo
                        }
                    }
                }
            }

            AlmacenDatosRAM.n = i;
            text.close();

        } catch (Exception ex) {
            ex.printStackTrace(); // Ayuda a ver errores en Logcat
        }

    }

}
