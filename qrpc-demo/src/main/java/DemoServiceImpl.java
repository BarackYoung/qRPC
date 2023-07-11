import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/9
 **/
public class DemoServiceImpl extends Demo.DemoService{
    @Override
    public void sendMessage(RpcController controller, Demo.request request, RpcCallback<Demo.response> done) {
        System.out.println("received from client, message: " + request.getMessage());
        Demo.response response = Demo.response.newBuilder().setMessage("hi client").build();
        done.run(response);
    }
}
