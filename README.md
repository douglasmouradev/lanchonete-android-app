# Lanchonete App

## Descrição
Aplicativo Android para controle de vendas e estoque de lanchonetes, desenvolvido em Kotlin com arquitetura MVVM e banco de dados Room.

## Funcionalidades

### 🛒 Vendas
- Interface intuitiva para registro de vendas
- Carrinho de compras com controle de quantidade
- Múltiplas formas de pagamento (Dinheiro, Cartão, PIX, Vale Refeição)
- Aplicação de descontos
- Registro de informações do cliente
- Histórico de vendas

### 📦 Controle de Estoque
- Gestão completa de produtos
- Controle de estoque mínimo e máximo
- Alertas de estoque baixo e sem estoque
- Movimentações de entrada e saída
- Reposição de estoque
- Ajustes de inventário

### 📊 Relatórios
- Relatórios diários, semanais e mensais
- Análise de vendas
- Produtos mais vendidos
- Relatórios de estoque
- Exportação de dados

### 🏪 Gestão de Produtos
- Cadastro de produtos com categorias
- Controle de preços e custos
- Código de barras
- Upload de imagens
- Busca e filtros

## Tecnologias Utilizadas

- **Linguagem**: Kotlin
- **Arquitetura**: MVVM (Model-View-ViewModel)
- **Banco de Dados**: Room (SQLite)
- **Backend**: Firebase (Firestore, Authentication)
- **UI**: Material Design 3
- **Navegação**: Navigation Component
- **Injeção de Dependência**: ViewModel e LiveData
- **Coroutines**: Para operações assíncronas

## Estrutura do Projeto

```
app/
├── src/main/java/com/lanchonete/app/
│   ├── data/
│   │   ├── database/          # Room Database, DAOs, Converters
│   │   ├── model/             # Entidades de dados
│   │   └── repository/        # Repositório de dados
│   ├── ui/
│   │   ├── sales/             # Módulo de vendas
│   │   ├── inventory/         # Módulo de estoque
│   │   ├── products/          # Módulo de produtos
│   │   └── reports/           # Módulo de relatórios
│   ├── viewmodel/             # ViewModels
│   └── MainActivity.kt        # Activity principal
└── src/main/res/
    ├── layout/                # Layouts XML
    ├── drawable/              # Ícones e recursos gráficos
    ├── values/                # Strings, cores, temas
    └── menu/                  # Menus
```

## Instalação

1. Clone o repositório
2. Abra o projeto no Android Studio
3. Configure o Firebase:
   - Adicione o arquivo `google-services.json` na pasta `app/`
   - Configure o projeto no Firebase Console
4. Sincronize o projeto
5. Execute no dispositivo ou emulador

## Configuração do Firebase

1. Acesse o [Firebase Console](https://console.firebase.google.com/)
2. Crie um novo projeto
3. Adicione um app Android com o package name: `com.lanchonete.app`
4. Baixe o arquivo `google-services.json`
5. Coloque o arquivo na pasta `app/` do projeto
6. Habilite o Firestore Database no console

## Funcionalidades Implementadas

### ✅ Completas
- [x] Estrutura base do projeto
- [x] Modelos de dados (Product, Sale, Inventory, etc.)
- [x] Banco de dados Room com DAOs
- [x] Interface principal (Dashboard)
- [x] Módulo de vendas completo
- [x] Módulo de controle de estoque
- [x] Módulo de gestão de produtos
- [x] Módulo de relatórios
- [x] Design Material 3
- [x] Navegação entre telas

### 🚧 Em Desenvolvimento
- [ ] Integração completa com Firebase
- [ ] Autenticação de usuários
- [ ] Sincronização offline/online
- [ ] Notificações push
- [ ] Backup automático

### 📋 Próximas Funcionalidades
- [ ] Sistema de usuários e permissões
- [ ] Relatórios avançados com gráficos
- [ ] Integração com impressoras
- [ ] Sistema de cupons e promoções
- [ ] Análise de vendas com IA
- [ ] App para clientes (pedidos online)

## Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## Contato

Desenvolvido como projeto acadêmico para demonstração de desenvolvimento Android com Kotlin.

---

**Nota**: Este é um projeto de demonstração desenvolvido para fins acadêmicos. Para uso em produção, são necessárias validações adicionais de segurança e testes mais abrangentes.
