package br.com.arthur.banco;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ContaRepositoryEmMemoria
        implements ContaRepository {

    private final Map<String, ContaBancaria> contas = new HashMap<>();

    @Override
    public void salvar(ContaBancaria conta) {
        contas.put(
                conta.getNumero(),
                conta);
    }

    @Override
    public Optional<ContaBancaria> buscarPorNumero(
            String numero) {
        return Optional.ofNullable(
                contas.get(numero));
    }

    @Override
    public List<ContaBancaria> listarTodas() {
        return List.copyOf(
                contas.values());
    }

    @Override
    public void salvarTodas(List<ContaBancaria> contas) {
        for (ContaBancaria conta : contas) {
            salvar(conta);
        }
    }
}