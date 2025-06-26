package dev.playground.bucket;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.serialization.Mapper;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
//import io.lettuce.core.RedisClient;

public class Bucket4j {

	public static void main(String[] args) throws InterruptedException {
//		try (var redisClient = RedisClient.create("redis://localhost:6379")) {
//			redisClient.connect();
//
//			var proxyManager = LettuceBasedProxyManager.builderFor(redisClient)
//					.withExpirationStrategy(ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofSeconds(10)))
//					.withKeyMapper(new StringKeyMapper<String>())
//					.build();
//
//			var bucketConfig = BucketConfiguration.builder()
//					.addLimit(Bandwidth.classic(3, Refill.intervally(3, Duration.ofMinutes(1))))
//					.build();
//
//			Runnable task1 = () -> {
//				var keyName = "key1";
//				boolean keepGoing = true;
//				Instant now = Instant.now();
//				int totalConsumed = 0;
//				while (keepGoing) {
//					var bucket = proxyManager.builder()
//							.build(keyName, bucketConfig);
//
//					System.out.println(keyName + ": Available tokens: " + bucket.getAvailableTokens());
//					boolean consumed = bucket.tryConsume(1);
//					if (consumed) {
//						totalConsumed = totalConsumed + 1;
//					}
//					try {
//						Thread.sleep(Duration.ofSeconds(1));
//					} catch (InterruptedException e) {
//						throw new RuntimeException(e);
//					}
//
//					Duration duration = Duration.between(now, Instant.now());
//					if (duration.toMillis() > Duration.ofMinutes(1).toMillis()) {
//						keepGoing = false;
//					}
//				}
//
//				System.out.println(keyName + ": Total consumed: " + totalConsumed);
//			};
//
//			Runnable task2 = () -> {
//				var keyName = "key2";
//				boolean keepGoing = true;
//				Instant now = Instant.now();
//				int totalConsumed = 0;
//				while (keepGoing) {
//					var bucket = proxyManager.builder()
//							.build(keyName, bucketConfig);
//
//					System.out.println(keyName + ": Available tokens: " + bucket.getAvailableTokens());
//					boolean consumed = bucket.tryConsume(1);
//					if (consumed) {
//						totalConsumed = totalConsumed + 1;
//					}
//					try {
//						Thread.sleep(Duration.ofSeconds(1));
//					} catch (InterruptedException e) {
//						throw new RuntimeException(e);
//					}
//
//					Duration duration = Duration.between(now, Instant.now());
//					if (duration.toMillis() > Duration.ofMinutes(1).toMillis()) {
//						keepGoing = false;
//					}
//				}
//
//				System.out.println(keyName + ": Total consumed: " + totalConsumed);
//			};
//
//			ExecutorService executor = Executors.newFixedThreadPool(2);
//			executor.invokeAll(List.of(Executors.callable(task1), Executors.callable(task2)));
//			executor.shutdown();
//		}
	}

	private static class StringKeyMapper<T> implements Mapper<T> {

		@Override
		public byte[] toBytes(final Object value) {
			return ((String) value).getBytes(StandardCharsets.UTF_8);
		}

		@Override
		public String toString(final Object value) {
			return (String) value;
		}
	}

}
