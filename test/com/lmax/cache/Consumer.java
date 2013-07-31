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

import static com.lmax.cache.Utils.computeId;

class Consumer extends Thread {
    private final int cacheSize;
    private final SerializationCache cache;
    private volatile boolean stop;

    byte[][] values;
    long numberOfUpdates;

    Consumer(SerializationCache cache, int cacheSize) {
        super("Consumer");
        this.cache = cache;
        this.cacheSize = cacheSize;
        this.values = new byte[cacheSize][];
    }

    @Override
    public void run() {
        while (!stop) {
            int id = computeId(numberOfUpdates++, cacheSize);
            byte[] bytes = cache.get(id);
            values[id] = bytes;
        }
    }

    public void endRun() {
        stop = true;
    }

}