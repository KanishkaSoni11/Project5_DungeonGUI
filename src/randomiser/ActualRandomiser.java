package randomiser;

import java.util.Collections;
import java.util.List;


/**
 * ActualRandomiser is an implementation of Randomiser interface which generates a random
 * number based on whether the range of random number has been passed or not.
 */

public class ActualRandomiser implements Randomiser {

  @Override
  public int getRandom(int min, int max) {
    return (int) Math.floor(Math.random() * (max - min + 1) + min);
  }

  @Override
  public <T> void shuffle(List<T> list) {
    Collections.shuffle(list);
  }

}
