# 🔧 Use & Devolva

<p align="center">
  <strong>Plataforma colaborativa para compartilhamento e aluguel de ferramentas entre usuários.</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white" alt="Thymeleaf">
  <img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white" alt="HTML5">
  <img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white" alt="CSS3">
  <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black" alt="JavaScript">
</p>

---

## 📖 Sobre o projeto

O **Use & Devolva** é uma plataforma web desenvolvida com o objetivo de facilitar o **compartilhamento e o aluguel de ferramentas entre usuários**.

A proposta é conectar pessoas que possuem ferramentas disponíveis com pessoas que precisam utilizá-las por determinado período, tornando esse processo mais simples, organizado e colaborativo.

A aplicação reúne em um único ambiente recursos de autenticação, localização de ferramentas, gerenciamento de aluguéis, comunicação entre usuários e pagamentos.

O projeto foi desenvolvido como **Trabalho de Conclusão de Curso (TCC)** no **Senac São Paulo**, durante o curso de Desenvolvimento de Sistemas.

---

## 🎯 Objetivo

Muitas ferramentas possuem um custo elevado e são utilizadas apenas ocasionalmente. Ao mesmo tempo, diversas pessoas possuem equipamentos que permanecem sem utilização durante grande parte do tempo.

O **Use & Devolva** foi criado para aproximar esses dois públicos.

A plataforma permite que usuários disponibilizem ferramentas e que outros usuários encontrem opções para aluguel, favorecendo o acesso aos equipamentos sem a necessidade de aquisição definitiva.

---

## ✨ Principais funcionalidades

- 🔐 Autenticação de usuários
- 👤 Gerenciamento de usuários
- 🔧 Disponibilização de ferramentas para aluguel
- 🔎 Pesquisa de ferramentas
- 📍 Busca utilizando geolocalização
- 📅 Gerenciamento do processo de aluguel
- 💬 Chat entre usuários
- 💳 Pagamentos via PIX ou cartão
- 🗄️ Persistência das informações em banco de dados PostgreSQL
- 🌐 Interface web integrada ao backend
- 📱 Interface desenvolvida para facilitar a navegação do usuário

---

## 🔄 Fluxo básico da plataforma

O funcionamento da aplicação pode ser resumido da seguinte forma:

```text
Usuário
   │
   ▼
Cadastro / Login
   │
   ▼
Busca por ferramenta
   │
   ├── Localização
   ├── Disponibilidade
   └── Informações do anúncio
   │
   ▼
Contato entre usuários
   │
   ▼
Solicitação de aluguel
   │
   ▼
Pagamento
   │
   ▼
Utilização da ferramenta
   │
   ▼
Devolução
```

---

## 🛠️ Tecnologias utilizadas

### Backend

- **Java**
- **Spring Boot**
- **Spring MVC**
- **Spring Data**
- **Maven**

### Frontend

- **HTML5**
- **CSS3**
- **JavaScript**
- **Thymeleaf**

### Banco de dados

- **PostgreSQL**

### Recursos da aplicação

- Autenticação de usuários
- Geolocalização
- Sistema de aluguel
- Chat
- Integração com pagamentos

---

## 🏗️ Arquitetura

A aplicação utiliza uma arquitetura baseada no ecossistema **Spring Boot**, separando as responsabilidades da aplicação entre as diferentes camadas.

```text
┌──────────────────────────┐
│        Interface         │
│ HTML / CSS / JavaScript  │
│       Thymeleaf          │
└────────────┬─────────────┘
             │
             ▼
┌──────────────────────────┐
│       Controllers        │
│      Spring MVC          │
└────────────┬─────────────┘
             │
             ▼
┌──────────────────────────┐
│         Services         │
│    Regras de negócio     │
└────────────┬─────────────┘
             │
             ▼
┌──────────────────────────┐
│       Repositories       │
│      Spring Data         │
└────────────┬─────────────┘
             │
             ▼
┌──────────────────────────┐
│       PostgreSQL         │
│     Banco de dados       │
└──────────────────────────┘
```

Essa separação facilita a manutenção, organização e evolução do sistema.

---

## 📋 Pré-requisitos

Antes de executar o projeto, é necessário possuir:

