package com.curso_simulaciones.mitrigesimaquintaapp.modelo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.curso_simulaciones.mitrigesimaquintaapp.AlmacenDatosRAM;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.UUID;

public class ClienteBluetooth {

    public BluetoothAdapter adaptadorBluetooth;
    public BluetoothDevice dispositivo;
    public BluetoothSocket clienteSocket;

    private BufferedInputStream flujoEntrada;
    private BufferedOutputStream flujoSalida;
    private String datoString;

    private static final String TAG = "ClienteBluetooth";

    public static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public ClienteBluetooth() {

    }

    public void abrirSocketCliente(String direccion) {

        adaptadorBluetooth = BluetoothAdapter.getDefaultAdapter();
        dispositivo = adaptadorBluetooth.getRemoteDevice(direccion);

        try {

            clienteSocket = dispositivo.createRfcommSocketToServiceRecord(uuid);

        } catch (IOException e) {

        }

    }

    public void conectarSocketCliente() {

        try {

            clienteSocket.connect();
            AlmacenDatosRAM.conexion_bluetooth = "  Conectado con "
                    + clienteSocket.getRemoteDevice().getName().toString();

        } catch (IOException e) {
            AlmacenDatosRAM.conexion_bluetooth = "  No se pudo conectar...";
            e.printStackTrace();

        }

    }

    public void abrirFlujoEntrada() {

        if (clienteSocket != null) {

            try {

                flujoEntrada = new BufferedInputStream(clienteSocket.getInputStream());

            } catch (IOException e) {
                e.printStackTrace();

            }

        }

    }

    public void abrirFlujoSalida() {

        if (clienteSocket != null) {
            try {

                flujoSalida = new BufferedOutputStream(clienteSocket.getOutputStream());

            } catch (IOException e) {
                e.printStackTrace();

            }

        }

    }

    public String leerString() {

        int dato;

        byte[] buffer = new byte[8 * 1024];

        try {

            if (flujoEntrada != null) {

                dato = flujoEntrada.read(buffer);

                if (dato > 0) {
                    datoString = new String(buffer, 0, dato);
                } else {
                    datoString = null;
                }

            }
        } catch (IOException e) {
            Log.d(TAG, "falla");
            e.printStackTrace();
            datoString = null;
        }

        return datoString;

    }

    public void escribirBytes(byte[] datoByteParaEnviar) {

        byte[] buffer = new byte[1024];

        if (datoByteParaEnviar != null)
            buffer = datoByteParaEnviar;

        if (flujoSalida != null) {
            try {

                flujoSalida.write(buffer);
                flujoSalida.flush();

            } catch (IOException e) {
            }

        }

    }

    public void cerrarSocketCliente() {

        if (clienteSocket != null) {
            try {

                clienteSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void cerrarFlujoEntrada() {

        if (flujoEntrada != null) {

            try {

                flujoEntrada.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void cerrarFlujoSalida() {

        if (flujoSalida != null) {
            try {

                flujoSalida.flush();
                flujoSalida.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}
