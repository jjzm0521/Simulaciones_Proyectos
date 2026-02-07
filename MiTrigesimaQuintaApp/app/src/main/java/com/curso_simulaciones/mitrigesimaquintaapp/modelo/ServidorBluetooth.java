package com.curso_simulaciones.mitrigesimaquintaapp.modelo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import com.curso_simulaciones.mitrigesimaquintaapp.AlmacenDatosRAM;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.UUID;

public class ServidorBluetooth {

    public static final String NOMBRE_SEGURO = "BluetoothServiceSecure";
    public BluetoothAdapter adaptadorBluetooth;
    public BluetoothServerSocket serverSocket;
    public BluetoothSocket clienteSocket;

    private BufferedInputStream flujoEntrada;
    private BufferedOutputStream flujoSalida;

    public static final UUID UUID_SEGURO = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public ServidorBluetooth() {

        adaptadorBluetooth = BluetoothAdapter.getDefaultAdapter();

    }

    public void abrirSocketServidor() {

        if (serverSocket != null)
            serverSocket = null;

        try {
            serverSocket = adaptadorBluetooth.listenUsingRfcommWithServiceRecord(NOMBRE_SEGURO, UUID_SEGURO);

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public void abrirSocketCliente() {

        try {

            clienteSocket = serverSocket.accept();
            AlmacenDatosRAM.conexion_bluetooth = " Conectado con  "
                    + clienteSocket.getRemoteDevice().getName().toString();

        } catch (IOException e) {
            AlmacenDatosRAM.conexion_bluetooth = "Falló la conexión";
            e.printStackTrace();

        }

    }

    public void abrirFlujoEntrada() {

        try {

            flujoEntrada = new BufferedInputStream(clienteSocket.getInputStream());

        } catch (IOException e) {

        }

    }

    public void abrirFlujoSalida() {

        try {

            flujoSalida = new BufferedOutputStream(clienteSocket.getOutputStream());

        } catch (IOException e) {

        }

    }

    public byte[] leerBytes() {

        int nuevoDato = 0;
        byte[] buffer = new byte[1024];

        byte[] datoBytesRecibido = null;

        if (flujoEntrada != null) {

            try {

                nuevoDato = flujoEntrada.read(buffer);
                if (nuevoDato > 0) {
                    datoBytesRecibido = (new String(buffer, 0, nuevoDato)).getBytes();
                }

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

        return datoBytesRecibido;

    }

    public void escribirBytes(byte[] datoByteParaEnviar) {

        int size = datoByteParaEnviar.length;

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

    public void cerrarSocketCliente() {

        if (clienteSocket != null) {
            try {

                clienteSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
