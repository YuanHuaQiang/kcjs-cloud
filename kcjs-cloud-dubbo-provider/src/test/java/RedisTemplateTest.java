import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SpringBootTest
public class RedisTemplateTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 1. String 操作
    @Test
    public void testString() {
        redisTemplate.opsForValue().set("stringKey", "hello world", Duration.ofMinutes(5));
        Object value = redisTemplate.opsForValue().get("stringKey");
        System.out.println("String: " + value);
    }

    // 2. Hash 操作
    @Test
    public void testHash() {
        String key = "hashKey";
        redisTemplate.opsForHash().put(key, "name", "John");
        redisTemplate.opsForHash().put(key, "age", "30");

        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        System.out.println("Hash: " + map);
    }

    // 3. List 操作
    @Test
    public void testList() {
        String key = "listKey";
        redisTemplate.opsForList().leftPushAll(key, "a", "b", "c");
        List<Object> list = redisTemplate.opsForList().range(key, 0, -1);
        System.out.println("List: " + list);
    }

    // 4. Set 操作
    @Test
    public void testSet() {
        String key = "setKey";
        redisTemplate.opsForSet().add(key, "x", "y", "z");
        Set<Object> members = redisTemplate.opsForSet().members(key);
        System.out.println("Set: " + members);
    }

    // 5. ZSet（有序集合）操作
    @Test
    public void testZSet() {
        String key = "zsetKey";
        redisTemplate.opsForZSet().add(key, "user1", 100);
        redisTemplate.opsForZSet().add(key, "user2", 90);
        Set<Object> zset = redisTemplate.opsForZSet().range(key, 0, -1);
        System.out.println("ZSet: " + zset);
    }

    // 6. Stream 操作（Redis 5.0+）
    @Test
    public void testStream() {
        String streamKey = "streamKey";
        Map<String, String> data = new HashMap<>();
        data.put("field1", "value1");
        data.put("field2", "value2");

        RecordId recordId = redisTemplate.opsForStream().add(StreamRecords.mapBacked(data).withStreamKey(streamKey));
        System.out.println("Stream Record ID: " + recordId);

        List<MapRecord<String, Object, Object>> records =
                redisTemplate.opsForStream().range(streamKey, Range.unbounded());
        for (MapRecord<String, Object, Object> record : records) {
            System.out.println("Stream Record: " + record);
        }
    }

    // 7. HyperLogLog 操作（基数估算）
    @Test
    public void testHyperLogLog() {
        String key = "hyperKey";
        for (int i = 0; i < 1000; i++) {
            redisTemplate.opsForHyperLogLog().add(key, "user" + i);
        }

        Long count = redisTemplate.opsForHyperLogLog().size(key);
        System.out.println("HyperLogLog estimated size: " + count);
    }

    // 8. Bitmap 操作（二进制位存储）
    @Test
    public void testBitMap() {
        String key = "bitmapKey";
        redisTemplate.opsForValue().setBit(key, 1, true);
        redisTemplate.opsForValue().setBit(key, 3, true);

        Boolean bit1 = redisTemplate.opsForValue().getBit(key, 1);
        Boolean bit2 = redisTemplate.opsForValue().getBit(key, 2);
        System.out.println("Bit 1: " + bit1 + ", Bit 2: " + bit2);
    }
}