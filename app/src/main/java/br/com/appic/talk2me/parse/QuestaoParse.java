package br.com.appic.talk2me.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by vagnnermartins on 21/08/14.
 */
@ParseClassName("Questao")
public class QuestaoParse extends ParseObject {

    public String getTitulo(){
        return getString("titulo");
    }

    public PesquisaParse getPesquisa(){
        return (PesquisaParse) getParseObject("pesquisa");
    }

    public int getTipoQuestao(){
        return getInt("tipoQuestao");
    }
}
