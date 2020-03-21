package br.com.ratacheski.bluefood.application.service;

import br.com.ratacheski.bluefood.domain.pagamento.DadosCartao;
import br.com.ratacheski.bluefood.domain.pagamento.Pagamento;
import br.com.ratacheski.bluefood.domain.pagamento.PagamentoRepository;
import br.com.ratacheski.bluefood.domain.pagamento.StatusPagamento;
import br.com.ratacheski.bluefood.domain.pedido.*;
import br.com.ratacheski.bluefood.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Value("${bluefood.sbpay.url}")
    private String sbPayUrl;

    @Value("${bluefood.sbpay.token}")
    private String sbPayToken;
    @Autowired
    private PagamentoRepository pagamentoRepository;

    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = PagamentoException.class)
    public Pedido criarEPagar(Carrinho carrinho, String numCartao) throws PagamentoException {
        Pedido pedido = new Pedido();
        pedido.setData(LocalDateTime.now());
        pedido.setCliente(SecurityUtils.sessionCliente());
        pedido.setRestaurante(carrinho.getRestaurante());
        pedido.setStatusPedido(StatusPedido.PRODUCAO);
        pedido.setTaxaEntrega(carrinho.getRestaurante().getTaxaEntrega());
        pedido.setSubTotal(carrinho.getPrecoTotal(false));
        pedido.setTotal(carrinho.getPrecoTotal(true));
        pedido = pedidoRepository.save(pedido);
        int ordem = 1;
        for (ItemPedido itemPedido : carrinho.getItens()) {
            itemPedido.setId(new ItemPedidoPK(pedido, ordem++));
            itemPedidoRepository.save(itemPedido);
        }

        DadosCartao dadosCartao = new DadosCartao();
        dadosCartao.setNumCartao(numCartao);
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add("Token", sbPayToken);

        HttpEntity<DadosCartao> entity = new HttpEntity<>(dadosCartao, header);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> response;
        try {
            response = restTemplate.postForObject(sbPayUrl, entity, Map.class);
        } catch (Exception e) {
            throw new PagamentoException("Erro no servidor de pagamento");
        }
        StatusPagamento statusPagamento = StatusPagamento.valueOf(response.get("statusPagamento"));
        if (statusPagamento != StatusPagamento.AUTORIZADO) {
            throw new PagamentoException(statusPagamento.getDescricao());
        }
        Pagamento pagamento = new Pagamento();
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamento.setPedido(pedido);
        pagamento.defNumeroEBandeira(numCartao);
        pagamentoRepository.save(pagamento);
        return pedido;
    }
}
