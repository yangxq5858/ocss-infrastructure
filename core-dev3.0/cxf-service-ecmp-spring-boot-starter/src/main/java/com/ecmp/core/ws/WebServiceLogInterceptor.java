package com.ecmp.core.ws;

import com.ecmp.log.util.LogUtil;
import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedWriter;
import org.apache.cxf.io.DelegatingInputStream;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import java.io.InputStream;
import java.io.Reader;
import java.io.SequenceInputStream;

/**
 * 通过PhaseInterceptor指定拦截器在哪个阶段起作用
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/5/21 15:24
 */
public class WebServiceLogInterceptor extends AbstractPhaseInterceptor<Message> {
    protected int limit = 48 * 1024;

    /**
     * 显式调用父类有参构造器，因为AbstractPhaseInterceptor没有无参构造器
     */
    public WebServiceLogInterceptor() {
        super(Phase.RECEIVE);
    }

    /**
     * 实现自己的拦截器的时候需要实现此方法，其中的形参就是被拦截到的SOAP消息
     * Intercepts a message.
     * Interceptors should NOT invoke handleMessage or handleFault
     * on the next interceptor - the interceptor chain will
     * take care of this.
     *
     * @param message message
     */
    @Override
    public void handleMessage(Message message) throws Fault {
//        if (message.containsKey(MessageData.ID_KEY)) {
//            return;
//        }
//        String id = (String) message.getExchange().get(MessageData.ID_KEY);
//        if (id == null) {
//            id = MessageData.nextId();
//            message.getExchange().put(MessageData.ID_KEY, id);
//        }
//        message.put(MessageData.ID_KEY, id);
//        final MessageData buffer = new MessageData("Inbound Message\n\r", id);
//
//        if (!Boolean.TRUE.equals(message.get(Message.DECOUPLED_CHANNEL_MESSAGE))) {
//            // avoid logging the default responseCode 200 for the decoupled responses
//            Integer responseCode = (Integer) message.get(Message.RESPONSE_CODE);
//            if (responseCode != null) {
//                buffer.getResponseCode().append(responseCode);
//            }
//        }
//
//        String encoding = (String) message.get(Message.ENCODING);
//
//        if (encoding != null) {
//            buffer.getEncoding().append(encoding);
//        }
//        String httpMethod = (String) message.get(Message.HTTP_REQUEST_METHOD);
//        if (httpMethod != null) {
//            buffer.getHttpMethod().append(httpMethod);
//        }
//        String ct = (String) message.get(Message.CONTENT_TYPE);
//        if (ct != null) {
//            buffer.getContentType().append(ct);
//        }
//        Object headers = message.get(Message.PROTOCOL_HEADERS);
//
//        if (headers != null) {
//            buffer.getHeader().append(headers);
//        }
//        String uri = (String) message.get(Message.REQUEST_URL);
//        if (uri == null) {
//            String address = (String) message.get(Message.ENDPOINT_ADDRESS);
//            uri = (String) message.get(Message.REQUEST_URI);
//            if (uri != null && uri.startsWith("/")) {
//                if (address != null && !address.startsWith(uri)) {
//                    if (address.endsWith("/") && address.length() > 1) {
//                        address = address.substring(0, address.length());
//                    }
//                    uri = address + uri;
//                }
//            } else {
//                uri = address;
//            }
//        }
//        if (uri != null) {
//            buffer.getAddress().append(uri);
//            String query = (String) message.get(Message.QUERY_STRING);
//            if (query != null) {
//                buffer.getAddress().append("?").append(query);
//            }
//        }
//
//        InputStream is = message.getContent(InputStream.class);
//        if (is != null) {
//            logInputStream(message, is, buffer, encoding);
//        } else {
//            Reader reader = message.getContent(Reader.class);
//            if (reader != null) {
//                try {
//                    CachedWriter writer = new CachedWriter();
//                    IOUtils.copyAndCloseInput(reader, writer);
//                    message.setContent(Reader.class, writer.getReader());
//
//                    if (writer.getTempFile() != null) {
//                        //large thing on disk...
//                        buffer.getMessage().append("\nMessage (saved to tmp file):\n");
//                        buffer.getMessage().append("Filename: ").append(writer.getTempFile().getAbsolutePath()).append("\n");
//                    }
//                    if (writer.size() > limit && limit != -1) {
//                        buffer.getMessage().append("(message truncated to ").append(limit).append(" bytes)\n");
//                    }
//                    writer.writeCacheTo(buffer.getPayload(), limit);
//                } catch (Exception e) {
//                    throw new Fault(e);
//                }
//            }
//        }
//        //TODO 后期考虑kafka
//        LogUtil.info(buffer.toString());
    }

    protected void logInputStream(Message message, InputStream is, MessageData buffer,
                                  String encoding) {
        CachedOutputStream bos = new CachedOutputStream();
        try {
            // use the appropriate input stream and restore it later
            InputStream bis = is instanceof DelegatingInputStream
                    ? ((DelegatingInputStream) is).getInputStream() : is;


            //only copy up to the limit since that's all we need to log
            //we can stream the rest
            IOUtils.copyAtLeast(bis, bos, limit == -1 ? Integer.MAX_VALUE : limit);
            bos.flush();
            bis = new SequenceInputStream(bos.getInputStream(), bis);

            // restore the delegating input stream or the input stream
            if (is instanceof DelegatingInputStream) {
                ((DelegatingInputStream) is).setInputStream(bis);
            } else {
                message.setContent(InputStream.class, bis);
            }

            if (bos.getTempFile() != null) {
                //large thing on disk...
                buffer.getMessage().append("\nMessage (saved to tmp file):\n");
                buffer.getMessage().append("Filename: ").append(bos.getTempFile().getAbsolutePath()).append("\n");
            }
            if (bos.size() > limit && limit != -1) {
                buffer.getMessage().append("(message truncated to ").append(limit).append(" bytes)\n");
            }

            if (StringUtils.isEmpty(encoding)) {
                bos.writeCacheTo(buffer.getPayload(), limit);
            } else {
                bos.writeCacheTo(buffer.getPayload(), encoding, limit);
            }

            bos.close();
        } catch (Exception e) {
            throw new Fault(e);
        }
    }

}
