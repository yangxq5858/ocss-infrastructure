package com.ecmp.config.util;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/6/12 13:48
 */
public class ZkClient {

    /**
     * 客户端
     */
    private CuratorFramework client;
    /**
     * Zookeeper服务器地址
     */
    private String zookeeperServer;
    /**
     * 命名空间
     */
    private String namespace;
    /**
     * session超时时间
     */
    private int sessionTimeoutMs = 5000;
    /**
     * 连接超时时间
     */
    private int connectionTimeoutMs = 5000;
    /**
     * 基本重试时间差
     */
    private int baseSleepTimeMs = 1000;
    /**
     * 最大重试次数
     */
    private int maxRetries = 10;

    public ZkClient(String zookeeperServer, String namespace) {
        this.zookeeperServer = zookeeperServer;
        this.namespace = namespace;
    }

    public String getZookeeperServer() {
        return zookeeperServer;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public int getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public int getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setBaseSleepTimeMs(int baseSleepTimeMs) {
        this.baseSleepTimeMs = baseSleepTimeMs;
    }

    public int getBaseSleepTimeMs() {
        return baseSleepTimeMs;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    /**
     * 初始创建客户端
     */
    public void init() {
        //自定义重试机制
        //基本重试间隔时间，重试次数(每次重试时间加长)
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
        client = CuratorFrameworkFactory.builder().namespace(namespace)
                .connectString(zookeeperServer).retryPolicy(retryPolicy)
                .sessionTimeoutMs(sessionTimeoutMs).connectionTimeoutMs(connectionTimeoutMs).build();
        client.start();
    }

    /**
     * 停用
     */
    public void stop() {
        client.close();
    }

    /**
     * @return 返回zk客户端
     */
    public CuratorFramework getClient() {
        return client;
    }

    /**
     * 获取指定路径下的数据
     *
     * @param path 路径
     * @return 返回数据
     * @throws Exception
     */
    public String getData(String path) throws Exception {
        byte[] b = client.getData().forPath(path);
        return new String(b, "UTF-8");
    }

    public void nodeListen(String path, NodeCacheListener listener) throws Exception {
        final NodeCache cache = new NodeCache(client, path);
        cache.start();
        cache.getListenable().addListener(listener);
    }
}
