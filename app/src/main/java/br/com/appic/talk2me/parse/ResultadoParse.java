package br.com.appic.talk2me.parse;

import com.parse.ParseObject;

/**
 * Created by vagnnermartins on 22/08/14.
 */
public class ResultadoParse extends ParseObject {

    public RespostaParse getResposta(){
        return (RespostaParse) getParseObject("resposta");
    }

    public EntrevistaParse getEntrevista(){
        return (EntrevistaParse) getParseObject("entrevista");
    }
}
