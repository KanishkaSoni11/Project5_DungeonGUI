package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import control.DungeonController;
import control.IDungeonController;
import dungeon.Dungeon;
import dungeon.ReadOnlyDungeonModel;

public interface DungeonView {
  void makeVisible();

  void showErrorMessage(String error);

  void showDungeon();

  void addKeyListener(KeyListener listener);

  void move();

  void addActionListener(ActionListener actionListener);

  void resetFocus();

  void refresh();

  void setString(String s);

  String getDistance();

  String getDirection();

  void restart(ReadOnlyDungeonModel model);

  void setDungeonPreferences();

  void addClickListener(IDungeonController listener);

  int getRows();

  int getCols();

  int getInterconnectivity();

  boolean getWrapping();

  float getTreasurePercent();

  int getNumberOfMonsters();

  void showPlayerDesc();


}
