package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import javax.swing.*;

import control.DungeonController;
import control.IDungeonController;
import dungeon.ReadOnlyDungeonModel;

public class DungeonGridView extends JFrame implements DungeonView {

  private DungeonPanel dungeonPanel;
  private JMenuBar jMenuBar;
  private JMenu dungeonSettings;
  private JMenu newGame;
  private JMenu exit;
  private JScrollPane jScrollPane;
  private JMenuItem dungeonPreferences;
  private JMenuItem newGame1;
  private JMenuItem restart;
  private JPanel jPanel;
  private JButton pickArrowButton;
  private JButton pickTreasureButton;
  private JButton moveNorthButton;
  private JButton moveSouthButton;
  private JButton moveEastButton;
  private JButton moveWestButton;
  private JButton playerDescButton;
  private JLabel distanceLabel;
  private JLabel directionLabel;
  private JTextField directionTextField;
  private JTextField distanceTextField;
  private JFrame jFrameDungeonPref;
  private ReadOnlyDungeonModel model;
  private JSplitPane jSplitPane;
  private JTextField rowText;
  private JTextField colsText;
  private JTextField interconnectivityText;
  private JTextField wrappingText;
  private JTextField treasurePercentText;
  private JTextField numberOfMonstersText;


  private JButton shootButton;
  private JLabel moveLabel;
  private JLabel locationDescLabel;


