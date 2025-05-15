## 📌 Descrição do Projeto

API bancária desenvolvida em Java com Spring Boot, focada em simular operações bancárias como criação de contas, movimentações financeiras e gerenciamento de clientes. A API foi construída utilizando práticas modernas de desenvolvimento e autenticação OAuth2.

## ✅ Funcionalidades

- Criação e gerenciamento de contas bancárias
- Cadastro e atualização de clientes
- Realização de transações entre contas
- Consulta de transações por valor, data ou conta
- Autenticação com OAuth2 usando o fluxo `client_credentials`
- Segurança com token Bearer
- Validações com anotações do Bean Validation
- Conversão entre entidades e DTOs feita por lógica própria (sem MapStruct), com objetivo de treinar e reforçar o entendimento
- Testes unitários e de integração implementados com JUnit e Mockito
- Utilização do padrão Builder em algumas entidades
- Tratamento global de exceções com mensagens personalizadas

## 🛠️ Tecnologias e Ferramentas

- Java 21
- Spring Boot
- Spring Security
- Spring Authorization Server (OAuth2)
- JPA / Hibernate
- MySQL
- JUnit & Mockito
- Lombok

## 🚀 Observações

A API foi estruturada com foco em aprendizado e boas práticas, priorizando a clareza no fluxo dos dados (Controller → DTO → Service → Repository) e a segurança com autenticação baseada em token.
