package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


import control.command.Move;
import control.command.PickArrow;
import control.command.PickTreasure;
import control.command.ShootArrow;
import dungeon.Dungeon;
import dungeon.DungeonImpl;
import location.Direction;
import randomiser.ActualRandomiser;
import view.ButtonListener;
import view.DungeonGridView;
import view.DungeonView;
import view.KeyboardListener;

/**
 * DungeonController is the implementation of the IDungeonController which calls the command
 * functions depending on the input given by the user.
 */
public class DungeonController implements IDungeonController, ActionListener {

  private final Appendable out;
  private final Scanner scan;
  private Dungeon model;
  private DungeonView view;
  private boolean shootMonster;
  private int shootingDistance;

  /**
   * Constructor for the controller.
   *
   * @param in  the source to read from
   * @param out the target to print to
   */
  public DungeonController(Readable in, Appendable out) {
    if (in == null || out == null) {
      throw new IllegalArgumentException("Readable and Appendable can't be null");
    }
    this.out = out;
    scan = new Scanner(in);
  }

  public DungeonController(Dungeon model, DungeonView view) {
    out = null;
    scan = null;
    this.model = model;
    this.view = view;
    this.shootMonster = false;
    this.shootingDistance = -1;
    configureKeyBoardListener();
    configureButtonListener();
  }

  @Override
  public void play(Dungeon dungeon) throws IllegalArgumentException {

    if (dungeon == null) {
      throw new IllegalArgumentException("The model cannot be null");
    }
    DungeonCommandController cmd = null;

    try {
      while (!dungeon.hasReached() && dungeon.getPlayer().getPlayerLive() && !dungeon.hasFallenIntoPit()) {

        out.append(dungeon.getPlayerDesc());
        out.append(dungeon.getLocationDesc());
        out.append("What do you want to do next ? M/P/S").append("\n");
        if (!scan.hasNext()) {
          break;
        }
        String inputMove = scan.next();

        switch (inputMove) {
          case "q":
          case "Q":
            out.append("Game quit!");
            return;

          case "M":
          case "m":
            out.append("Enter the direction:").append("\n");
            String input = scan.next();
            cmd = new Move(input);
            break;

          case "S":
          case "s":
            out.append("Enter distance:").append("\n");
            int dis = scan.nextInt();
            out.append("Enter the direction:").append("\n");
            input = scan.next();
            Direction direction;
            if (input.equals("E")) {
              direction = Direction.EAST;
            } else if (input.equals("W")) {
              direction = Direction.WEST;
            } else if (input.equals("S")) {
              direction = Direction.SOUTH;
            } else if (input.equals("N")) {
              direction = Direction.NORTH;
            } else {
              direction = null;
              out.append("The entered direction is wrong").append("\n");
            }

            cmd = new ShootArrow(dis, direction);
            break;
          case "P":
          case "p":
            out.append("What?").append("\n");
            input = scan.next();
            if (input.equals("Arrow")) {
              cmd = new PickArrow();
            } else if (input.equals("Treasure")) {
              cmd = new PickTreasure();

            } else {
              out.append("Invalid entry!!").append("\n");
            }
            break;
          default:
            out.append(String.format("Unknown command %s", inputMove)).append("\n");
            cmd = null;
            break;
        }
        if (cmd != null) {
          out.append(cmd.goPlay(dungeon)).append("\n");
          cmd = null;
        }
      }
      if (!dungeon.getPlayer().getPlayerLive()) {
        out.append("Player is dead!!");
      } else if (dungeon.hasReached()) {
        out.append("Player has reached the final destination!!!!");
      } else if (dungeon.hasFallenIntoPit()) {
        out.append("Player has fallen into the pit!!Game Over!!");
      }
    } catch (IOException ioe) {
      throw new IllegalStateException("Append failed ", ioe);
    } catch (IllegalArgumentException exception) {
      throw new IllegalArgumentException(exception.getMessage());
    }
  }

  @Override
  public void playView() {
    view.addActionListener(this);
    view.addClickListener(this);
    view.makeVisible();
  }

