package org.gomez.tolosa;

import java.util.concurrent.Semaphore;

public class Esquiador implements Runnable {
    private Semaphore sEsquiadores;
    private int id;

    private BarreraDeAcceso barreraDeAcceso;
    private Semaphore sAerosilla;

    public Esquiador(int id, Semaphore sEsquiadores, Semaphore sAerosilla, BarreraDeAcceso barreraDeAcceso) {
        this.id = id;
        this.sEsquiadores = sEsquiadores;
        this.sAerosilla = sAerosilla;
        this.barreraDeAcceso = barreraDeAcceso;

    }


    @Override
    public void run() {

        try {
            System.out.println("\uD83E\uDDCD\u200D♂️\uD83E\uDDCD\u200D♂️Llega  el esquiador " + id + ", y espera en la fila");
            sEsquiadores.acquire();//solo pueden ingresar los 4 esquiadores a la Aerosilla
            System.out.println("\uD83D\uDEB6\u200D♂️El esquiador " + id + ", avanza y espera a que lo dejen subir");

            if (!barreraDeAcceso.hayAerosilla()) {
                System.out.println("⏳ Esperando que llegue una Aerosilla");
            }
            sAerosilla.acquire();//esperar hasta que llegue una aerosilla
            barreraDeAcceso.subirAAerosilla(this);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Esquiador{" +
                ", id=" + id +
                '}';
    }
}
