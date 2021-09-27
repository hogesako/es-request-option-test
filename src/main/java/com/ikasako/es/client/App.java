package com.ikasako.es.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;

public class App {
    public static void main(String[] args) throws IOException {
        List<HttpHost> httpHosts = new ArrayList<>(1);
        httpHosts.add(HttpHost.create("localhost:9200"));
        var builder = RestClient.builder(httpHosts.toArray(new HttpHost[0]));

        builder.setDefaultHeaders(new Header[] {new BasicHeader("cluster", "fuga")});
        builder.setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
            .setConnectTimeout(5*1000)
            .setSocketTimeout(30000));
        builder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setKeepAliveStrategy((response, context) -> 30000));
        builder.setCompressionEnabled(false);

        try (var client = new RestHighLevelClient(builder)) {
            var options = RequestOptions.DEFAULT.toBuilder().addHeader("cluster", "foobar").build();
            GetIndexRequest getRequest = new GetIndexRequest("hoge");
            client.indices().get(getRequest, options);
        }
    }
}
