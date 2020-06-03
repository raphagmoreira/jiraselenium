package br.com.emailversaoautomatico.enums;

public enum EnumQuadro {

    SPRINT_BACKLOG(10),
    DESENVOLVIMENTO(11),
    PARA_CODE_REVIEW(92),
    CODE_REVIEW(43),
    PENDENTE_VERSAO_QA(303),
    PARA_TESTE(95),
    TESTE(45),
    AGUARDANDO_DEPLOY(44),
    ITENS_CONCLUIDOS(12);

    private Integer id;

    EnumQuadro(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

}
