package br.com.ratacheski.bluefood.domain.pagamento;

public enum StatusPagamento {
    AUTORIZADO("Autorizado"),
    NAO_AUTORIZADO("Não Autorizado pela Instituição Financeira"),
    CARTAO_INVALIDO("Cartão Inválido ou Bloqueado");

    String descricao;

    StatusPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
