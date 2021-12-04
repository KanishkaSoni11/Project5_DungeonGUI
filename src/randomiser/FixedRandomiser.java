package randomiser;

import java.util.ArrayList;
import java.util.List;

/**
 * FixedRandomiser is implementation of Randomiser interface which represents the fixed number
 * that we want to pass wherever the object of randomiser is used for testing purpose.
 */

public class FixedRandomiser implements Randomiser {
  private int index;
  private int[] arr;

  /**
   * Constructs the actual number by taking the array of fixed numbers that the user wants to use.
   *
   * @param arr variadic array of fixed numbers.
   */
  public FixedRandomiser(int... arr) {
    this.arr = arr;
    this.index = 0;
  }

  @Override
  public int getRandom(int min, int max) {
    int val = arr[index++];
    index = index >= arr.length ? 0 : index;
    return val;
  }

  @Override
  public <T> void shuffle(List<T> list) {
    new ArrayList<T>(list);
  }
}
