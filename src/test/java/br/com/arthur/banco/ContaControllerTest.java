package br.com.arthur.banco;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContaControllerTest {

        private ContaService service;
        private ContaController controller;

        @BeforeEach
        void setUp() {
                ContaRepository repository = new ContaRepositoryEmMemoria();

                service = new ContaService(repository);

                controller = new ContaController(service);
        }

        @Test
        void deveBuscarConta() {
                ContaPoupanca conta = new ContaPoupanca(
                                "Arthur",
                                "001");

                conta.depositar(500.0);

                service.adicionarConta(conta);

                ContaResumo resultado = controller.buscarConta("001");

                assertEquals(
                                "001",
                                resultado.numero());

                assertEquals(
                                "Arthur",
                                resultado.titular());

                assertEquals(
                                500.0,
                                resultado.saldo(),
                                0.001);
        }

        @Test
        void deveListarContas() {
                service.adicionarConta(
                                new ContaCorrente(
                                                "Arthur",
                                                "001"));

                service.adicionarConta(
                                new ContaPoupanca(
                                                "Maria",
                                                "002"));

                List<ContaResumo> contas = controller.listarContas();

                assertEquals(
                                2,
                                contas.size());
        }

        @Test
        void deveTransferirEntreContas() {
                ContaPoupanca origem = new ContaPoupanca(
                                "Arthur",
                                "001");

                ContaPoupanca destino = new ContaPoupanca(
                                "Maria",
                                "002");

                origem.depositar(1000.0);

                service.adicionarConta(origem);
                service.adicionarConta(destino);

                TransferenciaRequest request = new TransferenciaRequest(
                                "001",
                                "002",
                                250.0);

                controller.transferir(request);

                assertEquals(
                                750.0,
                                origem.getSaldo(),
                                0.001);

                assertEquals(
                                250.0,
                                destino.getSaldo(),
                                0.001);
        }

        @Test
        void deveRetornar200AoBuscarContaExistente() {
                ContaPoupanca conta = new ContaPoupanca(
                                "Arthur",
                                "001");

                conta.depositar(500.0);

                service.adicionarConta(conta);

                RespostaHttp<ContaResumo> resposta = controller.buscarContaHttp("001");

                assertEquals(
                                200,
                                resposta.status());

                assertEquals(
                                "001",
                                resposta.corpo().numero());

                assertEquals(
                                "Arthur",
                                resposta.corpo().titular());
        }

        @Test
        void deveRetornar404AoBuscarContaInexistente() {
                RespostaHttp<ContaResumo> resposta = controller.buscarContaHttp("999");

                assertEquals(
                                404,
                                resposta.status());

                assertEquals(
                                null,
                                resposta.corpo());

                assertEquals(
                                "Conta não encontrada: 999",
                                resposta.erro());
        }

        @Test
        void deveRetornar201AoCriarConta() {
                CriarContaRequest request = new CriarContaRequest(
                                "Arthur",
                                "001",
                                TipoConta.CORRENTE);

                RespostaHttp<ContaResumo> resposta = controller.criarContaHttp(request);

                assertEquals(
                                201,
                                resposta.status());

                assertEquals(
                                "001",
                                resposta.corpo().numero());
        }

        @Test
        void deveRetornar409AoCriarContaDuplicada() {
                CriarContaRequest request = new CriarContaRequest(
                                "Arthur",
                                "001",
                                TipoConta.CORRENTE);

                controller.criarContaHttp(request);

                RespostaHttp<ContaResumo> resposta = controller.criarContaHttp(request);

                assertEquals(
                                409,
                                resposta.status());
        }
}