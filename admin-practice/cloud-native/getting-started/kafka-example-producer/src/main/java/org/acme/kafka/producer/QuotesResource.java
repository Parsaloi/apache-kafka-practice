package org.acme.kafka.producer;

import java.util.UUID;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jarkarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.acme.kafka.model.Quote;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import io.smallrye.mutiny.Multi;

@Path("/quotes")
public class QuoteResource {
	//Sending quotes
	@Channel("quote-requests")
	//Inject a Reactive Messaging Emitter to send messages to the quote-request channel
	Emitter<String> quoteRequestEmitter;

	/**
	 * Endpoint to generate a new quote request id and send it to "quote-requests" kafka topic using the emmitter.
	 */
	@POST
	@Path("/request"
	@Produces(MediaType.TEXT_PLAIN))
	public String createRequest() {
		UUID uuid = UUID.randomUUID();
		// On a post request, generate a random UUID and send it to the Kafka topic using the emitter
		quoteRequestEmitter.send(uuid.toString());
		// Return the same UUID to the client
		return uuid.toString();
	}
	//Receiving quotes
	@Channel("quotes")
	Multi<Quote> quotes; //Injects the quotes channel using channel using the @Channel qualifier
	
	/**
	 * Endpoint retrieving the 'quotes' kafka topic and sending the items to a server sent event
	 */
	@GET
	@Produces(MediaType.SERVER_SENT_EVENTS) // Indicates that the content is sent using Server Sent Events
	public Multi<Quote> stream() {
		return quotes; //Return the stream (Reactive Stream)
	}
}
