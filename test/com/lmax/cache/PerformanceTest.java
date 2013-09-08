/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lmax.cache;

import java.util.concurrent.CountDownLatch;

import static java.lang.Math.round;
import static java.lang.System.out;
import static java.util.concurrent.TimeUnit.SECONDS;

public class PerformanceTest {

    public static final int RUN_SECONDS = 180;
    private static final int SIZE = 16;

    private final Cache cache;
    private final int size;

    public PerformanceTest(Cache cache, int size) {
        this.cache = cache;
        this.size = size;
    }

    public long run() throws InterruptedException {
        out.println("testing " + cache.getClass());
        byte[][] producerByteArrays = createByteArrays();

        CountDownLatch start = new CountDownLatch(2);

        Producer producer = new Producer(cache, producerByteArrays, start);
        Consumer consumer = new Consumer(cache, size, start);
        gc();

        producer.start();
        consumer.start();

        start.await();
        Thread.sleep(SECONDS.toMillis(RUN_SECONDS));

        producer.endRun();
        consumer.endRun();

        return computeAndPrintResults(producer, consumer);
    }

    private byte[][] createByteArrays() {
        byte[][] byteArrays = new byte[size][];

        for (int i = 0; i < size; i++) {
            byteArrays[i] = createBytes((byte) (i + 65));
        }

        return byteArrays;
    }

    private byte[] createBytes(byte i) {
        byte[] bytes = new byte[64];
        for (int j = 0; j < bytes.length; j++) {
            bytes[j] = i;
        }
        return bytes;
    }

    private void gc() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            System.gc();
            Thread.sleep(100);
        }
    }

    private long computeAndPrintResults(Producer producer, Consumer consumer) {
        for (int i = 0; i < consumer.values.length; i++) {
            out.println(new String(consumer.values[i]));
        }

        double megaOpsPerSecond = (1000.0 * producer.numberOfUpdates) / SECONDS.toNanos(RUN_SECONDS);
        out.println(String.format("mops = %.0f", megaOpsPerSecond));

        return round(megaOpsPerSecond);
    }

    private static long run(int runNumber) throws InterruptedException {
        Cache cache = CacheFactory.build(SIZE);
        PerformanceTest test = new PerformanceTest(cache, SIZE);

        out.println("\n======================================= run " + runNumber + " =======================================\n");
        return test.run();
    }

    private static void update(long[] results, long result) {
        System.arraycopy(results, 1, results, 0, results.length - 1);
        results[results.length - 1] = result;
    }

    private static boolean areAllResultsTheSame(long[] results) {
        long oldestResult = results[0];

        for (int i = 1; i < results.length; i++) {
            long result = results[i];

            if (result != oldestResult) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) throws Exception {
        long[] results = new long[3];
        int runNumber = 1;

        do {
            long result = run(runNumber++);
            update(results, result);
            Thread.sleep(5 * 1000);

        } while (!areAllResultsTheSame(results));
    }

}
