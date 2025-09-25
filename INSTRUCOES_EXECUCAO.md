# Instruções para Executar o Aplicativo Lanchonete

## ✅ Problemas Corrigidos

1. **Compatibilidade Java**: Atualizada de Java 8 para Java 11
2. **Métodos Deprecated**: Corrigidos `onBackPressed()` e `startActivityForResult()`
3. **Serializable Warning**: Corrigido `getSerializableExtra()` deprecated
4. **Tratamento de Erros**: Adicionado try-catch no MainViewModel para evitar crashes

## 🚀 Como Executar

### No Android Studio:
1. Abra o projeto `LanchoneteApp` no Android Studio
2. Conecte um dispositivo Android ou inicie um emulador
3. Clique no botão "Run" (▶️) ou pressione `Shift + F10`
4. O aplicativo deve abrir sem crashes

### Via Terminal:
```bash
cd /Users/douglas/Desktop/faculdade/LanchoneteApp
./gradlew installDebug
```

## 🔧 Funcionalidades Disponíveis

### Dashboard Principal
- **Vendas Hoje**: Mostra o total de vendas do dia
- **Pedidos Hoje**: Conta o número de pedidos
- **Ações Rápidas**: 4 cards para navegar para:
  - Vendas
  - Estoque
  - Produtos
  - Relatórios

### Módulos Implementados
1. **Vendas**: Sistema completo de vendas com carrinho
2. **Estoque**: Gestão de inventário e movimentações
3. **Produtos**: CRUD completo de produtos
4. **Relatórios**: Relatórios de vendas e estoque

## 🗄️ Banco de Dados

O aplicativo usa **Room Database** com as seguintes entidades:
- `Product`: Produtos
- `InventoryItem`: Itens de estoque
- `Sale`: Vendas
- `SaleItem`: Itens de venda
- `StockMovement`: Movimentações de estoque

## 🔥 Firebase

O projeto está configurado para Firebase:
- **Firestore**: Para sincronização de dados
- **Authentication**: Para autenticação de usuários
- **Analytics**: Para análise de uso

**Nota**: Você precisa adicionar o arquivo `google-services.json` na pasta `app/` para usar o Firebase.

## 🐛 Solução de Problemas

### Se o app ainda crashar:
1. Verifique os logs no Android Studio (Logcat)
2. Certifique-se de que o dispositivo/emulador tem Android 7.0+ (API 24+)
3. Limpe o projeto: `./gradlew clean`
4. Recompile: `./gradlew assembleDebug`

### Logs Importantes:
- Procure por erros relacionados ao banco de dados
- Verifique se há problemas de permissões
- Confirme se todos os recursos estão sendo encontrados

## 📱 Teste das Funcionalidades

1. **Teste Básico**: Abra o app e navegue pelos cards
2. **Teste de Vendas**: Adicione produtos ao carrinho
3. **Teste de Produtos**: Crie/edite produtos
4. **Teste de Estoque**: Verifique movimentações

## 🎯 Próximos Passos

1. Adicionar dados de teste
2. Configurar Firebase
3. Implementar autenticação
4. Adicionar validações
5. Testes unitários

---

**Status**: ✅ Aplicativo compilando e pronto para execução
**Última Atualização**: $(date)
