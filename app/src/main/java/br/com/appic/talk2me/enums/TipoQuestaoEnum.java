package br.com.appic.talk2me.enums;

/**
 * Created by vagnnermartins on 25/08/14.
 */
public enum TipoQuestaoEnum {
    MULTIPLA_ESCOLHA(1),
    MULTIPLA_RESPOSTAS(2),
    DISSERTATIVA(3);

    private final int tipoQuestao;

    private TipoQuestaoEnum(int tipoQuestao) {
        this.tipoQuestao = tipoQuestao;
    }

    public int getTipoQuestao() {
        return tipoQuestao;
    }
}
