package org.gomez.tolosa;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {


        Semaphore sEsquiadores = new Semaphore(4);
        Semaphore sAerosilla = new Semaphore(0);
        BarreraDeAcceso barreraDeAccesoInferior = new BarreraDeAcceso(sEsquiadores, sAerosilla);
        BarreraDeAcceso barreraDeAccesoSuperior = new BarreraDeAcceso(sEsquiadores, sAerosilla);


        ArrayList<Thread> esquiadoresThreads = new ArrayList<>();
        ArrayList<Thread> aerosillasThreads = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            Thread t = new Thread(new Esquiador(i, sEsquiadores, sAerosilla, barreraDeAccesoInferior));
            esquiadoresThreads.add(t);
            t.start();
        }


        for (int i = 0; i < 2; i++) {
            Thread t = new Thread(new Aerosilla(i, barreraDeAccesoInferior, barreraDeAccesoSuperior));
            aerosillasThreads.add(t);
            t.start();
        }

        for (Thread t : esquiadoresThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        //este for no se va a dar porque las aerosillas quedan en un bucle infinito en el run
        for (Thread t : aerosillasThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}