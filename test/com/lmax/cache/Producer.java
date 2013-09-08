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

import static com.lmax.cache.Utils.computeId;

class Producer extends Thread {

    private final Cache cache;
    private final byte[][] bytes;
    private final CountDownLatch start;

    private volatile boolean stop;
    long numberOfUpdates;

    Producer(Cache cache, byte[][] bytes, CountDownLatch start) {
        super("Producer");
        this.cache = cache;
        this.bytes = bytes;
        this.start = start;
    }

    @Override
    public void run() {
        synchronizeStart();

        while(!stop) {
            int id = computeId(numberOfUpdates++, bytes.length);
            cache.put(id, bytes[id]);
        }
    }

    private void synchronizeStart() {
        start.countDown();

        try {
            start.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void endRun() {
        stop = true;
    }

}