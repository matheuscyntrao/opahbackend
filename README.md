# Projeto de Testes Automatizados para API de Usuários

Este documento descreve os passos necessários para configurar o ambiente e executar os testes automatizados desenvolvidos para a API de gerenciamento de usuários.

## 🔧 Configurando o Ambiente:

1.  **Instale o Java Development Kit (JDK) 1.8 ou superior:**
    * Faça o download do JDK no site da Oracle ou através do seu gerenciador de pacotes preferido (ex: `apt install openjdk-17-jdk` no Debian/Ubuntu, `brew install openjdk` no macOS).
    * Configure a variável de ambiente `JAVA_HOME` para apontar para o diretório de instalação do JDK.

2.  **Instale o Apache Maven:**
    * Faça o download do Maven no site oficial do Apache Maven ([https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)).
    * Siga as instruções de instalação para o seu sistema operacional.
    * Verifique a instalação abrindo o terminal e executando o comando `mvn -v`.

3.  **Instale uma IDE (Ambiente de Desenvolvimento Integrado) (Opcional, mas recomendado):**
    * **IntelliJ IDEA (Comunidade ou Ultimate):** Altamente recomendado para desenvolvimento Java e Maven.
    * **Eclipse IDE for Java Developers:** Outra opção popular para desenvolvimento Java.
    * **Visual Studio Code:** Leve e extensível, com bons plugins para Java e Maven.

## ⚙️ Instalando as Dependências:

Este projeto utiliza o Maven para gerenciar as dependências. As dependências necessárias já estão configuradas no arquivo `pom.xml` do projeto. Para instalá-las:

1.  **Navegue até o diretório raiz do projeto** no seu terminal.
2.  **Execute o seguinte comando Maven:**
    ```bash
    mvn clean install
    ```
    Este comando irá baixar todas as dependências listadas no `pom.xml`, incluindo:
    * `io.rest-assured:rest-assured`: Biblioteca para testes de API RESTful em Java.
    * `org.junit.jupiter:junit-jupiter-api`: Framework de testes JUnit 5.
    * `org.hamcrest:hamcrest`: Biblioteca de matchers para asserções.
    * `com.fasterxml.jackson.core:jackson-databind`: Biblioteca Jackson para manipulação de JSON (útil para trabalhar com os corpos das requisições e respostas da API).

## 🛠️ Configuração do Projeto (Maven):

O arquivo `pom.xml` na raiz do projeto contém as configurações necessárias para o Maven construir e executar os testes. As principais configurações incluem:

* **`groupId` e `artifactId`:** Identificadores do projeto.
* **`version`:** Versão do projeto.
* **`<properties>`:** Define variáveis para gerenciar versões de dependências (RestAssured, JUnit, etc.) e a versão do Java para compilação.
* **`<dependencies>`:** Lista as bibliotecas externas que o projeto utiliza (mencionado acima).
* **`<build>`:** Configura o processo de build do Maven, incluindo os plugins para compilação (`maven-compiler-plugin`) e execução de testes (`maven-surefire-plugin`).

## 🔑 Autenticação JWT:

O projeto foi configurado para lidar com a autenticação via token JWT.

1.  **Endpoint de Login:** Os testes incluem uma função (`getJwtToken()`) que faz uma requisição `POST` para o endpoint de login da API (`/login`).
2.  **Credenciais:** As credenciais de teste (email e senha) estão definidas dentro da função `getJwtToken()`:
    ```java
    loginData.put("email", "fulano@qa.com");
    loginData.put("password", "teste");
    ```
    **Importante:** Certifique-se de que essas credenciais correspondam a um usuário válido na sua API para que o token seja gerado com sucesso.
3.  **Inclusão do Token:** O token JWT obtido é armazenado em uma variável estática (`jwtToken`) e é adicionado ao header de autorização (`Authorization: Bearer <token>`) em todas as requisições subsequentes aos endpoints protegidos da API.

## 🚀 Executando os Testes:

Para executar os testes automatizados:

1.  **Navegue até o diretório raiz do projeto** no seu terminal.
2.  **Execute o seguinte comando Maven:**
    ```bash
    mvn test
    ```
    O Maven irá compilar o código de teste e executar todos os testes JUnit encontrados no projeto.
3.  **Verifique os resultados:** Os resultados dos testes serão exibidos no terminal, indicando quantos testes foram executados, quantos passaram, falharam ou foram ignorados.

## 🧪 Cobertura da API:

Este conjunto de testes automatizados visa garantir a cobertura das principais funcionalidades da API de gerenciamento de usuários, incluindo:

* **Criação de Usuário (POST /users):** Testes com dados válidos, dados faltando, dados inválidos e emails duplicados (se aplicável).
* **Listagem de Usuários (GET /users):** Teste para verificar a resposta bem-sucedida e se a lista não está vazia (após a criação de usuários).
* **Obtenção de Usuário por ID (GET /users/{id}):** Testes com IDs existentes e inexistentes.
* **Atualização de Usuário (PUT /users/{id}):** Testes com dados válidos para atualização e tentativas com IDs inexistentes.
* **Exclusão de Usuário (DELETE /users/{id}):** Testes com IDs existentes e inexistentes.
* **Autenticação JWT:** Testes para garantir que os endpoints protegidos requerem um token válido.

## 📝 Próximos Passos e Melhorias:

* **Relatórios Detalhados:** Integrar uma biblioteca de relatórios mais completa como Allure Report para gerar relatórios HTML detalhados com informações sobre os testes executados, status, logs e screenshots de falhas (se implementado).
* **Data-Driven Testing:** Implementar a leitura de dados de teste a partir de arquivos externos (CSV, JSON) para executar os mesmos testes com diferentes conjuntos de dados, aumentando a cobertura e a flexibilidade.
* **Testes de Cenários de Erro Mais Granulares:** Adicionar testes mais específicos para diferentes tipos de erros de validação e regras de negócio da API.
* **Cobertura de Código:** Integrar ferramentas de cobertura de código para medir a porcentagem do código da API que está sendo exercitada pelos testes.
* **Autenticação com Tokens Inválidos/Expirados:** Adicionar testes específicos para verificar o comportamento da API com tokens JWT inválidos ou expirados.
* **Teste de Rate Limiting Aprimorado:** Desenvolver um teste de rate limiting mais robusto que possa verificar com precisão o limite de requisições e o comportamento da API ao atingir e ultrapassar esse limite.
* **Integração CI/CD:** Configurar um pipeline de Integração Contínua e Entrega Contínua (CI/CD) usando ferramentas como Jenkins, GitLab CI/CD ou GitHub Actions para automatizar a execução dos testes a cada alteração no código da API.

Este README fornece um guia completo para configurar o ambiente e executar os testes automatizados para a API de usuários. Certifique-se de seguir os passos de instalação e configuração corretamente para garantir que os testes sejam executados com sucesso.