  public DungeonGridView(ReadOnlyDungeonModel model) {
    this.setSize(700, 700);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());
    this.model = model;
    addJMenuBar();
    addJButton();
    jScrollPane = new JScrollPane(jPanel);
    this.add(jScrollPane, BorderLayout.LINE_END);
    setDungeonSwing();
  }

  private void setDungeonSwing() {
    dungeonPanel = new DungeonPanel(model);
    dungeonPanel.setPreferredSize(new Dimension(500, 500));

    dungeonPanel.setDungeonPanel();
    jScrollPane = new JScrollPane(dungeonPanel);
    this.add(BorderLayout.CENTER, jScrollPane);
    moveLabel = new JLabel();
    moveLabel.setPreferredSize(new Dimension(500, 100));
    dungeonPanel.add(BorderLayout.PAGE_END, moveLabel);

    locationDescLabel = new JLabel("", SwingConstants.CENTER);
    locationDescLabel.setPreferredSize(new Dimension(700, 200));
    this.add(BorderLayout.PAGE_END, locationDescLabel);

    showDungeon();
    resetFocus();
    this.pack();
  }


  private void addJMenuBar() {
    jMenuBar = new JMenuBar();
    this.setJMenuBar(jMenuBar);
    dungeonSettings = new JMenu("Dungeon");
    jMenuBar.add(dungeonSettings);
    newGame = new JMenu("New Game");
    jMenuBar.add(newGame);
    exit = new JMenu("Exit");
    jMenuBar.add(exit);

    dungeonPreferences = new JMenuItem("Dungeon Preferences");
    dungeonPreferences.setActionCommand("Dungeon Preferences Menu");

    dungeonSettings.add(dungeonPreferences);

    newGame1 = new JMenuItem("New Game");
    newGame1.setActionCommand("New Game Menu");
    restart = new JMenuItem("Restart Game");
    restart.setActionCommand("Restart Game Menu");

    newGame.add(newGame1);
    newGame.add(restart);

    JMenuItem exitButton = new JMenuItem("Exit Game");
    exit.add(exitButton);
    exitButton.addActionListener((ActionEvent e) -> System.exit(0));

  }

  private void addJButton() {
    jPanel = new JPanel();
    jPanel.setPreferredSize(new Dimension(150, 500));
    moveNorthButton = new JButton("Move North");
    moveNorthButton.setActionCommand("Move North Button");
    jPanel.add(moveNorthButton);

    moveSouthButton = new JButton("Move South");
    moveSouthButton.setActionCommand("Move South Button");
    jPanel.add(moveSouthButton);

    moveEastButton = new JButton("Move East");
    moveEastButton.setActionCommand("Move East Button");
    jPanel.add(moveEastButton);

    moveWestButton = new JButton("Move West");
    moveWestButton.setActionCommand("Move West Button");
    jPanel.add(moveWestButton);


    pickArrowButton = new JButton("Pick Arrow");
    pickArrowButton.setActionCommand("Pick Arrow Button");
    jPanel.add(pickArrowButton);

    pickTreasureButton = new JButton("Pick Treasure");
    pickTreasureButton.setActionCommand("Pick Treasure Button");
    jPanel.add(pickTreasureButton);

    shootButton = new JButton("Shoot Arrow");
    shootButton.setActionCommand("Shoot Arrow Button");
    jPanel.add(shootButton);

    distanceLabel = new JLabel("Enter Distance");
    jPanel.add(distanceLabel);

    distanceTextField = new JTextField("", 2);
    jPanel.add(distanceTextField);

    directionLabel = new JLabel("Enter Direction");
    jPanel.add(directionLabel);

    directionTextField = new JTextField("", 2);
    jPanel.add(directionTextField);

    playerDescButton = new JButton("Player Description");
    playerDescButton.setActionCommand("Player Description Button");
    jPanel.add(playerDescButton);

    jPanel.add(shootButton);
    resetFocus();
  }

  @Override
  public void addActionListener(ActionListener actionListener) {
    moveNorthButton.addActionListener(actionListener);
    moveSouthButton.addActionListener(actionListener);
    moveEastButton.addActionListener(actionListener);
    moveWestButton.addActionListener(actionListener);
    pickArrowButton.addActionListener(actionListener);
    pickTreasureButton.addActionListener(actionListener);
    shootButton.addActionListener(actionListener);
    dungeonPreferences.addActionListener(actionListener);
    restart.addActionListener(actionListener);
    newGame1.addActionListener(actionListener);
    playerDescButton.addActionListener(actionListener);
  }

  @Override
  public void showPlayerDesc() {
    JOptionPane.showMessageDialog(this, model.getPlayerDesc(), "Player Description", JOptionPane.OK_OPTION);
    resetFocus();
  }

  @Override
  public void makeVisible() {
    setVisible(true);
  }

  @Override
  public void showErrorMessage(String error) {
    System.out.println("Error: " + error);
    JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void showDungeon() {
    dungeonPanel.showCaveDetails();
  }

  @Override
  public void move() {
    dungeonPanel.showCaveDetails();
  }

  @Override
  public void resetFocus() {
    this.setFocusable(true);
    this.requestFocus();
  }


  @Override
  public void refresh() {
    repaint();
  }

  @Override
  public void setString(String s) {
    moveLabel.setText("<html>" + s.replaceAll("\n", "<br/>") + "</html>");
    locationDescLabel.setText("<html>" + model.getLocationDesc().replaceAll("\n", "<br/>") + "</html>");
    resetFocus();
    refresh();
  }

  @Override
  public String getDistance() {
    resetFocus();
    return distanceTextField.getText();
  }

  @Override
  public String getDirection() {
    resetFocus();
    return directionTextField.getText();
  }

  @Override
  public void restart(ReadOnlyDungeonModel model) {
    this.model = model;
    this.getContentPane().remove(jScrollPane);
    moveLabel.setText("");
    locationDescLabel.setText("");
    setDungeonSwing();
    showDungeon();
    this.getContentPane().revalidate();
    this.getContentPane().repaint();
    resetFocus();
  }

  public void setDungeonPreferences() {
    JPanel jPanel = new JPanel();
    jPanel.setPreferredSize(new Dimension(450, 100));

    JLabel rows = new JLabel("Number of rows");
    JLabel cols = new JLabel("Number of columns");
    JLabel interconnectivity = new JLabel("Interconnectivity");
    JLabel wrapping = new JLabel("Wrapping");
    JLabel treasurePercent = new JLabel("Treasure Percent");
    JLabel numberOfMonsters = new JLabel("Number of Monsters");


    rowText = new JTextField(String.valueOf(model.getRow()), 5);
    colsText = new JTextField(String.valueOf(model.getColumns()), 5);
    interconnectivityText = new JTextField(String.valueOf(model.getInterconnectivity()), 5);
    wrappingText = new JTextField(String.valueOf(model.getWrapping()), 5);
    treasurePercentText = new JTextField(String.valueOf(model.getTreasurePercent()), 5);
    numberOfMonstersText = new JTextField(String.valueOf(model.getNumberOfMonsters()), 5);

    jPanel.add(rows);
    jPanel.add(rowText);
    jPanel.add(cols);
    jPanel.add(colsText);
    jPanel.add(interconnectivity);
    jPanel.add(interconnectivityText);
    jPanel.add(wrapping);
    jPanel.add(wrappingText);
    jPanel.add(new JLabel("               "));
    jPanel.add(treasurePercent);
    jPanel.add(treasurePercentText);
    jPanel.add(numberOfMonsters);
    jPanel.add(numberOfMonstersText);

    JOptionPane.showConfirmDialog(this, jPanel,
            "Dungeon Preferences", JOptionPane.OK_OPTION);
    resetFocus();
  }

  @Override
  public int getRows() {
    return Integer.parseInt(rowText.getText());
  }

  @Override
  public int getCols() {
    return Integer.parseInt(colsText.getText());
  }

  @Override
  public int getInterconnectivity() {
    return Integer.parseInt(interconnectivityText.getText());
  }

  @Override
  public boolean getWrapping() {
    return Boolean.parseBoolean(wrappingText.getText());
  }

  @Override
  public float getTreasurePercent() {
    return Float.parseFloat(treasurePercentText.getText());
  }

  @Override
  public int getNumberOfMonsters() {
    return Integer.parseInt(numberOfMonstersText.getText());
  }

  @Override
  public void addClickListener(IDungeonController listener) {
    System.out.println("Calling adapter");
    MouseAdapter clickAdapter = new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        System.out.println("1Calling listener");
        super.mouseClicked(e);
        System.out.println("2Calling listener");
        dungeonPanel.addClickListenerDungeon(listener);
      }
    };
    dungeonPanel.addMouseListener(clickAdapter);
  }
}