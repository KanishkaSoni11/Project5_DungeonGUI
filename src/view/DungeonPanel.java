package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
  private final int row;
  private final int col;
  private JLabel jLabel;
  private final Map<Integer, JLabel> arr;
  private final ReadOnlyDungeonModel model;
  private JPanel jPanel;
  private BufferedImage myPicture;
  private String image;
  private Map<Integer, Boolean> isVisited;
  private Map<Integer, JLabel> neighbourList;
  private DungeonView view;


  public DungeonPanel(ReadOnlyDungeonModel model) {
    super();
    this.row = model.getRow();
    this.col = model.getColumns();
    this.model = model;
     jPanel = new JPanel();
//    jPanel.setLayout(new GridLayout(row, col));
//    jPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    isVisited = new HashMap<>();
    arr = new HashMap<>();
    neighbourList = new HashMap<>();
      this.add(jPanel);

    for (int i = 0; i < row * col; i++) {
      isVisited.put(i, false);
    }

  }


  public void setDungeonPanel() {
    for (int i = 0; i < row * col; i++) {
      jLabel = new JLabel();
      isVisited.put(i, false);
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
    System.out.println("Show cave details");
    isVisited.put(model.getPlayer().getCurrentCave().getCaveId(), true);

    for (int i = 0; i < row * col; i++) {
      JLabel jLabel = new JLabel();
      if (isVisited.get(i)) {
        String image = "";
        if (model.getListOfCaves().get(i).getCaveList().containsKey(Direction.NORTH)) {
          image = image + "N";
        }

        if (model.getListOfCaves().get(i).getCaveList().containsKey(Direction.EAST)) {
          image = image + "E";
        }
        if (model.getListOfCaves().get(i).getCaveList().containsKey(Direction.SOUTH)) {
          image = image + "S";
        }

        if (model.getListOfCaves().get(i).getCaveList().containsKey(Direction.WEST)) {
          image = image + "W";
        }
        image = "img/" + image + ".png";
        try {
          BufferedImage myPicture = ImageIO.read(new File(image));
          if (model.getListOfCaves().get(i).getTreasureList().contains(Treasure.RUBY)) {
            myPicture = overlay(myPicture, "img/ruby.png", 1);
          }
          if (model.getListOfCaves().get(i).getTreasureList().contains(Treasure.DIAMOND)) {
            myPicture = overlay(myPicture, "img/diamond.png", 2);
          }
          if (model.getListOfCaves().get(i).getTreasureList().contains(Treasure.SAPPHIRE)) {
            myPicture = overlay(myPicture, "img/emerald.png", 3);
          }
          if (model.getListOfCaves().get(i).getArrow() > 0) {
            myPicture = overlay(myPicture, "img/arrow-white.png", 4);
          }
          if (model.getListOfCaves().get(i).hasMonster()) {
            myPicture = overlay(myPicture, "img/otyugh.png", 5);
          }

          if (model.getListOfCaves().get(i).getThief()) {
            myPicture = overlay(myPicture, "img/thief.png", 6);
          }

          if (model.getListOfCaves().get(i).getPit()) {
            myPicture = overlay(myPicture, "img/pit.png", 7);
          }

          if (model.getListOfCaves().get(i).hasMovingMonster()) {
            myPicture = overlay(myPicture, "img/monster.png", 7);
          }

          if (model.getPlayer().getCurrentCave().getCaveId() == i) {
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

        } catch (IOException e) {

        }
      } else {
        try {
          jLabel.setIcon(new ImageIcon(ImageIO.read((new File("img/blank.png")))));
        } catch (IOException e) {

        }
      }
      jPanel.add(jLabel);
      arr.put(i, jLabel);
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
