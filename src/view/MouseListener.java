package view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import control.IDungeonController;
import location.Direction;

public class MouseListener extends MouseAdapter {
  private Direction locationEntry;
  private IDungeonController dungeonController;

  public MouseListener(Direction locationEntry, IDungeonController dungeonController) {
    this.locationEntry = locationEntry;
    this.dungeonController = dungeonController;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    super.mouseClicked(e);
    System.out.println("Here 2");
    String dir = "";
    if (locationEntry.equals(Direction.NORTH)) {
      dir = "N";
    } else if (locationEntry.equals(Direction.SOUTH)) {
      dir = "S";
    } else if (locationEntry.equals(Direction.EAST)) {
      dir = "E";
    } else if (locationEntry.equals(Direction.WEST)) {
      dir = "W";
    }
    dungeonController.move(dir);
  }



}
