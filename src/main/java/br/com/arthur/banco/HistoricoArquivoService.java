package br.com.arthur.banco;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class HistoricoArquivoService {

    public void exportar(
        ContaBancaria conta,
        Path caminho
    )  {

        if (conta == null) {
            throw new IllegalArgumentException(
                "A conta não pode ser nula."
            );
        }

        if (caminho == null) {
            throw new IllegalArgumentException(
                "O caminho não pode ser nulo."
            );
        }

        List<String> linhas = conta.getTransacaosHistorico()
            .stream()
            .map(transacao ->
                transacao.getTipo()
                    + ";"
                    + transacao.getValor()
                    + ";"
                    + transacao.getDataHoraFormatada()
            )
            .toList();
            try {
                Files.write(caminho, linhas);
            } catch (IOException e) {
                throw new ExportacaoException(
                    "Erro ao exportar o histórico da conta.",
                    e
                );
            }
    }

    public List<String> lerHistorico(Path caminho) {
        if (caminho == null) {
            throw new IllegalArgumentException(
                "O caminho não pode ser nulo."
                );
            }

            try (
                BufferedReader reader =
                    Files.newBufferedReader(caminho)
            ) {
                return reader.lines().toList();

            } catch (IOException e) {
                throw new ExportacaoException(
                    "Não foi possível ler o histórico.",
                    e
                );
            }
    }
}