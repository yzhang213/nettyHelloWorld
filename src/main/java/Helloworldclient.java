import com.sun.tools.corba.se.idl.InterfaceGen;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


public class Helloworldclient {
    static  final String HOST=System.getProperty("host","127.0.0.1");
    static  final  int PORT =Integer.parseInt(System.getProperty("port","8080"));
    static  final int SIZE = Integer.parseInt(System.getProperty("size","256"));

    public static void main(String[] args) throws Exception{
        EventLoopGroup workergroup = new NioEventLoopGroup();
        try {
            Bootstrap b=new Bootstrap();
            b.group(workergroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE,true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new HelloWorldClientHandler());
                }
            });
            //连接
            ChannelFuture f=b.connect(HOST,PORT).sync();
            f.channel().writeAndFlush("Hello Netty Server ,I am a common client");
            f.channel().closeFuture().sync();

        }finally {
            workergroup.shutdownGracefully();
        }

        }
    }

