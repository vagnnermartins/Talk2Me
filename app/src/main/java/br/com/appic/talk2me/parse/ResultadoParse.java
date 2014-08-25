package br.com.appic.talk2me.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by vagnnermartins on 22/08/14.
 */
@ParseClassName("Resultado")
public class ResultadoParse extends ParseObject {

    public RespostaParse getResposta(){
        return (RespostaParse) getParseObject("resposta");
    }

    public void setResposta(RespostaParse resposta){
        put("resposta", resposta);
    }

    public EntrevistaParse getEntrevista(){
        return (EntrevistaParse) getParseObject("entrevista");
    }

    public void setEntrevista(EntrevistaParse entrevista){
        put("entrevista", entrevista);
    }
}
