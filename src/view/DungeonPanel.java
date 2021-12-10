package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;

import control.IDungeonController;
import dungeon.ReadOnlyDungeonModel;
import location.Direction;
import location.Location;
import location.Smell;
import location.Treasure;

public class DungeonPanel extends JPanel {
  private final int row;
  private final int col;
  private JLabel jLabel;
  private final Map<Integer, JLabel> arr;
  private final ReadOnlyDungeonModel model;
  private JPanel jPanel;
  private BufferedImage myPicture;
  private String image;
  private Map<Integer, Boolean> isVisited;
  private DungeonView view;


  public DungeonPanel(ReadOnlyDungeonModel model) {
    super();
    this.row = model.getRow();
    this.col = model.getColumns();
    this.model = model;
    jPanel = new JPanel();
    isVisited = new HashMap<>();
    arr = new HashMap<>();
    this.add(jPanel);
  }

  public void setDungeonPanelBlack() {
    for (int i = 0; i < row * col; i++) {
      jLabel = new JLabel();
      isVisited.put(i, true);
      try {
        BufferedImage icon = ImageIO.read((new File("img/blank.png")));
        jLabel.setIcon(new ImageIcon(icon));
        jPanel.add(jLabel);
        arr.put(i, jLabel);
      } catch (IOException e) {

      }
    }

  }


  public void showCaveDetails() {
    this.remove(jPanel);
    jPanel = new JPanel();
    jPanel.setLayout(new GridLayout(row, col));
    jPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    arr.clear();

    isVisited.put(model.getPlayer().getPlayerCave().getCaveId(), true);

    for (Map.Entry<Integer, Boolean> set : isVisited.entrySet()) {
      jLabel = new JLabel();
      if (isVisited.get(set.getKey())) {
        jLabel.setIcon(null);
        image = "";
        if (model.getListOfCaves().get(set.getKey()).getCaveList().containsKey(Direction.NORTH)) {
          image = image + "N";
        }

        if (model.getListOfCaves().get(set.getKey()).getCaveList().containsKey(Direction.EAST)) {
          image = image + "E";
        }
        if (model.getListOfCaves().get(set.getKey()).getCaveList().containsKey(Direction.SOUTH)) {
          image = image + "S";
        }

        if (model.getListOfCaves().get(set.getKey()).getCaveList().containsKey(Direction.WEST)) {
          image = image + "W";
        }

        image = "img/" + image + ".png";
        try {
          myPicture = ImageIO.read(new File(image));
          if (model.getListOfCaves().get(set.getKey()).getTreasureList().contains(Treasure.RUBY)) {
            myPicture = overlay(myPicture, "img/ruby.png", 1);
          }
          if (model.getListOfCaves().get(set.getKey()).getTreasureList().contains(Treasure.DIAMOND)) {
            myPicture = overlay(myPicture, "img/diamond.png", 2);
          }
          if (model.getListOfCaves().get(set.getKey()).getTreasureList().contains(Treasure.SAPPHIRE)) {
            myPicture = overlay(myPicture, "img/emerald.png", 3);
          }
          if (model.getListOfCaves().get(set.getKey()).getArrow() > 0) {
            myPicture = overlay(myPicture, "img/arrow-white.png", 4);
          }
          if (model.getListOfCaves().get(set.getKey()).hasMonster()) {
            myPicture = overlay(myPicture, "img/otyugh.png", 5);
          }

          if (model.getListOfCaves().get(set.getKey()).getThief()) {
            myPicture = overlay(myPicture, "img/thief.png", 6);
          }

          if (model.getListOfCaves().get(set.getKey()).getPit()) {
            myPicture = overlay(myPicture, "img/pit.png", 7);
          }

          if (model.getListOfCaves().get(set.getKey()).hasMovingMonster()) {
            myPicture = overlay(myPicture, "img/monster.png", 7);
          }

          if (model.getPlayer().getPlayerCave().getCaveId() == set.getKey()) {
            myPicture = overlay(myPicture, "img/player.png", 8);
            if (model.getSmell().equals(Smell.HIGH)) {
              myPicture = overlay(myPicture, "img/stench02.png", 7);
            }
            if (model.getSmell().equals(Smell.LOW)) {
              myPicture = overlay(myPicture, "img/stench01.png", 7);
            }
            if (model.getSoil()) {
              myPicture = overlay(myPicture, "img/soil.png", 7);
            }
          }
          jLabel.setIcon(new ImageIcon(myPicture));
          arr.put(set.getKey(), jLabel);
          jPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        } catch (IOException e) {

        }
      } else {
        try {
          jLabel.setIcon(new ImageIcon(ImageIO.read((new File("img/blank.png")))));
        } catch (IOException e) {

        }
      }
      jPanel.add(jLabel);
      arr.put(set.getKey(), jLabel);
      this.add(jPanel);
      jPanel.updateUI();
      this.updateUI();

    }

    if (model.hasReached()) {
      JOptionPane.showMessageDialog(this, "Player has reached the final destination!!!!");
    }
    if (!model.getPlayer().getPlayerLive()) {
      JOptionPane.showMessageDialog(this, "Player is dead!!");
    }
    if (model.hasFallenIntoPit()) {
      JOptionPane.showMessageDialog(this, "Player has fallen into the pit!!Game Over!!");
    }

  }


  public void addClickListenerDungeon(IDungeonController controller) {
    for (Map.Entry<Direction, Location> locationEntry :
            model.getPlayer().getPlayerCave().getCaveList().entrySet()) {
      JLabel map = arr.get(locationEntry.getValue().getCaveId());
      map.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          super.mouseClicked(e);
          String dir = "";
          if (locationEntry.getKey().equals(Direction.NORTH)) {
            dir = "N";
          } else if (locationEntry.getKey().equals(Direction.SOUTH)) {
            dir = "S";
          } else if (locationEntry.getKey().equals(Direction.EAST)) {
            dir = "E";
          } else if (locationEntry.getKey().equals(Direction.WEST)) {
            dir = "W";
          }
          controller.move(dir);
        }
      });
    }

  }

  /**
   * code referred from  -
   * https://piazza.com/class/kt0jcw0x7h955a?cid=1500
   */

  private BufferedImage overlay(BufferedImage starting, String image, int offset) throws
          IOException {
    BufferedImage overlay = ImageIO.read(new File(image));
    int width = Math.max(starting.getWidth(), overlay.getWidth());
    int height = Math.max(starting.getHeight(), overlay.getHeight());
    BufferedImage combinedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics g = combinedImg.getGraphics();
    g.drawImage(starting, 0, 0, null);
    g.drawImage(overlay, offset, offset, null);
    return combinedImg;
  }


}
