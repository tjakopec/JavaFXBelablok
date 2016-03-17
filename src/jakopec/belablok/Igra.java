/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jakopec.belablok;

/**
 *
 * @author tjakopec
 */
public class Igra {

    private int igraMi;
    private int igraVi;
    private int zvanjeMi;
    private int zvanjeVi;

    public Igra() {

    }

    public Igra(int i) {
        igraMi = i;
        igraVi = i;
        zvanjeMi = i;
        zvanjeVi = i;
    }

    public int getIgraMi() {
        return igraMi;
    }

    public void setIgraMi(int igraMi) {
        this.igraMi = igraMi;
    }

    public int getIgraVi() {
        return igraVi;
    }

    public void setIgraVi(int igraVi) {
        this.igraVi = igraVi;
    }

    public int getZvanjeMi() {
        return zvanjeMi;
    }

    public void setZvanjeMi(int zvanjeMi) {
        this.zvanjeMi = zvanjeMi;
    }

    public int getZvanjeVi() {
        return zvanjeVi;
    }

    public void setZvanjeVi(int zvanjeVi) {
        this.zvanjeVi = zvanjeVi;
    }

    public int getUkupnoMi() {
        return this.igraMi + this.zvanjeMi;
    }

    public int getUkupnoVi() {
        return this.igraVi + this.zvanjeVi;
    }

}
