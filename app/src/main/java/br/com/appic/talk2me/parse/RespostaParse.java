package br.com.appic.talk2me.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by vagnnermartins on 21/08/14.
 */
@ParseClassName("Resposta")
public class RespostaParse extends ParseObject {

    public String getTitulo(){
        return getString("titulo");
    }
    public QuestaoParse getQuestao(){
        return (QuestaoParse) getParseObject("questao");
    }
}
