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

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class ArrayCache implements Cache {

    public static final int EMPTY = -1;
    private final int size;
    private final int[] ids;
    private final AtomicReferenceArray<byte[]> values;

    public ArrayCache(int size) {
        this.size = size;
        this.ids = new int[size];
        this.values = new AtomicReferenceArray<byte[]>(size);

        Arrays.fill(ids, EMPTY);
    }

    @Override
    public void put(int id, byte[] bytes) {
        for (int i = 0; i < size; i++) {

            if (ids[i] == EMPTY) {
                ids[i] = id;
                values.set(i, bytes);
                return;
            }

            else if (ids[i] == id) {
                values.set(i, bytes);
                return;
            }
        }
    }

    @Override
    public byte[] get(int id) {
        for (int i = 0; i < size; i++) {
            if (ids[i] == id) {
                return values.get(i);
            }
        }

        return null;
    }

}