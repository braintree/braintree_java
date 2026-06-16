package com.braintreegateway.unittest.util;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.braintreegateway.util.MapUtils;

public class MapUtilsTest {

    @Test
    public void toMapBuildsMapFromKeyValuePairs() {
        Map<String, Object> map = MapUtils.toMap("key1", "value1", "key2", 42);

        assertAll("map entries",
                () -> assertEquals(2, map.size()),
                () -> assertEquals("value1", map.get("key1")),
                () -> assertEquals(42, map.get("key2")));
    }

    @Test
    public void toMapReturnsEmptyMapForNoArgs() {
        assertEquals(0, MapUtils.toMap().size());
    }

    @Test
    public void toMapThrowsForOddNumberOfArgs() {
        Exception e = assertThrows(RuntimeException.class, () -> MapUtils.toMap("key1"));
        assertEquals("toMap must be called with an even number of parameters", e.getMessage());
    }
}
