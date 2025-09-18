package gh2;

import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

/**
 * A client that uses the synthesizer package to replicate a plucked guitar string sound
 */
public class GuitarHeroLite {
    private static final double CONCERT_A = 440.0;
    private static final double CONCERT_B = CONCERT_A * Math.pow(2, 2.0 / 12.0);
    private static final double CONCERT_C = CONCERT_A * Math.pow(2, 3.0 / 12.0);
    private static final double CONCERT_D = CONCERT_A * Math.pow(2, 5.0 / 12.0);
    private static final int WIDTH = 512;
    private static final int HEIGHT = 512;

    public static void main(String[] args) {
        /* create two guitar strings, for concert A and C */
        GuitarString stringA = new GuitarString(CONCERT_A);
        GuitarString stringB = new GuitarString(CONCERT_B);
        GuitarString stringC = new GuitarString(CONCERT_C);
        GuitarString stringD = new GuitarString(CONCERT_D);
        StdDraw.setCanvasSize(WIDTH, HEIGHT);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.05);
        StdDraw.text(WIDTH / 2, (HEIGHT + 16) / 2, "Play the guitar!");
        StdDraw.text(WIDTH / 2, (HEIGHT - 32) / 2, "Type A to G");
        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (key == 'a') {
                    StdDraw.clear();
                    StdDraw.text(WIDTH / 2, HEIGHT / 2, "A");

                    StdDraw.show();
                    stringA.pluck();

                } else if (key == 'c') {
                    StdDraw.clear();
                    StdDraw.text(WIDTH / 2, HEIGHT / 2, "C");
                    StdDraw.show();

                    stringC.pluck();
                } else if (key == 'b') {
                    StdDraw.clear();
                    StdDraw.text(WIDTH / 2, HEIGHT / 2, "B");
                    StdDraw.show();

                    stringB.pluck();
                } else if (key == 'd') {
                    StdDraw.clear();
                    StdDraw.text(WIDTH / 2, HEIGHT / 2, "D");
                    StdDraw.show();

                    stringD.pluck();
                }
            }

            /* compute the superposition of samples */
            double sample = stringA.sample() + stringB.sample() + stringC.sample() + stringD.sample();

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            stringA.tic();
            stringB.tic();
            stringC.tic();
            stringD.tic();

        }
    }
}

