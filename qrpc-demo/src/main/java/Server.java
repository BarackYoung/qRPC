import com.qrpc.server.ServerBuilder;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/9
 **/
public class Server {
    public static void main(String[] args) {
        com.qrpc.server.Server server = ServerBuilder.forPort(8888)
                .addService(new DemoServiceImpl())
                .build();
        server.start();
    }
}
