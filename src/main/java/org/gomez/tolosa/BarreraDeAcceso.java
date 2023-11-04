package org.gomez.tolosa;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class BarreraDeAcceso {
    private ArrayList<Aerosilla> aerosillas;
    private ArrayList<Esquiador> esquiadores;
    private Semaphore sEsquiadores, sAceesoSuperior;

    public BarreraDeAcceso(Semaphore sEsquiadores) {
        this.sEsquiadores = sEsquiadores;
        this.aerosillas = new ArrayList<>();
        this.esquiadores = new ArrayList<>();
        this.sAceesoSuperior = new Semaphore(1);
    }

    public synchronized void setAerosilla(Aerosilla aerosilla) {
        this.aerosillas.add(aerosilla);
    }


    public synchronized void deleteAerosilla(Aerosilla aerosilla) {
        this.aerosillas.remove(aerosilla);

    }


    public synchronized void subirAAerosilla(Esquiador esquiador) {
        this.esquiadores.add(esquiador);
        if (esquiadores.size() == 4) {
            comenzarRecorrido();
        } else {
            System.out.println("⏳\uD83C\uDFC2Esperar hasta que sean 4 esquiadores en la Aerosilla :" + aerosillas.get(0).getId());
        }
    }


    private synchronized void comenzarRecorrido() {
        if (esquiadores.isEmpty()) {
            throw new RuntimeException("No hay aerosillas disponibles para subir a los esquiadores");
        }
        System.out.println("Subiendo 4 esquiadores a la silla :" + aerosillas.get(0).getId());
        for (Esquiador e : esquiadores) {
            aerosillas.get(0).subirEsquiador(e);
        }
        esquiadores.clear();
        System.out.println("Ya subieron todos los esquiadores a la silla :" + aerosillas.get(0).getId());

    }

    public synchronized void bajarEsquiadores(Aerosilla aeroSilla) {
        try {
            sAceesoSuperior.acquire();
            ArrayList<Esquiador> esquiadorsDeAerosilla = new ArrayList<>();
            for (Esquiador e : aeroSilla.getEsquiadores()) {
                esquiadorsDeAerosilla.add(e);
            }
            for (Esquiador e : esquiadorsDeAerosilla) {
                aeroSilla.bajarEsquiadores(e);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void liberarAccesoDeAerosillas() {
        for (int i = 0; i < 4; i++) {
            sEsquiadores.release();
        }
    }


    public void liberarAccesoSuperior() {
        sAceesoSuperior.release();
    }

    public synchronized boolean hayAerosilla() {
        return aerosillas.isEmpty() ? false : true;
    }
}

