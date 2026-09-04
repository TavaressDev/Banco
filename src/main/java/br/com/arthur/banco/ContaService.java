package br.com.arthur.banco;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ContaService {

        private final ContaRepository repository;

        public ContaService(ContaRepository repository) {
                if (repository == null) {
                        throw new IllegalArgumentException(
                                        "O repositório não pode ser nulo.");
                }

                this.repository = repository;
        }

        public void adicionarConta(ContaBancaria conta) {
                if (conta == null) {
                        throw new IllegalArgumentException(
                                        "A conta não pode ser nula.");
                }

                if (repository
                                .buscarPorNumero(conta.getNumero())
                                .isPresent()) {

                        throw new ContaJaExisteException(
                                        "Já existe uma conta com o número: "
                                                        + conta.getNumero());
                }

                repository.salvar(conta);
        }

        public ContaBancaria buscarContaPorNumero(String numero) {
                if (numero == null || numero.isBlank()) {
                        throw new IllegalArgumentException(
                                        "O número da conta não pode ser nulo ou vazio.");
                }

                return repository.buscarPorNumero(numero)
                                .orElseThrow(
                                                () -> new ContaNaoEncontradaException(
                                                                "Conta não encontrada: " + numero));
        }

        public Optional<ContaBancaria> buscarContaOpcional(String numero) {
                if (numero == null || numero.isBlank()) {
                        throw new IllegalArgumentException(
                                        "O número da conta não pode ser nulo ou vazio.");
                }

                return repository.buscarPorNumero(numero);
        }

        public void transferir(
                        String numeroOrigem,
                        String numeroDestino,
                        double valor) {

                ContaBancaria contaOrigem = buscarContaPorNumero(numeroOrigem);

                ContaBancaria contaDestino = buscarContaPorNumero(numeroDestino);

                contaOrigem.transferir(
                                contaDestino,
                                valor);

                repository.salvar(contaOrigem);
                repository.salvar(contaDestino);
        }

        public void sacar(
                        String numero,
                        double valor) {

                ContaBancaria conta = buscarContaPorNumero(numero);

                conta.sacar(valor);

                repository.salvar(conta);
        }

        public void aplicarRendimento(
                        String numero,
                        double percentual) {

                ContaBancaria conta = buscarContaPorNumero(numero);

                if (!(conta instanceof Rendivel rendivel)) {
                        throw new ContaNaoRendivelException(
                                        "A conta não é rendível.");
                }

                rendivel.aplicarRendimento(percentual);

                repository.salvar(conta);
        }

        public List<ContaBancaria> listarContas() {
                return repository.listarTodas();
        }

        public List<ContaBancaria> buscarContasComSaldoMaiorQue(
                        double valor) {

                return repository.listarTodas()
                                .stream()
                                .filter(conta -> conta.getSaldo() > valor)
                                .toList();
        }

        public List<String> listarNumerosDasContas() {
                return repository.listarTodas()
                                .stream()
                                .map(ContaBancaria::getNumero)
                                .toList();
        }

        public List<String> listarNumerosDasContasComSaldoMaiorQue(
                        double valor) {

                return repository.listarTodas()
                                .stream()
                                .filter(conta -> conta.getSaldo() > valor)
                                .map(ContaBancaria::getNumero)
                                .toList();
        }

        public double calcularSaldoTotal() {
                return repository.listarTodas()
                                .stream()
                                .map(ContaBancaria::getSaldo)
                                .reduce(0.0, Double::sum);
        }

        public List<ContaBancaria> listarContasOrdenadasPorSaldo() {
                return repository.listarTodas()
                                .stream()
                                .sorted(
                                                Comparator.comparingDouble(
                                                                ContaBancaria::getSaldo))
                                .toList();
        }

        public ContaBancaria buscarPrimeiraContaComSaldoMaiorQue(
                        double valor) {

                return repository.listarTodas()
                                .stream()
                                .filter(conta -> conta.getSaldo() > valor)
                                .findFirst()
                                .orElseThrow(
                                                () -> new ContaNaoEncontradaException(
                                                                "Nenhuma conta encontrada com saldo maior que "
                                                                                + valor
                                                                                + "."));
        }

        public boolean existeContaComSaldoMaiorQue(double valor) {
                return repository.listarTodas()
                                .stream()
                                .anyMatch(
                                                conta -> conta.getSaldo() > valor);
        }

        public long contarContasComSaldoMaiorQue(double valor) {
                return repository.listarTodas()
                                .stream()
                                .filter(conta -> conta.getSaldo() > valor)
                                .count();
        }

        public List<ContaResumo> listarResumosDasContas() {
                return repository.listarTodas()
                                .stream()
                                .map(conta -> new ContaResumo(
                                                conta.getNumero(),
                                                conta.getTitular(),
                                                conta.getSaldo()))
                                .toList();
        }

        public Optional<String> buscarTitularDaConta(
                        String numero) {

                return buscarContaOpcional(numero)
                                .map(ContaBancaria::getTitular);
        }

        public ContaResumo criarConta(
                        CriarContaRequest request) {
                ContaBancaria conta;

                if (request.tipo() == TipoConta.CORRENTE) {
                        conta = new ContaCorrente(
                                        request.titular(),
                                        request.numero());
                } else {
                        conta = new ContaPoupanca(
                                        request.titular(),
                                        request.numero());
                }

                adicionarConta(conta);

                return new ContaResumo(
                                conta.getNumero(),
                                conta.getTitular(),
                                conta.getSaldo());
        }
}