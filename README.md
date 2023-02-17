# README

## Note: This is resolved

The issue is caused with the `@Deployment(resources = {"classpath*:**/*.bpmn", "classpath*:**/*.dmn"})` annotation wildcard not having a root directory.

Places bpmn resources in a folder e.g. `src/main/resources/bpmn` and reference as `@Deployment(resources = {""classpath*:/bpmn/**/*.bpmn", "classpath*:/bpmn/**/*.dmn"})`

References:

https://forum.camunda.io/t/error-with-spring-zeebe-app-duplicated-process-id-in-resources-do-nothing-bpmn-and-do-nothing-bpmn/42703

https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#resources-wildcards-in-path-other-stuff


## Steps to reproduce

Start up a Zeebe instance. A docker compose file is provided:

`$ docker compose up`

Build the worker application

`$ ./gradlew bootJar`

Run the following command:

`$ java -jar build/libs/zeebe-deployment-reproducer.jar`

When the `@Deployment` step is hit the application will throw the following error and exit:

```text

org.springframework.context.ApplicationContextException: Failed to start bean 'zeebeClientLifecycle'
        at org.springframework.context.support.DefaultLifecycleProcessor.doStart(DefaultLifecycleProcessor.java:181) ~[spring-context-6.0.4.jar!/:6.0.4]
        at org.springframework.context.support.DefaultLifecycleProcessor$LifecycleGroup.start(DefaultLifecycleProcessor.java:356) ~[spring-context-6.0.4.jar!/:6.0.4]
        at java.base/java.lang.Iterable.forEach(Iterable.java:75) ~[na:na]
        at org.springframework.context.support.DefaultLifecycleProcessor.startBeans(DefaultLifecycleProcessor.java:155) ~[spring-context-6.0.4.jar!/:6.0.4]
        at org.springframework.context.support.DefaultLifecycleProcessor.onRefresh(DefaultLifecycleProcessor.java:123) ~[spring-context-6.0.4.jar!/:6.0.4]
        at org.springframework.context.support.AbstractApplicationContext.finishRefresh(AbstractApplicationContext.java:932) ~[spring-context-6.0.4.jar!/:6.0.4]
        at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:587) ~[spring-context-6.0.4.jar!/:6.0.4]
        at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:730) ~[spring-boot-3.0.2.jar!/:3.0.2]
        at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:432) ~[spring-boot-3.0.2.jar!/:3.0.2]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:308) ~[spring-boot-3.0.2.jar!/:3.0.2]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1302) ~[spring-boot-3.0.2.jar!/:3.0.2]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1291) ~[spring-boot-3.0.2.jar!/:3.0.2]
        at com.example.ZeebeDeploymentReproducerApplication.main(ZeebeDeploymentReproducerApplication.java:14) ~[classes!/:na]
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77) ~[na:na]
        at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
        at java.base/java.lang.reflect.Method.invoke(Method.java:568) ~[na:na]
        at org.springframework.boot.loader.MainMethodRunner.run(MainMethodRunner.java:49) ~[zeebe-deployment-reproducer-0.0.1-SNAPSHOT.jar:na]
        at org.springframework.boot.loader.Launcher.launch(Launcher.java:95) ~[zeebe-deployment-reproducer-0.0.1-SNAPSHOT.jar:na]
        at org.springframework.boot.loader.Launcher.launch(Launcher.java:58) ~[zeebe-deployment-reproducer-0.0.1-SNAPSHOT.jar:na]
        at org.springframework.boot.loader.JarLauncher.main(JarLauncher.java:65) ~[zeebe-deployment-reproducer-0.0.1-SNAPSHOT.jar:na]
Caused by: io.camunda.zeebe.client.api.command.ClientStatusException: Command 'CREATE' rejected with code 'INVALID_ARGUMENT': Expected to deploy new resources, but encountered the following errors:
Duplicated process id in resources 'do-nothing.bpmn' and 'do-nothing.bpmn'
        at io.camunda.zeebe.client.impl.ZeebeClientFutureImpl.transformExecutionException(ZeebeClientFutureImpl.java:93) ~[zeebe-client-java-8.1.7.jar!/:8.1.7]
        at io.camunda.zeebe.client.impl.ZeebeClientFutureImpl.join(ZeebeClientFutureImpl.java:50) ~[zeebe-client-java-8.1.7.jar!/:8.1.7]
        at io.camunda.zeebe.spring.client.annotation.processor.ZeebeDeploymentAnnotationProcessor.lambda$start$7(ZeebeDeploymentAnnotationProcessor.java:119) ~[spring-zeebe-8.1.17.jar!/:na]
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1511) ~[na:na]
        at io.camunda.zeebe.spring.client.annotation.processor.ZeebeDeploymentAnnotationProcessor.start(ZeebeDeploymentAnnotationProcessor.java:100) ~[spring-zeebe-8.1.17.jar!/:na]
        at io.camunda.zeebe.spring.client.annotation.processor.ZeebeAnnotationProcessorRegistry.lambda$startAll$0(ZeebeAnnotationProcessorRegistry.java:38) ~[spring-zeebe-8.1.17.jar!/:na]
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1511) ~[na:na]
        at io.camunda.zeebe.spring.client.annotation.processor.ZeebeAnnotationProcessorRegistry.startAll(ZeebeAnnotationProcessorRegistry.java:38) ~[spring-zeebe-8.1.17.jar!/:na]
        at io.camunda.zeebe.spring.client.lifecycle.ZeebeClientLifecycle.start(ZeebeClientLifecycle.java:49) ~[spring-zeebe-8.1.17.jar!/:na]
        at org.springframework.context.support.DefaultLifecycleProcessor.doStart(DefaultLifecycleProcessor.java:178) ~[spring-context-6.0.4.jar!/:6.0.4]
        ... 20 common frames omitted
Caused by: java.util.concurrent.ExecutionException: io.grpc.StatusRuntimeException: INVALID_ARGUMENT: Command 'CREATE' rejected with code 'INVALID_ARGUMENT': Expected to deploy new resources, but encountered the following errors:
Duplicated process id in resources 'do-nothing.bpmn' and 'do-nothing.bpmn'
        at java.base/java.util.concurrent.CompletableFuture.reportGet(CompletableFuture.java:396) ~[na:na]
        at java.base/java.util.concurrent.CompletableFuture.get(CompletableFuture.java:2073) ~[na:na]
        at io.camunda.zeebe.client.impl.ZeebeClientFutureImpl.join(ZeebeClientFutureImpl.java:48) ~[zeebe-client-java-8.1.7.jar!/:8.1.7]
        ... 28 common frames omitted
Caused by: io.grpc.StatusRuntimeException: INVALID_ARGUMENT: Command 'CREATE' rejected with code 'INVALID_ARGUMENT': Expected to deploy new resources, but encountered the following errors:
Duplicated process id in resources 'do-nothing.bpmn' and 'do-nothing.bpmn'
        at io.grpc.Status.asRuntimeException(Status.java:535) ~[grpc-api-1.49.1.jar!/:1.49.1]
        at io.grpc.stub.ClientCalls$StreamObserverToCallListenerAdapter.onClose(ClientCalls.java:487) ~[grpc-stub-1.49.1.jar!/:1.49.1]
        at io.grpc.internal.DelayedClientCall$DelayedListener$3.run(DelayedClientCall.java:470) ~[grpc-core-1.49.1.jar!/:1.49.1]
        at io.grpc.internal.DelayedClientCall$DelayedListener.delayOrExecute(DelayedClientCall.java:434) ~[grpc-core-1.49.1.jar!/:1.49.1]
        at io.grpc.internal.DelayedClientCall$DelayedListener.onClose(DelayedClientCall.java:467) ~[grpc-core-1.49.1.jar!/:1.49.1]
        at io.grpc.internal.ClientCallImpl.closeObserver(ClientCallImpl.java:563) ~[grpc-core-1.49.1.jar!/:1.49.1]
        at io.grpc.internal.ClientCallImpl.access$300(ClientCallImpl.java:70) ~[grpc-core-1.49.1.jar!/:1.49.1]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInternal(ClientCallImpl.java:744) ~[grpc-core-1.49.1.jar!/:1.49.1]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInContext(ClientCallImpl.java:723) ~[grpc-core-1.49.1.jar!/:1.49.1]
        at io.grpc.internal.ContextRunnable.run(ContextRunnable.java:37) ~[grpc-core-1.49.1.jar!/:1.49.1]
        at io.grpc.internal.SerializingExecutor.run(SerializingExecutor.java:133) ~[grpc-core-1.49.1.jar!/:1.49.1]
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635) ~[na:na]
        at java.base/java.lang.Thread.run(Thread.java:833) ~[na:na]



```
