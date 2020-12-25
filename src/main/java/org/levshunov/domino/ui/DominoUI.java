package org.levshunov.domino.ui;

import org.levshunov.domino.Game;
import org.levshunov.domino.ai.AIStrategy;
import org.levshunov.domino.ai.StrategyHelper;
import org.levshunov.domino.model.Deck;
import org.levshunov.domino.model.EndGameException;
import org.levshunov.domino.model.Turn;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DominoUI extends JFrame {
    private JLabel dominoAILabel, dominoDeckLabel, dominoYourLabel, turnsLabel, strategiesLabel,
        dominoAICount, dominoDeckCount, dominoYourList;
    private JButton turnButton, newGameButton;
    private List<Turn> possibleTurnList;
    private JList<String> possibleTurnJList;
    private List<JRadioButton> strategyButtons;
    private ButtonGroup strategyButtonGroup;
    private JTextArea fieldArea;
    private JScrollPane fieldAreaScroll, turnListScroll;
    private Game game;
    private Font font;

    public DominoUI() {
        initUI();
    }

    public static void run() {
        EventQueue.invokeLater(() -> {
            DominoUI ui = new DominoUI();
            ui.setVisible(true);
        });
    }

    private void initUI() {
        setTitle("Левшунов, домино");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        createElements();
        createListeners();
        createLayout();
    }

    private void createElements() {
        font = new Font("Consolas", Font.PLAIN, 13);

        dominoAILabel = new JLabel("Количество домино у противника:");
        dominoDeckLabel = new JLabel("Количество домино в резерве:");
        dominoYourLabel = new JLabel("Ваши домино:");
        turnsLabel = new JLabel("Доступные ходы:");
        strategiesLabel = new JLabel("Доступные стратегии AI:");
        dominoAICount = new JLabel("0");
        dominoDeckCount = new JLabel(Deck.maxDeckSize().toString());
        dominoYourList = new JLabel("");
        dominoYourList.setFont(font);

        turnButton = new JButton("Сделать ход");
        newGameButton = new JButton("Начать новую игру");

        strategyButtonGroup = new ButtonGroup();
        strategyButtons = new ArrayList<>();
        List<AIStrategy> strategies = StrategyHelper.getStrategies();
        for (AIStrategy strategy : strategies) {
            JRadioButton button = new JRadioButton(strategy.uiName());
            button.setActionCommand(strategy.getClass().getName());
            strategyButtons.add(button);
            strategyButtonGroup.add(button);
        }
        strategyButtonGroup.getElements().nextElement().setSelected(true);

        fieldArea = new JTextArea(10, 35);
        fieldArea.setEditable(false);
        fieldArea.setFont(font);
        fieldAreaScroll = new JScrollPane(fieldArea);

        possibleTurnJList = new JList<>();
        possibleTurnJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        possibleTurnJList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        possibleTurnJList.setVisibleRowCount(-1);
        possibleTurnJList.setFixedCellWidth(120);
        possibleTurnJList.setFont(font);
        turnListScroll = new JScrollPane(possibleTurnJList);
    }

    private void createListeners() {
        newGameButton.addActionListener(e -> {
            AIStrategy strategy = StrategyHelper.getStrategyByName(strategyButtonGroup.getSelection().getActionCommand());
            game = new Game(strategy);
            try {
                refresh(game.play());
            } catch (EndGameException ex) {
                endGame();
            }
        });

        turnButton.addActionListener(e -> {
            int index = possibleTurnJList.getSelectedIndex();
            if (index == -1) {
                JOptionPane.showMessageDialog(DominoUI.this,
                    "Выберите ход", "Ошибка", JOptionPane.ERROR_MESSAGE);
            } else {
                List<Turn> possibleTurns;
                try {
                    possibleTurns = game.play(possibleTurnList.get(index));
                    refresh(possibleTurns);
                } catch (EndGameException ex) {
                    endGame();
                }
            }
        });
    }

    private void createLayout() {
        Container panel = getContentPane();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        GroupLayout.ParallelGroup strategiesHorizontalLayout = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        for (JRadioButton strategyButton : strategyButtons) {
            strategiesHorizontalLayout.addComponent(strategyButton);
        }

        GroupLayout.SequentialGroup strategiesVerticalLayout = layout.createSequentialGroup();
        for (JRadioButton strategyButton : strategyButtons) {
            strategiesVerticalLayout.addComponent(strategyButton);
        }

        layout.setHorizontalGroup(layout.createParallelGroup()
            .addComponent(strategiesLabel)
            .addGroup(strategiesHorizontalLayout)
            .addComponent(newGameButton)
            .addGap(10)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(dominoAILabel)
                    .addComponent(dominoAICount, GroupLayout.Alignment.CENTER))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(dominoDeckLabel)
                    .addComponent(dominoDeckCount, GroupLayout.Alignment.CENTER)))
            .addComponent(fieldAreaScroll)
            .addComponent(dominoYourLabel)
            .addComponent(dominoYourList)
            .addComponent(turnsLabel)
            .addComponent(turnListScroll)
            .addComponent(turnButton));

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addComponent(strategiesLabel)
            .addGroup(strategiesVerticalLayout)
            .addComponent(newGameButton)
            .addGap(10)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(dominoAILabel)
                .addComponent(dominoDeckLabel))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(dominoAICount, GroupLayout.Alignment.CENTER)
                .addComponent(dominoDeckCount, GroupLayout.Alignment.CENTER))
            .addComponent(fieldAreaScroll)
            .addComponent(dominoYourLabel)
            .addComponent(dominoYourList)
            .addComponent(turnsLabel)
            .addComponent(turnListScroll)
            .addComponent(turnButton));

        pack();
        setLocationRelativeTo(null);
    }

    private void refresh() {
        dominoAICount.setText(game.getAiPlayer().getDominoesCount().toString());
        dominoDeckCount.setText(game.getDeck().getDominoesCount().toString());
        dominoYourList.setText(game.getHumanPlayer().getUIList());
        fieldArea.setText(game.getField().printField());

        pack();
    }

    private void refresh(List<Turn> possibleTurns) {
        possibleTurnList = possibleTurns;
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addAll(
            possibleTurnList.stream()
                .map(Turn::toString)
                .collect(Collectors.toList()));
        possibleTurnJList.setModel(listModel);

        refresh();
    }

    private void endGame() {
        possibleTurnJList.setModel(new DefaultListModel<>());
        refresh();
        int win = game.isHumanWin();
        String aiDominoList = "";
        if (game.getAiPlayer().getDominoesCount() > 0) {
            aiDominoList = "Домино, оставшиеся у противника:<br>" + game.getAiPlayer().getUIList();
        }
        JLabel message = new JLabel();
        message.setFont(font);
        String title;
        if (win == 1) {
            message.setText("<html>Поздравляем! Вы победили!<br>" + aiDominoList);
            title = "Победа";
        } else if (win == -1) {
            message.setText("<html>Сожалеем, но вы проиграли.<br>" + aiDominoList);
            title = "Поражение";
        } else {
            message.setText("<html>Ничья<br>" + aiDominoList);
            title = "Ничья";
        }
        JOptionPane.showMessageDialog(DominoUI.this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}