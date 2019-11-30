package io.grpc.examples.helloworld;

import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Saluter {
    private static final Logger logger = Logger.getLogger(Saluter.class.getName());
    private HelloWorldClient client;

    Saluter(HelloWorldClient client) {
        this.client = client;
    }

    /**
     * Saluter receives one greeting
     * @param name
     */
    public void salute(String name) {
        logger.info("Will try to greet " + name + " ...");
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply response;
        try {
            response = client.getBlockingStub().sayHello(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Greeting: " + response.getMessage());
    }

    /**
     * Saluter receives multiple greetings
     * @param name
     */
    public void saluteOnceAndRecieveManyGreetings(String name) {
        logger.info("Will try to greet " + name + " ...");
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        Iterator<HelloReply> response;
        try {
            response = client.getBlockingStub().sayHelloManyTimes(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        while(response.hasNext()) {
            logger.info("Greeting: " + response.next().getMessage());
        }
    }

    /**
     * All saluters receive a greeting
     * @param names
     */
    public void greetAllSaluters(String... names) {
        logger.info("Will greet all Saluters in that order");
        final CountDownLatch finishJobLatch = new CountDownLatch(1);

        StreamObserver<HelloRequest> requestStreamObserver = client.getAsyncStub().sayHellToAll(
                // send handler to gather each request that was streamed
                new StreamObserver<HelloReply>() {
                    @Override
                    public void onNext(HelloReply helloReply) {
                        logger.info( "Greeting: " + helloReply.getMessage());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        logger.warning("Failed in streaming requests");
                        finishJobLatch.countDown();
                    }

                    @Override
                    public void onCompleted() {
                        logger.info("Request stream completed");
                        finishJobLatch.countDown();
                    }
                });

        // Stream requests from client
        Random rand = new Random();
        for(String name: names) {
            requestStreamObserver.onNext(HelloRequest.newBuilder().setName(name).build());
        }
        requestStreamObserver.onCompleted();

        try {
//            while (finishJobLatch.getCount()!=0) {}
            finishJobLatch.await(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.warning("Interrupted ");
        }
    }

    /**
     * All Saluters receive greeetings in multiple langs
     * @param names
     */
    public void greetAllSalutersInAllLangs(String... names) {
        logger.info("Will greet all Saluters in that order");
        final CountDownLatch finishJobLatch = new CountDownLatch(1);

        StreamObserver<HelloRequest> requestStreamObserver = client.getAsyncStub().sayHellToAll(
                // send handler to gather each request that was streamed
                new StreamObserver<HelloReply>() {
                    @Override
                    public void onNext(HelloReply helloReply) {
                        logger.info( "Greeting: " + helloReply.getMessage());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        logger.warning("Failed in streaming requests");
                        finishJobLatch.countDown();
                    }

                    @Override
                    public void onCompleted() {
                        logger.info("Request stream completed");
                        finishJobLatch.countDown();
                    }
                });

        // Stream requests from client
        Random rand = new Random();
        for(String name: names) {
            requestStreamObserver.onNext(HelloRequest.newBuilder().setName(name).build());
        }
        requestStreamObserver.onCompleted();

        try {
//            while (finishJobLatch.getCount()!=0) {}
            finishJobLatch.await(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.warning("Interrupted ");
        }
    }
}
