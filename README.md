# Health Seed Tool 🧬🌱

Uma ferramenta standalone para desenvolvedores Android projetada para popular o **Android Health Connect** com dados de saúde realistas para fins de teste e depuração.

## 🚀 Objetivo
Facilitar o teste de aplicativos que consomem dados do Health Connect, permitindo a geração rápida de até 90 dias de histórico de dados simulados, eliminando a necessidade de gerar dados manualmente através de exercícios ou outros aplicativos.

## 🛠 Tech Stack
- **Linguagem:** Kotlin
- **Arquitetura:** Single Activity
- **UI:** ViewBinding & Material Design
- **Concorrência:** Kotlin Coroutines
- **SDK de Saúde:** `androidx.health.connect:connect-client:1.1.0-alpha12`
- **Versão Mínima:** Android 8.0 (API 26)
- **Target SDK:** 35

## ✨ Funcionalidades
- **Checagem de SDK:** Verifica automaticamente se o Health Connect está disponível ou se precisa de atualização.
- **Gerenciamento de Permissões:** Fluxo integrado para solicitar permissões de escrita necessárias.
- **População Customizável:** Botões individuais para popular categorias específicas de dados.
- **Bulk Seed:** Botão único para popular todas as categorias de uma só vez (90 dias de dados).
- **Limpeza Total:** Botão para apagar todos os registros criados pela ferramenta nos últimos 90 dias.
- **Logs em Tempo Real:** Área de feedback visual mostrando o progresso das inserções.

## 📊 Dados Suportados
A ferramenta gera variações realistas (usando `Random`) para:
1.  **Passos (Steps):** 4.000 a 14.000 passos por dia.
2.  **Batimentos Cardíacos (Heart Rate):** Amostras a cada 2 horas (58 a 110 BPM).
3.  **Sono (Sleep):** Sessões noturnas de 5.5h a 8.5h.
4.  **Calorias Gastas:** 1.800 a 3.200 kcal por dia.
5.  **Peso:** Flutuações diárias entre 68.0kg e 75.0kg.
6.  **Exercícios:** 4 sessões por semana (Corrida e Musculação).

## 🚀 Como Usar
1.  Clone o repositório:
    ```bash
    git clone git@github.com:HugoMadMatos/AndroidHealthPopulate.git
    ```
2.  Abra o projeto no **Android Studio**.
3.  Faça o build e instale no seu dispositivo físico ou emulador com Health Connect.
4.  No app, clique em **Seed Data** (ou em um botão individual).
5.  Conceda as permissões na tela do Health Connect (ative "Permitir Tudo").
6.  Acompanhe o progresso nos logs.

## 📄 Licença
Este projeto foi desenvolvido para fins de ferramentas de desenvolvimento. Sinta-se à vontade para clonar e modificar conforme necessário.
