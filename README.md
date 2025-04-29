# Aplicativo Palavras

Um aplicativo Android simples para gerenciar uma lista de palavras.

## Funcionalidades

- Adicionar palavras através de um botão flutuante "+"
- Selecionar/desselecionar palavras clicando nelas (o fundo muda de branco para verde)
- Deslizar uma palavra para a esquerda para excluí-la
- Opção de desfazer a exclusão de uma palavra

## Como gerar o APK

### Usando o Android Studio

1. Abra o projeto no Android Studio
2. Selecione "Build" > "Build Bundle(s) / APK(s)" > "Build APK(s)"
3. O APK será gerado em `app/build/outputs/apk/debug/app-debug.apk`

### Usando a linha de comando

Para gerar um APK de debug:

```
./gradlew assembleDebug
```

O APK será gerado em `app/build/outputs/apk/debug/app-debug.apk`

Para gerar um APK de release:

```
./gradlew assembleRelease
```

O APK será gerado em `app/build/outputs/apk/release/Palavras-1.0.apk`

Ou use a tarefa personalizada:

```
./gradlew generateReleaseApk
```

## Requisitos

- Android 8.0 (API 26) ou superior
