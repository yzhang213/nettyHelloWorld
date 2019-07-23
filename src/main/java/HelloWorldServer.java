import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class HelloWorldServer {
    private  int port;

    public HelloWorldServer(int port) {
        this.port = port;
    }
    public void start(){
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap sbs = new ServerBootstrap().group(bossGroup,workGroup).channel(NioServerSocketChannel.class).
                    localAddress(new InetSocketAddress(port)).childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast("decoder",new StringDecoder());
                    socketChannel.pipeline().addLast("encoder",new StringEncoder());
                    socketChannel.pipeline().addLast(new HelloWorldServerHandler());
                };
            }).option(ChannelOption.SO_KEEPALIVE,true);
            ChannelFuture future = sbs.bind(port).sync();
        } catch (InterruptedException e) {
           bossGroup.shutdownGracefully();
           workGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args)throws Exception {
        int port;
        if (args.length>0){
            port=Integer.parseInt(args[0]);
        }else {
            port=8080;
        }
        new HelloWorldServer(port).start();
    }
}
