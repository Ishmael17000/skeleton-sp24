import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

/** Performs some basic linked list tests. */
public class LinkedListDeque61BTest {

     @Test
     /** In this test, we have three different assert statements that verify that addFirst works correctly. */
     public void addFirstTestBasic() {
         Deque61B<String> lld1 = new LinkedListDeque61B<>();

         lld1.addFirst("back"); // after this call we expect: ["back"]
         assertThat(lld1.toList()).containsExactly("back").inOrder();

         lld1.addFirst("middle"); // after this call we expect: ["middle", "back"]
         assertThat(lld1.toList()).containsExactly("middle", "back").inOrder();

         lld1.addFirst("front"); // after this call we expect: ["front", "middle", "back"]
         assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();

         /* Note: The first two assertThat statements aren't really necessary. For example, it's hard
            to imagine a bug in your code that would lead to ["front"] and ["front", "middle"] failing,
            but not ["front", "middle", "back"].
          */
     }

     @Test
     /** In this test, we use only one assertThat statement. IMO this test is just as good as addFirstTestBasic.
      *  In other words, the tedious work of adding the extra assertThat statements isn't worth it. */
     public void addLastTestBasic() {
         Deque61B<String> lld1 = new LinkedListDeque61B<>();

         lld1.addLast("front"); // after this call we expect: ["front"]
         lld1.addLast("middle"); // after this call we expect: ["front", "middle"]
         lld1.addLast("back"); // after this call we expect: ["front", "middle", "back"]
         assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();
     }

     @Test
     /** This test performs interspersed addFirst and addLast calls. */
     public void addFirstAndAddLastTest() {
         Deque61B<Integer> lld1 = new LinkedListDeque61B<>();

         /* I've decided to add in comments the state after each call for the convenience of the
            person reading this test. Some programmers might consider this excessively verbose. */
         lld1.addLast(0);   // [0]
         lld1.addLast(1);   // [0, 1]
         lld1.addFirst(-1); // [-1, 0, 1]
         lld1.addLast(2);   // [-1, 0, 1, 2]
         lld1.addFirst(-2); // [-2, -1, 0, 1, 2]

         assertThat(lld1.toList()).containsExactly(-2, -1, 0, 1, 2).inOrder();
     }

     // Below, you'll write your own tests for LinkedListDeque61B.

    @Test
    /** This test checks the isEmpty method. */
    public void isEmptyTest() {
        // Initialize an empty list.
        Deque61B<Integer> lld1 = new LinkedListDeque61B<>(); // []
        Boolean checkpoint1 = lld1.isEmpty();  // True
        assertThat(checkpoint1).isTrue();

        // Add something.
        lld1.addFirst(3); // [3]
        Boolean checkpoint2 = lld1.isEmpty(); // False
        assertThat(checkpoint2).isFalse();

        // Delete it and check again.
        lld1.removeFirst(); // []
        Boolean checkpoint3 = lld1.isEmpty(); // True
        assertThat(checkpoint3).isTrue();
    }

    @Test
    /** This test performs removeFirst and removeLast calls. */
    public void removeItemTest() {
        // Initialize the list.
        Deque61B<Integer> lld1 = new LinkedListDeque61B<>(); // []
        for (int i = 0; i < 5; i ++) {
            lld1.addLast(i); // [0, 1, 2, 3, 4]
        }

        int firstElement = lld1.removeFirst(); // 0; list becomes [1, 2, 3, 4]
        assertThat(lld1.toList()).containsExactly(1, 2, 3, 4).inOrder();
        assertThat(firstElement).isEqualTo(0);

        int lastElement = lld1.removeLast(); // 4; list becomes [1, 2, 3]
        assertThat(lld1.toList()).containsExactly(1, 2, 3).inOrder();
        assertThat(lastElement).isEqualTo(4);

        // Now consider some edge cases.

        Deque61B<Integer> lld2 = new LinkedListDeque61B<>();
        Integer a = lld2.removeLast();
        Integer b = lld2.removeFirst();

        assertThat(lld2.isEmpty()).isTrue();
        assertThat(a).isEqualTo(null);
        assertThat(b).isEqualTo(null);
    }

    @Test
    // Test the get method.
    public void getTest() {
        Deque61B<String> lld1 = new LinkedListDeque61B<>();
        lld1.addFirst("Hi");
        lld1.addLast("this");
        lld1.addLast("is");
        lld1.addLast("me");

        String item1 = lld1.get(0); // Hi
        String item2 = lld1.get(3); // me
        String item3 = lld1.get(4); // null
        String item4 = lld1.get(-1); // null

        assertThat(item1).isEqualTo("Hi");
        assertThat(item2).isEqualTo("me");
        assertThat(item3).isEqualTo(null);
        assertThat(item4).isEqualTo(null);
    }

    @Test
    // Test the get method.
    public void getRecursiveTest() {
        Deque61B<String> lld1 = new LinkedListDeque61B<>();
        lld1.addFirst("Hi");
        lld1.addLast("this");
        lld1.addLast("is");
        lld1.addLast("me");

        String item1 = lld1.getRecursive(0); // Hi
        String item2 = lld1.getRecursive(3); // me
        String item3 = lld1.getRecursive(4); // null
        String item4 = lld1.getRecursive(-1); // null

        assertThat(item1).isEqualTo("Hi");
        assertThat(item2).isEqualTo("me");
        assertThat(item3).isEqualTo(null);
        assertThat(item4).isEqualTo(null);
    }
}