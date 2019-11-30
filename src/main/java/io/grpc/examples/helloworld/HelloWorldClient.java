package io.grpc.examples.helloworld;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

public class HelloWorldClient {
    private final ManagedChannel channel;
    private final GreeterGrpc.GreeterBlockingStub blockingStub;
    private final GreeterGrpc.GreeterFutureStub futureStub;
    private final GreeterGrpc.GreeterStub asyncStub;

    /** Construct client connecting to HelloWorld server at {@code host:port}. */
    public HelloWorldClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build());
    }

    HelloWorldClient(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = GreeterGrpc.newBlockingStub(channel);
        asyncStub = GreeterGrpc.newStub(channel);
        futureStub = GreeterGrpc.newFutureStub(channel);
    }

    private void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public static void main(String[] args) throws Exception {
        // Access a service running on the local machine on port 50051
        HelloWorldClient client = new HelloWorldClient("localhost", 50051);
        try {
            String user = "Hecate";
            // Use the arg as the name to greet if provided
            if (args.length > 0) {
                user = args[0];
            }

            Saluter saluter = new Saluter(client);
            saluter.salute(user);
            saluter.saluteOnceAndRecieveManyGreetings(user);
            saluter.greetAllSaluters(new String[] {"Zeus", "Poseidon", "Prometheus", "Apollo", "Athena", "Hades", "Janus", "Nemesis"});
//                    "Hello", "Nǐn hǎo", "Bonjour", "Namaste", "Guten Tag", "Ola"});
        } finally {
            client.shutdown();
        }
    }

    public GreeterGrpc.GreeterBlockingStub getBlockingStub() {
        return blockingStub;
    }

    public GreeterGrpc.GreeterFutureStub getFutureStub() {
        return futureStub;
    }

    public GreeterGrpc.GreeterStub getAsyncStub() {
        return asyncStub;
    }
}
