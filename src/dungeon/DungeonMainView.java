package dungeon;

import control.DungeonController;
import randomiser.ActualRandomiser;
import view.DungeonGridView;
import view.DungeonView;

public class DungeonMainView {

  public static void main(String[] args) {
    Dungeon model = new DungeonImpl(5,6 ,2,false, 50, new ActualRandomiser(), 4);
    DungeonView view = new DungeonGridView(model);
    DungeonController controller = new DungeonController(model,view);
    controller.playView();
  }
}
