import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class ArrayDeque61BTest {

     @Test
     @DisplayName("ArrayDeque61B has no fields besides backing array and primitives")
     void noNonTrivialFields() {
         List<Field> badFields = Reflection.getFields(ArrayDeque61B.class)
                 .filter(f -> !(f.getType().isPrimitive() || f.getType().equals(Object[].class) || f.isSynthetic()))
                 .toList();

         assertWithMessage("Found fields that are not array or primitives").that(badFields).isEmpty();
     }

     @Test
    // Test the addFirst and addLast method.
    public void addFirstAndRemoveFirstTest() {
         ArrayDeque61B<Integer> d1 = new ArrayDeque61B<>();
         d1.addFirst(0); // [0]
         d1.addFirst(1); // [1, 0]
         d1.addLast(2); // [1, 0, 2]

         assertThat(d1.toList()).containsExactly(1, 0, 2).inOrder();

         // Test the resizing.
         ArrayDeque61B<Integer> d2 = new ArrayDeque61B<>();
         for (int i = 0; i < 350; i ++) {
             d2.addFirst(i);
         }

         assertThat(d2.toList().getFirst()).isEqualTo(349);

         // Test with other data types.
         ArrayDeque61B<String> d3 = new ArrayDeque61B<>();
         d3.addFirst("Otori");
         d3.addLast("Emu!");
         assertThat(d3.toList()).containsExactly("Otori", "Emu!").inOrder();
     }

     @Test
    //Test the get method.
    public void getTest() {
         ArrayDeque61B<Integer> d1 = new ArrayDeque61B<>();
         for (int i = 0; i < 5; i ++) {
             d1.addFirst(i);
         }

         int ele1 = d1.get(0);
         int ele2 = d1.get(3);

         assertThat(ele1).isEqualTo(4);
         assertThat(ele2).isEqualTo(1);
         assertThat(d1.get(6)).isEqualTo(null);

         // Test a huge set for complexity.
         ArrayDeque61B<Integer> d2 = new ArrayDeque61B<>();
         for (int i = 0; i < 50000; i ++) {
             d2.addFirst(i);
         }
         long startTime = System.nanoTime();
         int ele3 = d2.get(1);
         assertThat(ele3).isEqualTo(49998);
         long endTime = System.nanoTime();
         System.out.println(endTime - startTime);

         startTime = System.nanoTime();
         int ele4 = d2.get(40000);
         assertThat(ele4).isEqualTo(9999);
         endTime = System.nanoTime();
         System.out.println(endTime - startTime);
     }

     @Test
    // Test the isEmpty method.
    public void isEmptyTest() {
         ArrayDeque61B<Integer> d1 = new ArrayDeque61B<>();
         boolean checkpoint1 = d1.isEmpty();
         d1.addFirst(0); // [0]
         boolean checkpoint2 = d1.isEmpty();

         assertThat(checkpoint1).isTrue();
         assertThat(checkpoint2).isFalse();
     }

     @Test
    // Test the size method.
    public void sizeTest() {
         ArrayDeque61B<Integer> d1 = new ArrayDeque61B<>();
         int size1 = d1.size();
         assertThat(size1).isEqualTo(0);

         d1.addFirst(2);
         d1.addLast(5);
         int size2 = d1.size();
         assertThat(size2).isEqualTo(2);

         for (int i = 0; i < 10000; i ++) {
             d1.addLast(0);
         }

         int size3 = d1.size();
         assertThat(size3).isEqualTo(10002);
    }

    @Test
    // Test the remove methods.
    public void removeTest() {
        Deque61B<Integer> d1 = new ArrayDeque61B<>();
        d1.addFirst(2);
        d1.addFirst(3);
        d1.addFirst(9);

        int ele1 = d1.removeLast();
        assertThat(ele1).isEqualTo(2);
        assertThat(d1.size()).isEqualTo(2);

        ArrayDeque61B<Integer> d2 = new ArrayDeque61B<>();
        for (int i = 0; i < 50000; i ++) {
            d2.addFirst(i);
        }

        d2.removeFirst();
        d2.removeLast();
        assertThat(d2.get(0)).isEqualTo(49998);
        assertThat(d2.get(d2.size - 1)).isEqualTo(1);

//        for (int i = 0; i < 49900; i ++) {
//            d2.removeLast();
//        }
//        assertThat(d2.underlyingSize).isEqualTo(256);
     }

}