  private void configureKeyBoardListener() {

    Map<Character, Runnable> keyTypes = new HashMap<>();
    Map<Integer, Runnable> keyPresses = new HashMap<>();
    Map<Integer, Runnable> keyReleases = new HashMap<>();

    keyPresses.put(KeyEvent.VK_UP, () -> {
              if (shootMonster) {
                DungeonCommandController cmd = new ShootArrow(shootingDistance, Direction.NORTH);
                String move = cmd.goPlay(model);
                view.setString(move);
                this.shootMonster = false;
                view.resetFocus();
              } else {
                DungeonCommandController cmd = new Move("N");
                String move = cmd.goPlay(model);
                view.setString(move);
                view.resetFocus();
              }

            }
    );

    keyPresses.put(KeyEvent.VK_DOWN, () -> {
              if (shootMonster) {
                DungeonCommandController cmd = new ShootArrow(shootingDistance, Direction.SOUTH);
                String move = cmd.goPlay(model);
                view.setString(move);
                this.shootMonster = false;
                view.resetFocus();
              } else {
                DungeonCommandController cmd = new Move("S");
                String move = cmd.goPlay(model);
                view.setString(move);
                view.resetFocus();
              }
            }
    );

    keyPresses.put(KeyEvent.VK_RIGHT, () -> {
              if (shootMonster) {
                DungeonCommandController cmd = new ShootArrow(shootingDistance, Direction.EAST);
                String move = cmd.goPlay(model);
                view.setString(move);
                this.shootMonster = false;
                view.resetFocus();

              } else {
                DungeonCommandController cmd = new Move("E");
                String move = cmd.goPlay(model);
                view.setString(move);
                view.resetFocus();
              }

            }
    );

    keyPresses.put(KeyEvent.VK_LEFT, () -> {
              if (shootMonster) {
                DungeonCommandController cmd = new ShootArrow(shootingDistance, Direction.WEST);
                String move = cmd.goPlay(model);
                view.setString(move);
                this.shootMonster = false;
                view.resetFocus();

              } else {
                DungeonCommandController cmd = new Move("W");
                String move = cmd.goPlay(model);
                view.setString(move);
                view.resetFocus();
              }
            }
    );

    keyPresses.put(KeyEvent.VK_A, () -> {
              DungeonCommandController cmd = new PickArrow();
              String move = cmd.goPlay(model);
              view.setString(move);
              view.resetFocus();
            }
    );

    keyPresses.put(KeyEvent.VK_T, () -> {
              DungeonCommandController cmd = new PickTreasure();
              String move = cmd.goPlay(model);
              view.setString(move);
              view.resetFocus();
            }
    );

    keyPresses.put(KeyEvent.VK_1, () -> {
              if (shootMonster) {
                view.setString("Distance: 1, Enter Direction:");
                this.shootingDistance = 1;
              }

            }
    );

    keyPresses.put(KeyEvent.VK_2, () -> {
              if (shootMonster) {
                view.setString("Distance: 2, Enter Direction:");
                this.shootingDistance = 2;
              }
            }
    );

    keyPresses.put(KeyEvent.VK_3, () -> {
              if (shootMonster) {
                view.setString("Distance: 3, Enter Direction:");
                this.shootingDistance = 3;
              }
            }
    );

    keyPresses.put(KeyEvent.VK_4, () -> {
              if (shootMonster) {
                view.setString("Distance: 4, Enter Direction:");
                this.shootingDistance = 4;
              }

            }
    );

    keyPresses.put(KeyEvent.VK_5, () -> {
              if (shootMonster) {
                view.setString("Distance: 5, Enter Direction:");
                this.shootingDistance = 5;
              }

            }
    );

    keyPresses.put(KeyEvent.VK_S, () -> {
              this.shootMonster = true;
              view.setString("Shooting Monster! Enter Distance");
            }
    );


    keyReleases.put(KeyEvent.VK_UP, () -> {
              view.showDungeon();
            }
    );

    keyReleases.put(KeyEvent.VK_DOWN, () -> {
              view.showDungeon();
            }
    );
    keyReleases.put(KeyEvent.VK_RIGHT, () -> {
              view.showDungeon();
            }
    );
    keyReleases.put(KeyEvent.VK_LEFT, () -> {
              view.showDungeon();
            }
    );
    keyReleases.put(KeyEvent.VK_A, () -> {
              view.showDungeon();
            }
    );
    keyReleases.put(KeyEvent.VK_T, () -> {
              view.showDungeon();
            }
    );

    keyReleases.put(KeyEvent.VK_S, () -> {
              view.showDungeon();
            }
    );

    KeyboardListener kbd = new KeyboardListener();
    kbd.setKeyTypedMap(keyTypes);
    kbd.setKeyPressedMap(keyPresses);
    kbd.setKeyReleasedMap(keyReleases);

    view.addKeyListener(kbd);
    view.resetFocus();
  }

