package org.gomez.tolosa;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {


        Semaphore sEsquiadores = new Semaphore(4);
        BarreraDeAcceso barreraDeAccesoInferior = new BarreraDeAcceso(sEsquiadores);
        BarreraDeAcceso barreraDeAccesoSuperior = new BarreraDeAcceso(sEsquiadores);

        ArrayList<Thread> esquiadoresThreads = new ArrayList<>();
        ArrayList<Thread> aerosillasThreads = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            Thread t = new Thread(new Esquiador(i, sEsquiadores, barreraDeAccesoInferior));
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