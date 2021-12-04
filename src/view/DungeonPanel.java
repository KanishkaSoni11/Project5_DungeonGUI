package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.IconView;

import control.DungeonCommandController;
import control.DungeonController;
import control.IDungeonController;
import control.command.Move;
import dungeon.Dungeon;
import dungeon.ReadOnlyDungeonModel;
import location.Direction;
import location.Location;
import location.Smell;
import location.Treasure;

public class DungeonPanel extends JPanel {
  private int row;
  private int col;
  private JLabel jLabel;
  private Map<Integer, JLabel> arr;
  private ReadOnlyDungeonModel model;
  private JPanel jPanel;
  private BufferedImage myPicture;
  private String image;
  private BufferedImage[] bufferedImages;
  private Map<Integer, Boolean> isVisited;
  private Map<Integer, JLabel> neighbourList;

  public DungeonPanel(ReadOnlyDungeonModel model) {
    super();
    this.row = model.getRow();
    this.col = model.getColumns();
    this.model = model;
    jPanel = new JPanel();

    jPanel.setLayout(new GridLayout(row, col));
    this.add(jPanel);
    bufferedImages = new BufferedImage[row * col];
    isVisited = new HashMap<>();
    arr = new HashMap<>();
    neighbourList = new HashMap<>();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

  }

  public void setDungeonPanel() {
    for (int i = 0; i < row * col; i++) {
      jLabel = new JLabel();
      image = "img/black.png";
      isVisited.put(i, false);
      try {
        myPicture = ImageIO.read(new File(image));
        jLabel.setIcon(new ImageIcon(myPicture));
        jPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        jPanel.add(jLabel);
        arr.put(i, jLabel);
      } catch (IOException e) {

      }
    }
  }

  public void displayCaves() {
    // setDungeonPanel();
//    isVisited.put(model.getPlayer().getCurrentCave().getCaveId(), true);
//    for (Map.Entry<Integer, Boolean> set : isVisited.entrySet()) {
//      if (isVisited.get(set.getKey())) {
//        jLabel = new JLabel();
//        jPanel.add(jLabel);
//        image = "";
//        if (model.getListOfCaves().get(set.getKey()).getCaveList().containsKey(Direction.NORTH)) {
//          image = image + "N";
//        }
//
//        if (model.getListOfCaves().get(set.getKey()).getCaveList().containsKey(Direction.EAST)) {
//          image = image + "E";
//        }
//        if (model.getListOfCaves().get(set.getKey()).getCaveList().containsKey(Direction.SOUTH)) {
//          image = image + "S";
//        }
//
//        if (model.getListOfCaves().get(set.getKey()).getCaveList().containsKey(Direction.WEST)) {
//          image = image + "W";
//        }
//
//        image = "img/" + image + ".png";
//        try {
//          myPicture = ImageIO.read(new File(image));
//          jLabel.setIcon(new ImageIcon(myPicture));
//          jPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
//          jPanel.add(jLabel);
//
//          bufferedImages[model.getListOfCaves().get(set.getKey()).getCaveId()] = myPicture;
//
//        } catch (IOException e) {
//
//        }
//        arr.put(model.getListOfCaves().get(set.getKey()).getCaveId(), jLabel);
//        jLabel.setVisible(false);
//
//      }
//    }
  }

  public void showCaveDetails() {
    isVisited.put(model.getPlayer().getCurrentCave().getCaveId(), true);
    for (Map.Entry<Integer, Boolean> set : isVisited.entrySet()) {
      if (isVisited.get(set.getKey())) {
        jLabel = arr.get(set.getKey());
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

          if (model.getPlayer().getCurrentCave().getCaveId() == set.getKey()) {
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
          arr.put(set.getKey(),jLabel);
          jPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        } catch (IOException e) {

        }
      }

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
    System.out.println("Calling");
    for (Map.Entry<Direction, Location> locationEntry : model.getPlayer().getCurrentCave().getCaveList().entrySet()) {
      System.out.println("curr" + model.getPlayer().getCurrentCave().getCaveId());
      //arr.get(locationEntry.getValue().getCaveId());
      arr.get(locationEntry.getValue().getCaveId()).addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          super.mouseClicked(e);
          System.out.println("Here");
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

  private BufferedImage overlay(BufferedImage starting, String image, int offset) throws
          IOException {
    BufferedImage overlay = ImageIO.read(new File(image));
    int w = Math.max(starting.getWidth(), overlay.getWidth());
    int h = Math.max(starting.getHeight(), overlay.getHeight());
    BufferedImage combinedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    Graphics g = combinedImg.getGraphics();
    g.drawImage(starting, 0, 0, null);
    g.drawImage(overlay, offset, offset, null);
    return combinedImg;
  }


}
