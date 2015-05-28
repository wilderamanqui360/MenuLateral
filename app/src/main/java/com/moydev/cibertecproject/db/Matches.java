package com.moydev.cibertecproject.db;

import com.orm.SugarRecord;

/**
 * Created by ATTAKON on 5/23/15.
 */
public class Matches extends SugarRecord<Matches> {
    String fecha;
    String hora;
    String ciudad;
    String local;
    String visitor;

    public Matches() {}

    public Matches(String fecha, String hora, String ciudad, String local, String visitor) {
        this.fecha = fecha;
        this.hora = hora;
        this.ciudad = ciudad;
        this.local = local;
        this.visitor = visitor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getVisitor() {
        return visitor;
    }

    public void setVisitor(String visitor) {
        this.visitor = visitor;
    }
}