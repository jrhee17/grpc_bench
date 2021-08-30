package helloworld;

import java.util.concurrent.ExecutionException;

import com.linecorp.armeria.common.SessionProtocol;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.grpc.GrpcService;

import io.grpc.examples.helloworld.GreeterGrpc.GreeterImplBase;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;
import io.grpc.stub.StreamObserver;

public class Application {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final GrpcService grpcService = GrpcService.builder().addService(new GreeterImplBase() {
            @Override
            public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
                final HelloReply reply = HelloReply.newBuilder().setMessage(request.getName()).build();
                responseObserver.onNext(reply);
                responseObserver.onCompleted();
            }
        }).build();

        final Server server = Server.builder()
                                    .service(grpcService)
                                    .localPort(50051, SessionProtocol.HTTP)
                                    .build();
        server.start().get();
        server.blockUntilShutdown();
    }
}
