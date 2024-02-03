import com.google.protobuf.*;
import org.qrpc.client.Channel.SimpleBlockQRpcChannel;
import org.qrpc.client.Channel.SimpleQRpcChannel;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/9
 **/
public class Client {
    public static void main(String[] args) throws ServiceException {
Demo.request request = Demo.request.newBuilder()
        .setMessage("hi, server!")
        .build();

// synchronous usage
Demo.DemoService.BlockingInterface blockingStub = Demo.DemoService.newBlockingStub(SimpleBlockQRpcChannel.forAddress("127.0.0.1", 8888).build());
Demo.response response = blockingStub.sendMessage(null, request);
System.out.println("synchronous response: " + response.getMessage());

// asynchronous usage
Demo.DemoService.Stub stub = Demo.DemoService.newStub(SimpleQRpcChannel.forAddress("127.0.0.1", 8888).build());
stub.sendMessage(null, request, new RpcCallback<Demo.response>() {
    @Override
    public void run(Demo.response parameter) {
        System.out.println("asynchronous response: " + response.getMessage());
    }
});
    }
}
