package br.com.arthur.banco;

import java.util.List;
import java.util.Optional;

public interface ContaRepository {

    void salvar(ContaBancaria conta);

    Optional<ContaBancaria> buscarPorNumero(String numero);

    List<ContaBancaria> listarTodas();
}