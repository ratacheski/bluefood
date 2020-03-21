package br.com.ratacheski.bluefood.domain.pagamento;


import javax.validation.constraints.Pattern;

public class DadosCartao {

    @Pattern(regexp = "\\d{16}", message = "Número do Cartão é Inválido!")
    private String numCartao;

    public String getNumCartao() {
        return numCartao;
    }

    public void setNumCartao(String numCartao) {
        this.numCartao = numCartao;
    }
}
