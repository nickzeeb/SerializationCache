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

import java.util.HashMap;

public class SynchronizedMapCache implements Cache {

    private final HashMap<Integer, byte[]> map;

    public SynchronizedMapCache(int size) {
        map = new HashMap<Integer, byte[]>(size);
    }

    @Override
    public synchronized void put(Integer id, byte[] bytes) {
        map.put(id, bytes);
    }

    @Override
    public synchronized byte[] get(Integer id) {
        return map.get(id);
    }

}