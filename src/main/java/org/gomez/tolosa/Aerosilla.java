package org.gomez.tolosa;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Aerosilla implements Runnable {

    private int id;
    private int contEquiadores;
    private ArrayList<Esquiador> esquiadores;
    private BarreraDeAcceso barreraDeAccesoInferior, barreraDeAccesoSuperior;
    private Semaphore sAerosilla;

    public Aerosilla(int id, BarreraDeAcceso barreraDeAccesoInferior, BarreraDeAcceso barreraDeAccesoSuperior) {
        this.id = id;

        this.barreraDeAccesoInferior = barreraDeAccesoInferior;
        this.barreraDeAccesoSuperior = barreraDeAccesoSuperior;
        this.sAerosilla = new Semaphore(0);
        this.esquiadores = new ArrayList<>();
        this.contEquiadores = 0;

    }

    int cont = 0;

    @Override
    public void run() {
        barreraDeAccesoInferior.setAerosilla(this);

        while (true) {

            try {
                sAerosilla.acquire();
                barreraDeAccesoInferior.deleteAerosilla(this);
                subirMontaña();
                barreraDeAccesoSuperior.bajarEsquiadores(this);
                bajarMontaña();
                barreraDeAccesoInferior.setAerosilla(this);
                cont++;
                System.out.println("La AeroSilla :" + id + " ya realizo " + cont + " viajes de ida y vuelta");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        }

    }


    public synchronized void subirEsquiador(Esquiador esquiador) {
        try {
            if (contEquiadores == 4) {
                throw new RuntimeException("no pueden subir mas de 4 esquiadores");
            }
            esquiadores.add(esquiador);
            contEquiadores++;
            System.out.println("El esquiador : " + esquiador.getId() + " subió de la Aerosilla :" + this.id);
            if (contEquiadores == 4) {
                sAerosilla.release();
            }
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void bajarEsquiadores(Esquiador esquiador) {
        try {

            if (contEquiadores == 0) {
                throw new RuntimeException("no hay esquiadores para bajar");
            }
            esquiadores.remove(esquiador);
            contEquiadores--;
            System.out.println("\uD83C\uDFC2 El esquiador : " + esquiador.getId() + " bajó de la Aerosilla :" + this.id);
            Thread.sleep(10);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized void subirMontaña() {
        try {

            System.out.println("\uD83D\uDEA0\uD83C\uDFD4Aerosilla :" + this.id + " subiendo montaña...");
            barreraDeAccesoInferior.liberarAccesoDeAerosillas();
            Thread.sleep(5000);
            System.out.println("\uD83C\uDFD4La aerosilla :" + this.id + " llego a la cima de la montaña");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized void bajarMontaña() {
        try {
            System.out.println("\uD83D\uDEA0Aerosilla :" + this.id + " bajando montaña...");
            barreraDeAccesoSuperior.liberarAccesoSuperior();
            Thread.sleep(5000);
            System.out.println("La aerosilla :" + this.id + " llego a la base de la montaña");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Esquiador> getEsquiadores() {
        return esquiadores;
    }

    public int getId() {
        return id;
    }


}
