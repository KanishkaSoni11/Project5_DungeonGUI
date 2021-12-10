package controllertest;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;


import control.IDungeonController;
import dungeon.ReadOnlyDungeonModel;
import view.DungeonView;

public class MockView implements DungeonView {
  private StringBuilder log;

  public MockView(StringBuilder log) {
    this.log = log;
  }

  @Override
  public void makeVisible() {
    log.append("Calling make visible method").append("\n");
  }

  @Override
  public void showErrorMessage(String error) {
    log.append("Error:  " + error).append("\n");
  }

  @Override
  public void showDungeon() {
    log.append("Showing cave Details").append("\n");
  }

  @Override
  public void addKeyListener(KeyListener listener) {

  }


  @Override
  public void addActionListener(ActionListener actionListener) {
    log.append("Calling action listener").append("\n");

  }

  @Override
  public void resetFocus() {
    log.append("Calling reset focus").append("\n");
  }

  @Override
  public void refresh() {
    log.append("Calling refresh method").append("\n");
  }

  @Override
  public void setString(String s) {
    log.append("Calling set string method").append("\n");
    log.append(s);
  }

  @Override
  public String getDistance() {
    log.append("Calling get distance method").append("\n");
    return null;
  }

  @Override
  public String getDirection() {
    log.append("Calling get direction method").append("\n");
    return null;
  }

  @Override
  public void restart(ReadOnlyDungeonModel model) {
    log.append("Calling restart method").append("\n");
  }

  @Override
  public void setDungeonPreferences() {
    log.append("Calling set dungeon preferences method").append("\n");
  }

  @Override
  public void addClickListener(IDungeonController listener) {
    log.append("Calling add click listener method").append("\n");
  }

  @Override
  public int getRows() {
    log.append("Calling get rows method").append("\n");
    return 0;
  }

  @Override
  public int getCols() {
    log.append("Calling get columns method").append("\n");
    return 0;
  }

  @Override
  public int getInterconnectivity() {
    log.append("Calling get interconnectivity method").append("\n");
    return 0;
  }

  @Override
  public boolean getWrapping() {
    log.append("Calling get wrapping method").append("\n");
    return false;
  }

  @Override
  public float getTreasurePercent() {
    log.append("Calling get treasure percent method").append("\n");
    return 0;
  }

  @Override
  public int getNumberOfMonsters() {
    log.append("Calling get monster method").append("\n");
    return 0;
  }

  @Override
  public void showPlayerDesc() {
    log.append("Calling show player description").append("\n");

  }
}
