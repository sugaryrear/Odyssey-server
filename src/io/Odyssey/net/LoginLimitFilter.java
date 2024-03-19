package io.Odyssey.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.Odyssey.Configuration;
import io.Odyssey.Server;
import io.Odyssey.net.login.LoginRequestLimit;
import io.Odyssey.net.login.RS2LoginProtocol;
import io.Odyssey.util.logging.global.LoginRequestLog;

public class LoginLimitFilter extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ChannelHandler.incrementActiveConnections();
        String ipAddress = RS2LoginProtocol.getIP(ctx);
        if (LoginRequestLimit.timedOutInvalidLoginRequest(ipAddress)) {
            ctx.close();
            Server.getLogging().batchWrite(new LoginRequestLog(ipAddress, "Invalid login timeout deny"));
            return;
        }

        if (!Configuration.DISABLE_CONNECTION_REQUEST_LIMIT) {
            if (LoginRequestLimit.rejectConnectionRequest()) {
                Server.getLogging().batchWrite(new LoginRequestLog(ipAddress, "Rate limit deny"));
                ctx.close();
                return;
            }

            LoginRequestLimit.addRequest();
        }
    }

}
