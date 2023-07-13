qRPC简介
------

qRPC是一个基于TCP协议、基于Protobuf序列化和代码生成，跨语言的高效率轻量级RPC框架。

框架具有如下特点：

*   基于TCP协议的非阻塞IO(Netty NIO)实现底层网络通信，吞吐量高。


*   基于Protobuf序列化，紧凑高效、高性能、跨语言、可拓展。


*   通过IDL（Interface Define Language）进行接口定义并生成代码


*   跨语言，支持Java、Go、C++、Python等多种语言


使用方法
----

需要了解RPC(Rmote Procedure Call)，会用Protobuf（[Proto3官方文档](https://protobuf.dev/programming-guides/proto3/ "Proto3官方文档")），如果你使用过Google gRPC，你将会非常容易上手。

#### Java:

*   引入Maven依赖

```
<dependency>
<groupId>org.qrpc</groupId>
<artifactId>qrpc-core</artifactId>
<version>1.0.0.SNAPSHOT</version>
</dependency>
```

*   定义数据结构和服务接口

```
Syntax="proto3";
Option Java_Multiple_Files=false;
Option Java_Package="com.qrpc.demo";
Option Java_Outer_Classname="Demo";
Option Java_Generic_Services=true; 
Package qrpc.demo;

Message request {
    string message=1;
}

Message response {
    string message=1;
}
Service DemoService{
    Rpc sendMessage (request) returns (response) {};
}
```

*   生成数据结构和接口定义


```
Protoc -- java out=<path to your source root>-- proto path=<your proto path><proto filename>
```

*   服务端实现接口


```
Public class DemoServiceImpl extensions Demo.DemoService {
    @Overrides
    Public void sendMessage(RpcController controller, Demo.request request, RpcCallback<Demo.response> done){
        System.out.println("received from client, message:"+request. getMessage ());
        Demo.response response = Demo.response.newBuilder().setMessage("hi client").build();
        Done.run(response);
    }
}
```

*   客户端调用服务接口


```
Demo.requestrequest=Demo.request.newBuilder()
    .setMessage("hi, server!")
    .build();

//Synchronous call
UsageDemo.DemoService.BlockingInterface blockingStub=Demo.DemoService.newBlockingStub(SimpleBlockQRpcChannel.forAddress("127.0.0.1", 8888).build());
Demo.response response = blockingStub.sendMessage(null, request);
System.out.println("synchronous response:" + response.getMessage ());

//Asynchronous call
UsageDemo.DemoService.Stub stub = Demo.DemoService.newStub(SimpleQRpcChannel.forAddress("127.0.0.1", 8888).build());
Stub.sendMessage(null, request, new RpcCallback<Demo. response>() {
    @ Override
    Public void run(Demo.response parameter){
        System.out.println ("asynchronous response:"+response. getMessage ());
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



Introduction to qRPC
------
QRPC is an efficient and lightweight RPC framework based on the TCP protocol, Protobuf serialization, and code generation, spanning multiple languages.
The framework has the following characteristics:
*    Non blocking IO (Netty NIO) based on TCP protocol enables low-level network communication with high throughput.
*    Based on Protobuf serialization, it is compact, efficient, high-performance, cross language, and scalable.
*    Define interfaces and generate code through IDL (Interface Definition Language)
*    Cross language support for multiple languages such as Java, Go, C++, Python, etc










Usage
----
Need to understand RPC (Rmote Procedure Call) and use Protobuf ([Proto3 official document]（ https://protobuf.dev/programming-guides/proto3/ If you have used Google gRPC, it will be very easy for you to get started.
####Java:
*   Introducing Maven dependencies
```
<dependency>
<groupId>org.qrpc</groupId>
<artifactId>qrpc-core</artifactId>
<version>1.0.0.SNAPSHOT</version>
</dependency>
```
*   Define data structures and service interfaces
```
Syntax="proto3";
Option Java_Multiple_Files=false;
Option Java_Package="com.qrpc.demo";
Option Java_Outer_Classname="Demo";
Option Java_Generic_Services=true; 
Package qrpc.demo;

Message request {
    string message=1;
}

Message response {
    string message=1;
}
Service DemoService{
    Rpc sendMessage (request) returns (response) {};
}
```
*   Generate data structures and interface definitions
```
Protoc -- java out=<path to your source root>-- proto path=<your proto path><proto filename>
```
*   Server implementation interface
```
Public class DemoServiceImpl extensions Demo.DemoService {
    @Overrides
    Public void sendMessage(RpcController controller, Demo.request request, RpcCallback<Demo.response> done){
        System.out.println("received from client, message:"+request. getMessage ());
        Demo.response response = Demo.response.newBuilder().setMessage("hi client").build();
        Done.run(response);
    }
}
```
*   Client Call Service Interface
```
Demo.requestrequest=Demo.request.newBuilder()
    .setMessage("hi, server!")
    .build();

//Synchronous call
UsageDemo.DemoService.BlockingInterface blockingStub=Demo.DemoService.newBlockingStub(SimpleBlockQRpcChannel.forAddress("127.0.0.1", 8888).build());
Demo.response response = blockingStub.sendMessage(null, request);
System.out.println("synchronous response:" + response.getMessage ());

//Asynchronous call
UsageDemo.DemoService.Stub stub = Demo.DemoService.newStub(SimpleQRpcChannel.forAddress("127.0.0.1", 8888).build());
Stub.sendMessage(null, request, new RpcCallback<Demo. response>() {
    @ Override
    Public void run(Demo.response parameter){
        System.out.println ("asynchronous response:"+response. getMessage ());
    }
});
```
#### Go: TODO
#### C++: TODO


Points to be developed
----
*    Spring boot starter, automatically configured for spring boot, supports seamless integration with spring boot
*    Service governance (supporting service discovery such as ectd and zookeeper), load balancing calls
*    Configure the system, such as configurable parameters such as thread count
*    Cloud native related support
*    Test Cases

prompt
--
The project is in its early stages and the code is easy to read. We welcome programmers and students from all walks of life to join us and learn how to build wheels together. We will combine theory with practice to improve together!