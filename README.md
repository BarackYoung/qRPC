qRPC简介
------

qRPC是一个基于TCP协议、基于Protobuf序列化和代码生成，跨语言的高效率轻量级RPC框架。

框架具有如下特点：

*   基于TCP协议的非阻塞IO(NIO)实现底层网络通信，吞吐量高。


*   基于Protobuf序列化，紧凑高效、高性能、跨语言、可拓展。


*   通过IDL（Interface Define Language）进行接口定义并生成代码


*   跨语言，支持Java、Go、C++、Python等多种语言


使用方法
----

需要了解RPC(Rmote Procedure Call)，会用Protobuf（[Proto3官方文档](https://protobuf.dev/programming-guides/proto3/ "Proto3官方文档")），如果你使用过gRPC，你将会非常容易上手。

#### Java:

*   引入Maven依赖


```
        <dependency>
            <groupId>org.qrpc</groupId>
            <artifactId>qrpc-core</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
```

*   定义数据结构和服务接口


```
syntax = "proto3";

option java\_multiple\_files = false;
option java_package = "com.qrpc.demo";
option java\_outer\_classname = "Demo";
option java\_generic\_services = true;package qrpc.demo;

message request {  string message = 1;
}

message response {  string message = 1;
}

service DemoService {
  rpc sendMessage(request) returns (response) {};
}
```

*   生成数据结构和接口定义


```
protoc --java-out=<path to your source root> --proto-path=<your proto path> <proto filename>
```

*   服务端实现接口


```
public class DemoServiceImpl extends Demo.DemoService{    @Override
    public void sendMessage(RpcController controller, Demo.request request, RpcCallback<Demo.response> done) {
        System.out.println("received from client, message: " + request.getMessage());
        Demo.response response = Demo.response.newBuilder().setMessage("hi client").build();
        done.run(response);
    }
}
```

*   客户端调用服务接口


```
Demo.request request = Demo.request.newBuilder()
        .setMessage("hi, server!")
        .build();// synchronous usageDemo.DemoService.BlockingInterface blockingStub = Demo.DemoService.newBlockingStub(SimpleBlockQRpcChannel.forAddress("127.0.0.1", 8888).build());
Demo.response response = blockingStub.sendMessage(null, request);
System.out.println("synchronous response: " + response.getMessage());// asynchronous usageDemo.DemoService.Stub stub = Demo.DemoService.newStub(SimpleQRpcChannel.forAddress("127.0.0.1", 8888).build());
stub.sendMessage(null, request, new RpcCallback<Demo.response>() {    @Override
    public void run(Demo.response parameter) {
        System.out.println("asynchronous response: " + response.getMessage());
    }
});
```

#### Go: TODO

#### C++: TODO

待开发点
----

*   spring-boot-starter，springboot自动配置，支持与springboot无缝结合


*   服务治理（支持ectd、zookeeper等服务发现），负载均衡调用


*   配置系统，比如线程数等参数可配置


*   云原生相关支持


*   测试用例


提示
--

项目处于起步阶段，代码易读懂，欢迎广大社会程序员、学生一起加入，一起学习造轮子，把理论与实践相结合，一起提高！