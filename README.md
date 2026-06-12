# 🌊 Synthetic Oceans

> Jogo de arcade Space Invaders desenvolvido em Java com JavaFX, para a unidade curricular de *Programação Orientada a Objetos* — Escola Superior de Tecnologia de Setúbal / Instituto Politécnico de Setúbal, 2025/26.

---

## 👥 Autores

| Daniel Alexandre Silveiro Grancho | 2025179361 |
| Daniel Amaro Rosado | 2025177522 |

---

## 📖 Descrição

*Synthetic Oceans* é um jogo de arcade inspirado no clássico Space Invaders, com estética *neon/cyberpunk* e temática subaquática. O jogador controla um submarino e tem de eliminar ondas de invasores alienígenas antes que estes alcancem a base.

O jogo foi desenvolvido com uma arquitetura *MVC (Model-View-Controller)* em Java puro, usando JavaFX para a interface gráfica. Toda a lógica de jogo está completamente separada da camada visual.

---

## 🎮 Como Jogar

| Tecla | Ação |
|-------|------|
| ← / A | Mover para a esquerda |
| → / D | Mover para a direita |
| ESPAÇO | Disparar |
| ESC / P | Pausar / Retomar |

*Objetivo:* eliminar todos os inimigos de cada vaga antes que cheguem ao fundo do ecrã. A cada vaga, a frota aumenta de velocidade.

*Pontuação:*
- Inimigo da frente (ciano) → *10 pts*
- Inimigo do meio (roxo) → *20 pts*
- Inimigo de trás (laranja) → *30 pts*
- Invasor aleatório (dourado) → *50 a 300 pts* (variável)

---

## 🏗️ Arquitetura

O projeto está organizado em dois pacotes principais:


src/
├── game_graphicInterface/
│   ├── Controlador/
│   │   └── Main_Jogo.java          # Ponto de entrada (JavaFX Application)
│   └── View/
│       ├── Manager_View.java        # Gestor de navegação entre ecrãs
│       ├── Game_View.java           # Vista principal do jogo (game loop)
│       ├── MainMenu_View.java       # Menu principal
│       ├── Pause_Menu_View.java     # Menu de pausa (overlay)
│       ├── Game_Over_View.java      # Ecrã de fim de jogo
│       ├── Tabela_Classificacao_View.java  # Tabela de pontuações
│       └── Controlos_View.java      # Ecrã de controlos
└── game_Logic/
├── ModeloJogo.java              # Modelo central (lógica do jogo)
├── EstadoJogo.java              # Enum de estados do jogo
├── EntidadeJogo.java            # Superclasse abstrata de todas as entidades
├── Player.java                  # Jogador
├── FrotaInimigo.java            # Grelha 5×11 de inimigos
├── Inimigo.java                 # Superclasse abstrata dos inimigos
├── Inimigo_Frente.java          # Inimigo das filas da frente (10 pts)
├── Inimigo_Meio.java            # Inimigo das filas do meio (20 pts)
├── Inimigo_Tras.java            # Inimigo da fila de trás (30 pts)
├── Inimigo_aleatorio.java       # Invasor bónus que atravessa o topo
├── Barricadas.java              # Barricadas destrutíveis
├── Projetil.java                # Superclasse abstrata dos projéteis
├── Projetil_jogador.java        # Projétil do jogador (laser reto)
├── Projetil_Inimigo.java        # Projétil inimigo (reto ou zigzag)
└── MelhoresPontuacoes.java      # Tabela de high scores (persistência em disco)


### Padrão MVC

- *Model* (game_Logic) — toda a lógica de jogo, colisões, pontuações e estado. Sem qualquer dependência de JavaFX.
- *View* (game_graphicInterface.View) — renderização com formas JavaFX (Pane, Rectangle, Ellipse, etc.). Cada entidade é representada por objetos JavaFX atualizados a cada frame.
- *Controller* — o Manager_View gere a navegação entre ecrãs; o Game_View processa o input e coordena o game loop com AnimationTimer.

---

## ✨ Funcionalidades

- ✅ Frota de 55 inimigos em grelha 5×11 com movimento em bloco
- ✅ 3 tipos de inimigos com pontuações diferentes
- ✅ Invasor aleatório bónus com pontos variáveis (50–300 pts)
- ✅ 3 barricadas destrutíveis com sistema de dano progressivo (rachas visuais)
- ✅ Projéteis inimigos com trajetória reta ou zigzag
- ✅ Sistema de vagas com dificuldade crescente (velocidade e cadência de disparo)
- ✅ Menu de pausa com overlay semitransparente
- ✅ Tabela de high scores persistente em disco (pontuacoes.dat)
- ✅ Deteção de novo recorde
- ✅ Estatísticas de fim de jogo (pontuação, vaga alcançada, precisão de tiro)
- ✅ Estética neon/cyberpunk com efeitos de brilho e gradientes

---

## 🛠️ Tecnologias

- *Java 17+*
- *JavaFX 17+*
- Serialização Java (ObjectOutputStream / ObjectInputStream) para persistência de pontuações

---

## ▶️ Como Executar

### Pré-requisitos
- JDK 17 ou superior
- JavaFX SDK 17 ou superior

### Compilar e executar (com JavaFX no module-path)

bash
javac --module-path /caminho/para/javafx/lib \
--add-modules javafx.controls \
-d out \
src/**/*.java

java --module-path /caminho/para/javafx/lib \
--add-modules javafx.controls \
-cp out \
game_graphicInterface.Controlador.Main_Jogo


> Se estiveres a usar *IntelliJ IDEA* ou *Eclipse*, basta configurar o JavaFX SDK como biblioteca do projeto e correr a classe Main_Jogo diretamente.

---

## 📁 Ficheiro de Pontuações

As pontuações são guardadas automaticamente no ficheiro pontuacoes.dat na raiz do projeto. O ficheiro é criado na primeira partida e atualizado sempre que uma nova pontuação é registada. Para limpar o registo, usa o botão *LIMPAR* na tabela de classificações ou apaga o ficheiro manualmente.

---

## 📄 Licença

Projeto académico — Escola Superior de Tecnologia de Setúbal, 2025/26.
