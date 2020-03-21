package br.com.ratacheski.bluefood.domain.pagamento;

import br.com.ratacheski.bluefood.domain.pedido.Pedido;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "bf_pagamento", schema = "bf")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pagamento implements Serializable {

    @Id
    private Long id;

    @OneToOne
    @NotNull
    @MapsId
    private Pedido pedido;

    @NotNull
    private LocalDateTime dataPagamento;

    @Column(name = "num_cartao_final")
    @NotNull
    @Size(min = 4, max = 4)
    private String numCartaoFinal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private BandeiraCartao bandeiraCartao;

    public void defNumeroEBandeira(String numCartao){
        numCartaoFinal = numCartao.substring(12);
        bandeiraCartao = obterBandeiraPorNumeroCartao(numCartao);
    }

    private BandeiraCartao obterBandeiraPorNumeroCartao(String numCartao){
        if(numCartao.startsWith("0000")){
            return BandeiraCartao.AMEX;
        } else if(numCartao.startsWith("1111")){
            return BandeiraCartao.MASTER;
        } else if(numCartao.startsWith("2222")){
            return BandeiraCartao.ELO;
        }
        return BandeiraCartao.VISA;
    }
}
