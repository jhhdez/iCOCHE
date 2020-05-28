package com.example.icoche;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class Menu {

    String funcionalidad;
    String descripcion;
    int imagen;

    public Menu(String funcionalidad, String descripcion, int imagen) {
        this.funcionalidad = funcionalidad;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    public void setFuncionalidad(String funcionalidad) {
        this.funcionalidad = funcionalidad;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getFuncionalidad() {
        return funcionalidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getImagen() {
        return imagen;
    }
}
