package br.com.appic.talk2me.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by vagnnermartins on 21/08/14.
 */
@ParseClassName("Entrevista")
public class EntrevistaParse extends ParseObject {

    public void setPesquisa(PesquisaParse pesquisa){
        put("pesquisa", pesquisa);
    }

    public PesquisaParse getPesquisa(){
        return (PesquisaParse) getParseObject("pesquisa");
    }

    public void setInicio(Date inicio){
        put("inicio", inicio);
    }

    public Date getInicio(){
        return getDate("inicio");
    }

    public void setFim(Date fim){
        put("fim", fim);
    }

    public Date getFim(){
        return getDate("fim");
    }
}
