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

![image-20220222224616589](https://gitee.com/ven1ce/picGo/raw/master/img/image-20220222224616589.png)

创建api包(提供给consumer)和impl包(由provider提供)

![image-20220222224813406](https://gitee.com/ven1ce/picGo/raw/master/img/image-20220222224813406.png)

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

![image-20220223215511856](https://gitee.com/ven1ce/picGo/raw/master/img/image-20220223215511856.png)

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

![image-20220223225158528](https://gitee.com/ven1ce/picGo/raw/master/img/image-20220223225158528.png)



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

![image-20220223230340483](https://gitee.com/ven1ce/picGo/raw/master/img/image-20220223230340483.png)

