package netty_test.netty_test.myprogream.heart;

import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {
	private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());

	private int counter;

	private byte[] req;

	public TimeClientHandler() {
		req = ("QUERY TIME ORDER" + System.getProperty("line.separator")).getBytes();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 与服务端建立连接后
		System.out.println("客户端 channelActive..");
		ByteBuf message = null;
		for (int i = 0; i < 100; i++) {
			message = Unpooled.buffer(req.length);
			message.writeBytes(req);
			ctx.writeAndFlush(message);
		}
		new Thread(new Test(ctx) {
		}).start();
	}

	private class Test implements Runnable {
		ChannelHandlerContext ctx;

		public Test(ChannelHandlerContext ctx) {
			super();
			this.ctx = ctx;
		}

		public void run() {
			int i = 6;
			while (i-- > 0) {
				try {
					Thread.sleep(4000);
					ByteBuf	message = Unpooled.buffer(req.length);
					message.writeBytes("***dfgdg".getBytes());
					ctx.writeAndFlush(message);
					System.out.println("发送延迟");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("客户端 channelRead..");
		// 服务端返回消息后
		String body = (String) msg;
		System.out.println("当前时间 :" + body + " 次数:" + (++counter));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("客户端异常..");
		// 释放资源
		logger.warning("Unexpected exception from downstream:" + cause.getMessage());
		ctx.close();
	}

}
