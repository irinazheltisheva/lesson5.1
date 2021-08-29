package ru.geekbrains.multithreading;

import static java.lang.System.arraycopy;

class Main {

    private static final int size = 10000000;
    private static final int h = size / 2;
    private static final float[] arr = new float[size];

    public static void main(String[] args) {

        for (int i = 0; i < size; i++) {
            arr[i] = 1;
        }

        long singleTime = singleThread( );
        long multiTime = multiThread( );

        increase(singleTime, multiTime);
    }

    private static long singleThread() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < size; i++) {
            Main.arr[i] = (float) ( Main.arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }

        long singleTime = System.currentTimeMillis() - start;

        System.out.printf("single thread time: %d%n", singleTime);
        return singleTime;
    }

    private static long multiThread() {
        float[] a = new float[h];
        float[] b = new float[h];

        long start = System.currentTimeMillis();

        arraycopy( Main.arr, 0, a, 0, h);
        arraycopy( Main.arr, h, b, 0, h);

        MyThread t1 = new MyThread("a", a);
        MyThread t2 = new MyThread("b", b);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        a = t1.getArr();
        b = t2.getArr();

        arraycopy(a, 0, Main.arr, 0, h);
        arraycopy(b, 0, Main.arr, a.length, b.length);

        long multiTime = System.currentTimeMillis() - start;

        System.out.printf("multi thread time: %d%n", multiTime);

        return multiTime;
    }

    /**
     * Расчитать прирост в %
     */
    private static void increase(long singleTime, long multiTime) {
        double diff = ((double) singleTime / (double) multiTime) - 1;
        int increase = (int) (diff * 100);

        System.out.printf("increase: %d%%%n", increase);
    }
}