package netty_test.netty_test.myprogream.heart;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class AcceptorIdleStateTrigger extends ChannelInboundHandlerAdapter {

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		System.out.println("!!!!!!!!!!!!!!");
		if(evt instanceof IdleStateEvent){
			IdleStateEvent event = (IdleStateEvent)evt;
			switch(event.state()){
			case ALL_IDLE:
				System.out.println("读/写超时！！！");
				ctx.channel().close();
				break;
			case READER_IDLE:
				System.out.println("读超时！！！");
				ctx.channel().close();
				break;
			case WRITER_IDLE:
				System.out.println("写超时！！！");
				ctx.channel().close();
				break;
			default:
				break;
			}
		}
	}
}
