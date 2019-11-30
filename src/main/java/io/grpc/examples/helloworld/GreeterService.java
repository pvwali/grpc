package io.grpc.examples.helloworld;

import io.grpc.stub.StreamObserver;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GreeterService extends GreeterGrpc.GreeterImplBase {
    private static final Logger logger = Logger.getLogger(GreeterService.class.getName());


    /**
     * Send a hello greeting
     * @param request
     * @param responseObserver
     */
    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    /**
     *
     * @param request
     * @param responseObserver
     */
    @Override
    public void sayHelloManyTimes(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
        for (int i=0; i<10; i++) {
            responseObserver.onNext(reply);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.log(Level.WARNING, "Thread Interruped", e.getMessage());
            }
        }
        responseObserver.onCompleted();
    }

    /**
     *
     * @param responseObserver
     * @return
     */
    @Override
    public StreamObserver<HelloRequest> sayHellToAll(final StreamObserver<HelloReply> responseObserver) {
        return new StreamObserver<HelloRequest>() {
            StringBuilder sb = new StringBuilder();

            @Override
            public void onNext(HelloRequest helloRequest) {
                logger.info("Gathering data from stream");
                sb.append("Hello "+helloRequest.getName()+" | ");
            }

            @Override
            public void onError(Throwable throwable) {
                logger.log(Level.WARNING, "Error in gathering streaming data");
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(HelloReply.newBuilder().setMessage(sb.toString()).build());
                responseObserver.onCompleted();
            }
        };
    }

    /**
     *
     * @param responseObserver
     * @return
     */
//    @Override
//    public StreamObserver<HelloRequest> sayHelloToAllInAllLangs(StreamObserver<HelloReply> responseObserver) {
//        return
//    }
}
