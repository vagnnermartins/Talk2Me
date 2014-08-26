package br.com.appic.talk2me.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;

/**
 * Created by vagnnermartins on 21/08/14.
 */
@ParseClassName("Pesquisa")
public class PesquisaParse extends ParseObject implements Serializable{

    private String titulo;

    public String getTitulo(){
        return getString("titulo");
    }

    public EmpresaParse getEmpresa(){
        return (EmpresaParse) getParseObject("empresa");
    }

    @Override
    public String toString() {
        return getObjectId() + " " + getTitulo();
    }
}
