package com.curso_simulaciones.mitrigesimaoctavaapp.actividades_secundarias;

import android.app.Activity;
import android.os.Bundle;

import com.example.comunicaciones.ScannerBluetooth;
import com.curso_simulaciones.mitrigesimaoctavaapp.datos.AlmacenDatosRAM;

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
        AlmacenDatosRAM.direccion = scanear.direccion;
        AlmacenDatosRAM.conexion_bluetooth = scanear.direccion;
    }

}
