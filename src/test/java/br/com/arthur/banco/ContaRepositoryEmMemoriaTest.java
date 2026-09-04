package br.com.arthur.banco;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class ContaRepositoryEmMemoriaTest {

    @Test
    void deveSalvarEBuscarConta() {
        ContaRepository repository = new ContaRepositoryEmMemoria();

        ContaCorrente conta = new ContaCorrente(
                "Arthur",
                "001");

        repository.salvar(conta);

        Optional<ContaBancaria> resultado = repository.buscarPorNumero("001");

        assertTrue(resultado.isPresent());
        assertSame(
                conta,
                resultado.orElseThrow());
    }

    @Test
    void deveRetornarOptionalVazioQuandoContaNaoExistir() {
        ContaRepository repository = new ContaRepositoryEmMemoria();

        Optional<ContaBancaria> resultado = repository.buscarPorNumero("999");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveListarContasSalvas() {
        ContaRepository repository = new ContaRepositoryEmMemoria();

        repository.salvar(
                new ContaCorrente(
                        "Arthur",
                        "001"));

        repository.salvar(
                new ContaPoupanca(
                        "Maria",
                        "002"));

        assertEquals(
                2,
                repository.listarTodas().size());
    }
}