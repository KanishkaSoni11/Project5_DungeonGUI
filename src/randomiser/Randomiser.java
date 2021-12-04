package randomiser;

import java.util.List;

/**
 * Randomiser to generate random values and  fixed values for testing is implemented in
 * RandomiserInt and gives the desired random value or fixed value based on the object passed.
 */

public interface Randomiser {


  /**
   * Get the random value in the range - min to max.
   *
   * @param min minimum value for the random number
   * @param max maximum value for the random number
   * @return random numberr
   */
  public int getRandom(int min, int max);


  /**
   * Shuffle the given list.
   *
   * @param list list to be shuffled
   * @param <T>  Generic type for the list that has to be shuffled.
   */
  public <T> void shuffle(List<T> list);


}
