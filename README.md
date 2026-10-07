# Sistema Bancário em Java

Projeto de estudo para praticar conceitos fundamentais de Java e construir um sistema bancário simples sem frameworks de aplicação. O domínio e as regras de negócio são implementados com Java; bibliotecas externas são usadas para persistência, migrações e testes.

## Sobre o projeto

O sistema modela contas correntes e poupanças, operações bancárias, histórico de transações e consultas de contas. Os dados podem ser trabalhados por um repositório em memória ou persistidos em PostgreSQL.

O projeto não expõe uma API HTTP nem usa um framework web. `ContaController` é uma camada didática que recebe requisições como records e representa respostas com `RespostaHttp<T>`.

## O que foi praticado

### Java e orientação a objetos

- **Classes, objetos, atributos e métodos:** representação de contas e transações.
- **Encapsulamento e invariantes:** atributos privados e validações que protegem o estado das contas.
- **Abstração:** `ContaBancaria` define o comportamento comum às contas e deixa a tarifa de saque para as subclasses.
- **Herança e sobrescrita:** `ContaCorrente` e `ContaPoupanca` especializam a classe base.
- **Interfaces e polimorfismo:** `Rendivel` representa uma capacidade opcional de uma conta; o serviço opera com `ContaRepository`, sem depender diretamente de uma implementação concreta.
- **Composição:** contas mantêm seus históricos de `Transacao`.
- **Enums:** `TipoConta` e `TipoTransacao` limitam valores a opções conhecidas.
- **Imutabilidade e cópias defensivas:** identificadores e dados de transação são imutáveis, e o histórico é exposto como cópia não modificável.
- **Contrato de igualdade:** `equals` e `hashCode` de uma conta são baseados no número da conta.

### Recursos da linguagem e da biblioteca padrão

- **Records:** objetos de transporte e resumo, como `CriarContaRequest`, `TransferenciaRequest` e `ContaResumo`.
- **Generics:** tipos reutilizáveis como `Caixa<T>`, `RepositorioConta<T extends ContaBancaria>` e `RespostaHttp<T>`.
- **Coleções:** uso de `List`, `Map`, `HashMap` e `LinkedHashMap` para armazenar e organizar dados.
- **Streams e lambdas:** filtragem, transformação, ordenação, agregação e busca de contas.
- **`Optional`:** representação de buscas que podem não encontrar uma conta.
- **API de data e hora:** `LocalDate`, `LocalDateTime`, `Period`, `Duration` e `DateTimeFormatter`.
- **Manipulação de arquivos:** leitura e escrita usando `Path` e `Files`.
- **`try-with-resources`:** fechamento de recursos como conexões JDBC, statements, result sets e leitores.

### Regras de negócio e tratamento de erros

- Depósito, saque, transferência e rendimento de poupança.
- Tarifas diferentes de saque para cada tipo de conta.
- Verificação de saldo, validação de argumentos e prevenção de transferências para a própria conta.
- Exceções específicas para situações de negócio, como conta inexistente, conta duplicada e conta não rendível.
- Conversão de exceções em respostas modeladas com status HTTP na camada de controller.

### Organização e persistência

- **Separação de responsabilidades:** domínio, serviço, controller e repositório têm papéis distintos.
- **Inversão de dependência:** `ContaService` recebe a interface `ContaRepository` por construtor; assim, pode usar repositórios diferentes.
- **Persistência intercambiável:** `ContaRepositoryEmMemoria` facilita o uso sem banco, enquanto `ContaRepositoryPostgres` implementa a persistência em PostgreSQL.
- **JDBC:** conexões, consultas parametrizadas com `PreparedStatement`, leitura de `ResultSet` e mapeamento entre linhas e objetos Java.
- **Transações de banco:** agrupamento de gravações com `commit` e `rollback`, inclusive para salvar várias contas.
- **Migrações e integridade relacional:** scripts versionados criam tabelas, restrições, chaves estrangeiras e índices.
- **Configuração externa:** credenciais e URL do banco são lidas das variáveis de ambiente pela configuração da aplicação.

### Testes

- Testes unitários com **JUnit 5** para regras de negócio, serviços, controller, relatórios, arquivos e repositórios.
- Testes de integração do repositório PostgreSQL com **Testcontainers**, executando um banco em contêiner.
- Testes de cenários positivos, validações, erros, persistência de histórico e rollback.

## Organização dos arquivos

```text
src/
├── main/
│   ├── java/br/com/arthur/banco/   # Aplicação, domínio e camadas do sistema
│   └── resources/db/migration/     # Migrações versionadas do Flyway
└── test/
    └── java/br/com/arthur/banco/   # Testes unitários e de integração
database/
├── schema.sql                      # Esquema SQL de referência
└── seed.sql                        # Dados iniciais de exemplo
docker-compose.yml                  # PostgreSQL local
pom.xml                             # Configuração Maven e dependências
```

## Tecnologias

- Java 21
- Maven
- PostgreSQL e JDBC
- HikariCP para pool de conexões
- Flyway para migrações
- JUnit 5 e Testcontainers para testes
- Docker Compose para iniciar o PostgreSQL local

Essas bibliotecas não substituem o foco do projeto: as regras de negócio e as camadas da aplicação são escritas diretamente em Java, sem framework de aplicação.

## Como executar

É necessário ter Java 21 e Maven instalados.

### Executar a demonstração em memória

Execute a classe `br.com.arthur.banco.App` pela sua IDE. Essa demonstração instancia `ContaRepositoryEmMemoria`, então não precisa de PostgreSQL.

### Executar os testes

```bash
mvn test
```

Os testes de integração usam Testcontainers; portanto, é necessário ter Docker disponível para executar a suíte completa.

### Iniciar o PostgreSQL local

```bash
docker compose up -d
```

O serviço local usa a porta `5433` no host. Para os componentes que conectam ao banco, configure as variáveis `DB_URL`, `DB_USER` e `DB_PASSWORD`. As migrações Flyway são aplicadas quando `DatabaseMigration.migrate` é chamado. O arquivo `database/seed.sql` contém dados de exemplo e pode ser executado manualmente quando necessário.

## Observação

Este sistema é um exercício de aprendizado, não uma aplicação bancária pronta para uso real. Por exemplo, os valores monetários estão representados com `double`; sistemas financeiros normalmente precisam de uma representação decimal apropriada e de requisitos adicionais de segurança, concorrência e auditoria.
