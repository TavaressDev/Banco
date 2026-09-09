package br.com.arthur.banco;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Transacao {

    private final UUID id;
    private final TipoTransacao tipo;
    private final double valor;
    private final LocalDateTime dataHora;

    public Transacao(
            TipoTransacao tipo,
            double valor) {
        this(
                UUID.randomUUID(),
                tipo,
                valor,
                LocalDateTime.now());
    }

    public Transacao(
            TipoTransacao tipo,
            double valor,
            LocalDateTime dataHora) {
        this(
                UUID.randomUUID(),
                tipo,
                valor,
                dataHora);
    }

    private Transacao(
            UUID id,
            TipoTransacao tipo,
            double valor,
            LocalDateTime dataHora) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "ID da transação não pode ser nulo.");
        }

        if (tipo == null) {
            throw new IllegalArgumentException(
                    "Tipo de transação não pode ser nulo.");
        }

        if (valor <= 0) {
            throw new IllegalArgumentException(
                    "Valor da transação deve ser positivo.");
        }

        if (dataHora == null) {
            throw new IllegalArgumentException(
                    "Data e hora não podem ser nulas.");
        }

        this.id = id;
        this.tipo = tipo;
        this.valor = valor;
        this.dataHora = dataHora;
    }

    static Transacao reidratar(
            UUID id,
            TipoTransacao tipo,
            double valor,
            LocalDateTime dataHora) {
        return new Transacao(
                id,
                tipo,
                valor,
                dataHora);
    }

    public UUID getId() {
        return id;
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public double getValor() {
        return valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public LocalDate getData() {
        return dataHora.toLocalDate();
    }

    public String getDataHoraFormatada() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "dd/MM/yyyy HH:mm:ss");

        return dataHora.format(formatter);
    }
}