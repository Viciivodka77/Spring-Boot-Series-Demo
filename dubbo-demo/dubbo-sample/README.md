# 简单的Dubbo demo

基于官方的dubbo-samples-basic

## Step 1 准备依赖

我们首先要准备dubbo和zookeeper以及spring的依赖

```xml
 <dependencies>
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo</artifactId>
        </dependency>

        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-dependencies-zookeeper</artifactId>
            <type>pom</type>
        </dependency>
	
   			<!-- 如果你想要可以增加一个测试依赖 -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

接下来我们需要准备，需要被暴露的服务

<img src="https://gitee.com/ven1ce/picGo/raw/master/img/image-20220224225223776.png" alt="image-20220224225223776" style="zoom:67%;" />

创建api包(提供给consumer)和impl包(由provider提供)

<img src="https://gitee.com/ven1ce/picGo/raw/master/img/image-20220224225254604.png" alt="image-20220224225254604" style="zoom:67%;" />

在其中我们实现一个sayHello方法，在控制台输出`hello world`

之后我们还需要准备log4j的配置，在`resource`文件夹下创建`log4j.properties`

```properties
###set log levels###
log4j.rootLogger=info, stdout
###output to the console###
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target=System.out
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=[%d{dd/MM/yy hh:mm:ss:sss z}] %t %5p %c{2}: %m%n
```



## Step 2 准备Provider

在resource文件夹下新建一个`provider.xml`

<img src="https://gitee.com/ven1ce/picGo/raw/master/img/image-20220224225318237.png" alt="image-20220224225318237" style="zoom:67%;" />

首先我们需要在增加dubbo的xml配置

```xml
xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
xsi:schemaLocation="http://dubbo.apache.org/schema/dubbo http://dubbo.apache.org/schema/dubbo/dubbo.xsd"
```

创建dubbo的应用大致有4步

1. 创建dubbo service

   在xml中添加`<dubbo:application name="demo-provider"/>`

   `dubbo:application`用于配置当前的应用信息，要想使用dubbo就得定义当前的应用信息

   详情查看 https://dubbo.apache.org/zh/docs/references/xml/dubbo-application/

2. 注册地址

   在xml中添加`<dubbo:registry address="N/A" />`

   `dubbo:registry` 用于配置连接注册中心相关信息，通常与zookeeper或者nacos配合。这里暂时不使用zookeeper，设置为`N/A`

3. 注册bean

   在xml中添加`<bean id="demoService" class="org.apache.dubbo.samples.basic.impl.DemoServiceImpl"/>`

   添加实现类至spring容器中

4. 暴露接口

   在xml中添加`<dubbo:service interface="com.sou7h.api.HelloService" ref="helloService" />`

   暴露提供的接口HelloService,其中的ref为接口的实现类，此处的ref与上一步中的id相对应

最终的效果如下

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://dubbo.apache.org/schema/dubbo http://dubbo.apache.org/schema/dubbo/dubbo.xsd">

    <!-- 1.创建dubbo应用 -->
    <dubbo:application name="demo-provider"/>

    <!-- 2.注册地址 N/A采用直连方式 -->
    <dubbo:registry address="N/A" />

    <!-- 3.把impl服务注入spring -->
    <bean id="helloService" class="com.sou7h.impl.HelloServiceImpl" />

    <!-- 4.暴露到dubbo服务 -->
    <dubbo:service interface="com.sou7h.api.HelloService" ref="helloService" />

</beans>
```



接下来就是开启服务

新建一个`Provider`类

```java
/**
 * @author sou7h
 * @description 方法提供者
 * @date 2022年02月22日 10:37 下午
 */
public class Provider {

    public static void main(String[] args) throws IOException {
        //1.获取到Spring容器
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/provider.xml");
        //2.开启服务
        context.start();
        System.out.println("dubbo service started");
        //3.堵塞主线程
        System.in.read();
    }

}
```



启动main方法，我们dubbo服务就启动好了，也可以看到暴露的url

<img src="https://gitee.com/ven1ce/picGo/raw/master/img/image-20220224225401921.png" alt="image-20220224225401921" style="zoom:67%;" />



