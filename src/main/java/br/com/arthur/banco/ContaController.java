package br.com.arthur.banco;

import java.util.List;

public class ContaController {

    private final ContaService service;

    public ContaController(ContaService service) {
        if (service == null) {
            throw new IllegalArgumentException(
                    "O service não pode ser nulo.");
        }

        this.service = service;
    }

    public ContaResumo buscarConta(String numero) {
        ContaBancaria conta = service.buscarContaPorNumero(numero);

        return new ContaResumo(
                conta.getNumero(),
                conta.getTitular(),
                conta.getSaldo());
    }

    public List<ContaResumo> listarContas() {
        return service.listarResumosDasContas();
    }

    public void transferir(
            TransferenciaRequest request) {
        if (request == null) {
            throw new IllegalArgumentException(
                    "A requisição não pode ser nula.");
        }

        service.transferir(
                request.contaOrigem(),
                request.contaDestino(),
                request.valor());
    }

    public ContaResumo criarConta(
            CriarContaRequest request) {
        if (request == null) {
            throw new IllegalArgumentException(
                    "A requisição não pode ser nula.");
        }

        return service.criarConta(request);
    }

    public RespostaHttp<ContaResumo> buscarContaHttp(
            String numero) {
        try {
            ContaResumo conta = buscarConta(numero);

            return new RespostaHttp<>(
                    200,
                    conta,
                    null);

        } catch (ContaNaoEncontradaException e) {

            return new RespostaHttp<>(
                    404,
                    null,
                    e.getMessage());

        } catch (IllegalArgumentException e) {

            return new RespostaHttp<>(
                    400,
                    null,
                    e.getMessage());
        }
    }

    public RespostaHttp<ContaResumo> criarContaHttp(
            CriarContaRequest request) {
        try {
            ContaResumo conta = criarConta(request);

            return new RespostaHttp<>(
                    201,
                    conta,
                    null);

        } catch (ContaJaExisteException e) {

            return new RespostaHttp<>(
                    409,
                    null,
                    e.getMessage());

        } catch (IllegalArgumentException e) {

            return new RespostaHttp<>(
                    400,
                    null,
                    e.getMessage());
        }
    }
}