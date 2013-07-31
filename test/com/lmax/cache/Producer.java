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

class Producer extends Thread {

    private final SerializationCache cache;
    private final byte[][] bytes;

    private volatile boolean stop;

    Producer(SerializationCache cache, byte[][] bytes) {
        super("Producer");
        this.cache = cache;
        this.bytes = bytes;
    }

    @Override
    public void run() {
        long counter = 0;

        while(!stop) {
            int id = computeId(counter++, bytes.length);
            cache.put(id, bytes[id]);
        }
    }

    public void endRun() {
        stop = true;
    }

}