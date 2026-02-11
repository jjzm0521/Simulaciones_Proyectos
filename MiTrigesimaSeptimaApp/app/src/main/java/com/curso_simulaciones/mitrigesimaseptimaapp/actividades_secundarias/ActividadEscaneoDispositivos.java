package com.curso_simulaciones.mitrigesimaseptimaapp.actividades_secundarias;

import android.app.Activity;
import android.os.Bundle;

import com.curso_simulaciones.mitrigesimaseptimaapp.datos.AlmacenDatosRAM;
import com.example.comunicaciones.ScannerBluetooth;

public class ActividadEscaneoDispositivos extends Activity {

    ScannerBluetooth scanear;

    // @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scanear = new ScannerBluetooth(this);
        scanear.descubriendoDispositivos();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (scanear != null) {
            AlmacenDatosRAM.direccion = scanear.direccion;
            // If 'scanear.direccion' is what we want for
            // 'AlmacenDatosRAM.conexion_bluetooth'
            AlmacenDatosRAM.conexion_bluetooth = scanear.direccion;
        }
    }

}
