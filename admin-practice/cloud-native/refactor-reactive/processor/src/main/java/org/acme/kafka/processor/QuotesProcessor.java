package org.acme.kafka.processor;

import java.util.Random;

import jakarta.enterprise.context.ApplicationScoped;

import org.acme.kafka.model.Quote;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.smallrye.reactive.messaging.annotations.Blocking;

/**
 * A bean consuming data from the "quote-requests" Kafka topic (mapped to "requests" channel) and giving out a random quote.
 * The result is pushed to the "quotes" Kafka topic.
 */
@ApplicationScoped
public class QuotesProcessor {
	
	private Random random = new Random();

	@Incoming("requests") // Indicates that the method consumes the items from the requests channel
	@Outgoing("quotes") // Indicates that the objects returned by the method are sent to the quotes channel
	@Blocking // Indicates that the processing is blocking and cannot be run on the caller thread
	public Quote process(String quoteRequest) throws InterruptedException {
		// simulate some hard working task
		Thread.sleep(200);
		return new Quote(quoteRequest, random.nextInt(100));
	}
}