  /**
   * Setting up the button listeners.
   */
  private void configureButtonListener() {
    Map<String, Runnable> buttonClickedMap = new HashMap<>();
    ButtonListener buttonListener = new ButtonListener();

    buttonClickedMap.put("Move North Button", () -> {
      DungeonCommandController cmd = new Move("N");
      String setString = cmd.goPlay(model);
      view.setString(setString);
      view.showDungeon();
      view.resetFocus();
    });

    buttonClickedMap.put("Move South Button", () -> {
      DungeonCommandController cmd = new Move("S");
      String setString = cmd.goPlay(model);
      view.setString(setString);
      view.showDungeon();
      view.resetFocus();
    });

    buttonClickedMap.put("Move East Button", () -> {
      DungeonCommandController cmd = new Move("E");
      String setString = cmd.goPlay(model);
      view.setString(setString);
      view.showDungeon();
      view.resetFocus();
    });

    buttonClickedMap.put("Move West Button", () -> {
      DungeonCommandController cmd = new Move("W");
      String setString = cmd.goPlay(model);
      view.setString(setString);
      view.showDungeon();
      view.resetFocus();
    });

    buttonClickedMap.put("Pick Treasure Button", () -> {
      DungeonCommandController cmd = new PickTreasure();
      String setString = cmd.goPlay(model);
      view.setString(setString);
      view.showDungeon();
      view.resetFocus();
    });

    buttonClickedMap.put("Pick Arrow Button", () -> {
      DungeonCommandController cmd = new PickArrow();
      String setString = cmd.goPlay(model);
      view.setString(setString);
      view.showDungeon();
      view.resetFocus();
    });

    buttonClickedMap.put("Shoot Arrow Button", () -> {
      int dis = Integer.parseInt(view.getDistance());
      String input = view.getDirection();
      Direction direction;

      if (input.equals("E")) {
        direction = Direction.EAST;
      } else if (input.equals("W")) {
        direction = Direction.WEST;
      } else if (input.equals("S")) {
        direction = Direction.SOUTH;
      } else if (input.equals("N")) {
        direction = Direction.NORTH;
      } else {
        direction = null;
        view.setString("The entered direction is wrong");
      }
      DungeonCommandController cmd = new ShootArrow(dis, direction);
      String setString = cmd.goPlay(model);
      view.setString(setString);
      view.showDungeon();
      view.resetFocus();
    });

    buttonClickedMap.put("Restart Game Menu", () -> {
      view.setDungeonPreferences();
      this.model = new DungeonImpl(view.getRows(), view.getCols(), view.getInterconnectivity(), view.getWrapping(), view.getTreasurePercent(), new ActualRandomiser(), view.getNumberOfMonsters());
      view.restart(model);
      view.addClickListener(this);
      view.resetFocus();
    });

    buttonClickedMap.put("New Game Menu", () -> {
      view.setDungeonPreferences();
      this.model = new DungeonImpl(view.getRows(), view.getCols(), view.getInterconnectivity(), view.getWrapping(), view.getTreasurePercent(), new ActualRandomiser(), view.getNumberOfMonsters());
      view.restart(model);
      view.addClickListener(this);
      view.resetFocus();
    });

    buttonClickedMap.put("Dungeon Preferences Menu", () -> {
      view.setDungeonPreferences();
      view.resetFocus();
    });

    buttonClickedMap.put("Player Description Button", () -> {
      view.showPlayerDesc();
    });

    buttonListener.setButtonClickedActionMap(buttonClickedMap);
    view.addActionListener(buttonListener);

    view.resetFocus();
  }

  @Override
  public void move(String s){
    DungeonCommandController cmd = new Move(s);
    String move = cmd.goPlay(model);
    System.out.println("current" + model.getPlayer().getCurrentCave().getCaveId());
    view.setString(move);
    view.showDungeon();
    view.resetFocus();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    try {
      view.refresh();
    } catch (Exception ex) {
      view.showErrorMessage(ex.getMessage());
    }
  }
}