- **Git**
- **Java**, utilizando versão compatível com a definida no projeto
- **Maven**
- **PostgreSQL**
- Uma IDE Java, como:
  - IntelliJ IDEA
  - Eclipse
  - Visual Studio Code

---

## 🚀 Como executar o projeto

### 1. Clone o repositório

```bash
git clone https://github.com/ThiagoGatti/PI5.git
```

Entre na pasta do projeto:

```bash
cd PI5
```

---

### 2. Configure o PostgreSQL

Crie um banco de dados destinado à aplicação.

Exemplo:

```sql
CREATE DATABASE use_devolva;
```

Depois configure os dados de conexão do banco no arquivo de configuração do Spring Boot.

Exemplo de configuração:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/use_devolva
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

> ⚠️ Não publique usuários, senhas, tokens, chaves de API ou outras credenciais no repositório.

---

### 3. Instale as dependências

Na raiz do projeto execute:

```bash
mvn clean install
```

---

### 4. Execute a aplicação

```bash
mvn spring-boot:run
```

Caso o projeto possua o Maven Wrapper, também é possível utilizar:

#### Windows

```bash
mvnw.cmd spring-boot:run
```

#### Linux/macOS

```bash
./mvnw spring-boot:run
```

---

### 5. Acesse a aplicação

Após a inicialização, a aplicação poderá ser acessada pelo navegador.

Por padrão, aplicações Spring Boot são executadas em:

```text
http://localhost:8080
```

> A porta poderá ser diferente caso tenha sido alterada nas configurações do projeto.

---

## 🔐 Configuração e segurança

Dados sensíveis não devem ser adicionados diretamente ao GitHub.

Entre eles:

```text
Senha do banco de dados
Chaves de API
Tokens de autenticação
Credenciais de serviços externos
Credenciais de pagamento
```

Para ambientes reais, prefira utilizar **variáveis de ambiente**.

Exemplo:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
```

---

## 💡 Proposta do Use & Devolva

O projeto parte de uma situação cotidiana:

> Por que comprar uma ferramenta que será utilizada poucas vezes se outra pessoa próxima já possui essa ferramenta disponível?

A partir dessa ideia, o **Use & Devolva** busca criar uma ponte entre quem **possui uma ferramenta** e quem **precisa utilizá-la temporariamente**.

```text
Quem possui
uma ferramenta
      │
      │ disponibiliza
      ▼
┌─────────────────┐
│  USE & DEVOLVA  │
└─────────────────┘
      │
      │ conecta
      ▼
Quem precisa
da ferramenta
```

---

## 🎓 Projeto acadêmico

O **Use & Devolva** foi desenvolvido como Trabalho de Conclusão de Curso no **Senac São Paulo**.

O desenvolvimento ocorreu entre **2025 e 2026**, envolvendo etapas de levantamento de requisitos, modelagem, implementação, integração, testes e apresentação final.

O projeto proporcionou a aplicação prática de conceitos como:

- Programação orientada a objetos
- Desenvolvimento web
- Arquitetura MVC
- Persistência de dados
- Banco de dados relacional
- Desenvolvimento com Spring Boot
- Integração entre frontend e backend
- Autenticação
- Geolocalização
- Integração com serviços externos
- Desenvolvimento colaborativo com Git e GitHub

---

## 👨‍💻 Desenvolvedores

Projeto desenvolvido por:

- **Vinícius Ferreira Scott**
- **Hevillyn Klein**
- **Thiago Gatti Bafi**

### Orientação acadêmica

**Professor:** José Martinele Alves Silva  
**Coordenação:** Eduardo Heredia

**Instituição:** Senac São Paulo

---

## 📌 Status do projeto

```text
✅ Projeto concluído
✅ TCC apresentado
✅ TCC aprovado
```

---

## 📂 Repositório

O código-fonte do projeto está disponível em:

**GitHub:**  
https://github.com/ThiagoGatti/PI5

---

## 📄 Uso do projeto

Este projeto foi desenvolvido com finalidade **acadêmica e educacional**.

Caso deseje utilizar partes do código em outros projetos, recomenda-se consultar os autores e verificar as condições de utilização do repositório.

---

<p align="center">
  Desenvolvido com ☕ Java, 🌱 Spring Boot e muitas horas de código.
</p>

<p align="center">
  <strong>Use & Devolva — compartilhe, utilize e devolva.</strong>
</p>
