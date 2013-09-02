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

public class LazyArrayCache implements Cache {

    private static final int EMPTY = -1;
    private final int size;
    private final AtomicReferenceArray<Integer> ids;
    private final AtomicReferenceArray<byte[]> values;

    public LazyArrayCache(int size) {
        this.size = size;
        this.ids = new AtomicReferenceArray<Integer>(size);
        this.values = new AtomicReferenceArray<byte[]>(size);
    }

    @Override
    public void put(Integer id, byte[] bytes) {
        for (int i = 0; i < size; i++) {
            Integer currentId = ids.get(i);

            if (currentId == null) {
                values.lazySet(i, bytes);
                ids.lazySet(i, id);
                return;
            }

            else if (currentId.equals(id)) {
                values.lazySet(i, bytes);
                return;
            }
        }
    }

    @Override
    public byte[] get(Integer id) {
        for (int i = 0; i < size; i++) {
            if (id.equals(ids.get(i))) {
                return values.get(i);
            }
        }

        return null;
    }

}