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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UnitTest {

    private static final byte[] BYTES_1 = "1".getBytes();
    private static final byte[] BYTES_2 = "2".getBytes();

    private SerializationCache cache;

    @Before
    public void beforeEveryTest() {
        cache = SerializationCacheFactory.build(8);
    }

    @Test
    public void shouldReturnNullForUnknownKeys() throws Exception {
        assertNull(cache.get(1));

        cache.put(1, BYTES_1);
        assertNull(cache.get(2));
    }

    @Test
    public void shouldReturnSingleValueForSingleKey() throws Exception {
        cache.put(1, BYTES_1);
        assertContains(1, BYTES_1);
    }

    @Test
    public void shouldReturnUpdatedValueForSingleKey() throws Exception {
        cache.put(3, BYTES_1);
        cache.put(3, BYTES_2);

        assertContains(3, BYTES_2);
    }

    @Test
    public void shouldReturnMultipleValues() throws Exception {
        cache.put(1, BYTES_1);
        cache.put(2, BYTES_2);

        assertContains(1, BYTES_1);
        assertContains(2, BYTES_2);
    }

    @Test
    public void shouldReturnUpdatedValueForMultipleKeys() throws Exception {
        cache.put(1, BYTES_1);
        cache.put(2, new byte[8]);
        cache.put(2, BYTES_2);

        assertContains(1, BYTES_1);
        assertContains(2, BYTES_2);
    }

    private void assertContains(int id, byte[] bytes) {
        assertEquals(bytes, cache.get(id));
    }

}