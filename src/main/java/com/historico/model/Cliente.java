package com.historico.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String nombre;
    private String apellido1;
    private String apellido2;
    private String comercialPrincipal;
    private long idEmpresa;

    @ElementCollection
    private List<Date> fechasVisitas;

    public Cliente() {
        this.fechasVisitas = new ArrayList<>();
    }

    public Cliente(String nombre, String apellido1, String apellido2,
                   String comercialPrincipal, long idEmpresa) {
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.comercialPrincipal = comercialPrincipal;
        this.idEmpresa = idEmpresa;
        this.fechasVisitas = new ArrayList<>();
    }

    public long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido1() { return apellido1; }
    public void setApellido1(String apellido1) { this.apellido1 = apellido1; }
    public String getApellido2() { return apellido2; }
    public void setApellido2(String apellido2) { this.apellido2 = apellido2; }
    public String getComercialPrincipal() { return comercialPrincipal; }
    public void setComercialPrincipal(String comercialPrincipal) { this.comercialPrincipal = comercialPrincipal; }
    public long getIdEmpresa() { return idEmpresa; }
    public void setIdEmpresa(long idEmpresa) { this.idEmpresa = idEmpresa; }
    public List<Date> getFechasVisitas() { return fechasVisitas; }
    public void setFechasVisitas(List<Date> fechasVisitas) { this.fechasVisitas = fechasVisitas; }
    public void agregarVisita(Date fecha) { this.fechasVisitas.add(fecha); }

    @Override
    public String toString() {
        return "Cliente{id=" + id + ", nombre='" + nombre + "', apellido1='" + apellido1 +
               "', apellido2='" + apellido2 + "', comercial='" + comercialPrincipal +
               "', idEmpresa=" + idEmpresa + ", visitas=" + fechasVisitas + '}';
    }
}