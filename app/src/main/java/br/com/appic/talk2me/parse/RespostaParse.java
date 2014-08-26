package br.com.appic.talk2me.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by vagnnermartins on 22/08/14.
 */
@ParseClassName("Resposta")
public class RespostaParse extends ParseObject {

    public AlternativaParse getResposta(){
        return (AlternativaParse) getParseObject("alternativa");
    }

    public void setResposta(AlternativaParse resposta){
        put("alternativa", resposta);
    }

    public EntrevistaParse getEntrevista(){
        return (EntrevistaParse) getParseObject("entrevista");
    }

    public void setEntrevista(EntrevistaParse entrevista){
        put("entrevista", entrevista);
    }
}
