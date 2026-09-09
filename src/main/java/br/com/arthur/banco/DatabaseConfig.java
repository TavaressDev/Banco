package br.com.arthur.banco;

public class DatabaseConfig {

    private final String url;
    private final String usuario;
    private final String senha;

    public DatabaseConfig(
            String url,
            String usuario,
            String senha) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException(
                    "URL do banco não pode ser vazia.");
        }

        if (usuario == null || usuario.isBlank()) {
            throw new IllegalArgumentException(
                    "Usuário do banco não pode ser vazio.");
        }

        if (senha == null || senha.isBlank()) {
            throw new IllegalArgumentException(
                    "Senha do banco não pode ser vazia.");
        }

        this.url = url;
        this.usuario = usuario;
        this.senha = senha;
    }

    public static DatabaseConfig fromEnvironment() {

        return new DatabaseConfig(
                System.getenv("DB_URL"),
                System.getenv("DB_USER"),
                System.getenv("DB_PASSWORD"));
    }

    public String getUrl() {
        return url;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getSenha() {
        return senha;
    }
}