## Step 3 准备Consumer

与Provider相同，建立`consumer.xml`以及`Consumer`类

在`consumer.xml`中前两步都是创建dubbo服务`dubbo:application`以及设置注册中心`dubbo:register`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://dubbo.apache.org/schema/dubbo http://dubbo.apache.org/schema/dubbo/dubbo.xsd ">


    <!-- 1.创建dubbo服务 -->
    <dubbo:application name="demo-consumer"/>

    <!-- 2.注册中心 -->
    <dubbo:registry address="N/A"/>

    <!-- 3.引用需要使用的service -->
    <dubbo:reference id="helloService" check="true" interface="com.sou7h.api.HelloService"
        url="dubbo://127.0.0.1:20880"/>
</beans>
```

完成前两步后，这里有个新的配置`dubbo:reference`它是消费者需要被引用的服务，与生产者的`dubbo:service`互相对应。

在`Consumer`中我们就可以通过`getBean()`方法获取`helloService`

```java
/**
 * @author sou7h
 * @description TODO
 * @date 2022年02月23日 9:47 下午
 */
public class Consumer {

    public static void main(String[] args) {
      	//获取spring容器
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/consumer.xml");
      	context.start();
      	//获取bean 这里的属性name与xml中的id相对应
        HelloService helloService = (HelloService) context.getBean("helloService");
        helloService.sayHello();
    }

}
```

启动main方法后，我们可以在Provider的控制台看到输出

<img src="https://gitee.com/ven1ce/picGo/raw/master/img/image-20220224225426337.png" alt="image-20220224225426337" style="zoom:67%;" />

## Step 4 加入Zookeeper

进入zookeeper官网下载页面https://zookeeper.apache.org/releases.html下载

解压后我们首先需要在conf文件夹下增加zookeeper的配置

<img src="https://gitee.com/ven1ce/picGo/raw/master/img/image-20220224223147039.png" style="zoom:67%;" />

<img src="https://gitee.com/ven1ce/picGo/raw/master/img/image-20220224223213671.png" style="zoom:67%;" />

其中`zoo_sample.cfg`是官方给的例子

我们仿照它创建`zoo.cfg`文件

<img src="https://gitee.com/ven1ce/picGo/raw/master/img/image-20220224223342782.png" alt="image-20220224223342782" style="zoom: 50%;" />

我们简单的配置zookeeper之后进入到根目录的bin目录下

<img src="https://gitee.com/ven1ce/picGo/raw/master/img/image-20220224223458788.png" alt="image-20220224223458788" style="zoom:67%;" />

在bin目录下我们可以通过`zkServer.sh`启动zookeeper服务，可以通过`zkCli.sh`开启zookeeper客户端连接上zookeeper服务器

<img src="https://gitee.com/ven1ce/picGo/raw/master/img/image-20220224223818374.png" alt="image-20220224223818374" style="zoom:67%;" />

更多zookeeper相关，可以看看官方文档https://zookeeper.apache.org/doc/r3.7.0/zookeeperStarted.html#sc_ProgrammingToZooKeeper，在dubbo的官方案例中https://github.com/apache/dubbo-samples/tree/master/dubbo-samples-basic可以看到如何通过代码启动zookeeper。

到这里我们已经把zookeeper启动起来了，接下来我们只需要在`provider.xml`以及`consumer.xml`中修改注册的配置中心到zk。

provider.xml的修改

<img src="https://gitee.com/ven1ce/picGo/raw/master/img/image-20220224224222269.png" alt="image-20220224224222269" style="zoom:67%;" />

consumer.xml的修改有两处，修改注册中心以及引用服务时的url。

<img src="https://gitee.com/ven1ce/picGo/raw/master/img/image-20220224224311954.png" alt="image-20220224224311954" style="zoom:67%;" />

重新启动Provider后，再次请求Consumer

可以看到成功请求了。

## Step5 Consumer与Provider的传数据与接收数据

向远程提供服务的Provider传数据和平时调用方法一样简单，只需要修改服务接口的方法，增加一个String参数。

获取返回数据也一样，只需要修改服务接口的返回值。

<img src="https://gitee.com/ven1ce/picGo/raw/master/img/image-20220224230016988.png" alt="image-20220224230016988" style="zoom:67%;" />

无论是对于服务的调用方还是提供方而言，整体的调用都是无感的。

当然我们也可以获得更多信息，比如调用方的ip

我们可以通过`RpcContext`获取调用方或者提供方的信息。

值得一提的是在dubbo3中`getContext`方法被弃用了，而官方文档中却依旧使用该方法，在源码的注释中也没有任何解释，或者其他方案的引用，在github的issues中也看到了吐槽https://github.com/apache/dubbo/issues/8063。

<img src="https://gitee.com/ven1ce/picGo/raw/master/img/image-20220224232455505.png" alt="image-20220224232455505" style="zoom:67%;" />

<img src="https://gitee.com/ven1ce/picGo/raw/master/img/image-20220224232739377.png" alt="image-20220224232739377" style="zoom:67%;" />

![image-20220224232813096](https://gitee.com/ven1ce/picGo/raw/master/img/image-20220224232813096.png)



## Step 6 搭配dubbo-admin控制台

下载github上的dubbo-admin

```bash
git clone https://github.com/apache/dubbo-admin.git
```

打开zookeeper

进入`dubbo-admin-server/src/main/resources/application.properties`中查看Zookeeper配置是否正确，并且修改服务端口

<img src="https://gitee.com/ven1ce/picGo/raw/master/img/image-20220228215123923.png" alt="image-20220228215123923" style="zoom: 50%;" />

> 这里修改服务端口是因为开启zookeeper会占用8080端口
>
> 如果需要修改需要在zoo.cfg中添加admin.serverPort=空闲端口

之后我们把项目编译打包

```bash
mvn clean package -Dmaven.test.skip=true
```

启动有两种方式

- `mvn --projects dubbo-admin-server spring-boot:run`
- `cd dubbo-admin-distribution/target`; `java -jar dubbo-admin-0.1.jar`

之后访问你的设置的dubbo-admin端口，我这里是`localhost:8088`

账号和密码在`application.properties`文件中可以随意修改，默认为root和root

<img src="https://gitee.com/ven1ce/picGo/raw/master/img/image-20220228220437076.png" alt="image-20220228220437076" style="zoom:50%;" />

然后我们需要在原来的服务中新增一个标签`dubbo:protocol`

```xml
<dubbo:protocol name="dubbo" port="20881"/>
```

> protocol相关信息https://dubbo.apache.org/zh/docs/references/xml/dubbo-protocol/

port为dubbo协议缺省端口为20880，rmi协议缺省端口为1099，http和hessian协议缺省端口为80；如果**没有**配置port，则自动采用默认端口，如果配置为**-1**，则会分配一个没有被占用的端口。Dubbo 2.4.0+，分配的端口在协议缺省端口的基础上增长，确保端口段可控。

之后启动`Provider`，我们就可以在dubbo-admin上看到注册的服务了。	

![image-20220228221309188](https://gitee.com/ven1ce/picGo/raw/master/img/image-20220228221309188.png)

## Note

需要注意的坑

1. 如果你的电脑上有多环境JDK(我的电脑上安装了JDK8和JDK17)，注意将你的项目JDK版本切换到JDK8

   Idea进入`Project Structure `=>`Project Setting`=>`Project`=>`Project SDK`

2. 开启zookeeper会占用8080端口导致dubbo-admin跑不起来，可以在zoo.cfg中添加admin.serverPort=空闲端口修改zookeeper的端口，也可以修改dubbo-admin服务的端口

3. dubbo-admin会有一个自带的测试服务`org.apache.dubbo.mock.api.MockService`，他占用掉了**20880**端口，在我们自己的服务中可以通过指定协议修改协议端口。

4. 我发现我的消费者在dubbo-admin中显示不出来，而生产者是可以正常显示的，在issues中看到了相同的问题https://github.com/apache/dubbo-admin/issues/162，但是依旧有人反馈看不到。明天再测试一下。

