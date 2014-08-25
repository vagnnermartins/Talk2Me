package br.com.appic.talk2me.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by vagnnermartins on 21/08/14.
 */
@ParseClassName("Empresa")
public class EmpresaParse extends ParseObject {

    public String getNome(){
        return getString("nome");
    }
}
