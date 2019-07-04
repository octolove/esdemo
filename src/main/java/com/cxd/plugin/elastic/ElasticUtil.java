package com.cxd.plugin.elastic;

import java.io.IOException;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

@Component
public class ElasticUtil {

    @Autowired
    private RestHighLevelClient client;

    /**
     * 判断idnex是否存在
     *
     * @param indexName
     * @return
     * @throws IOException`
     */
    public boolean existsIndex(String indexName) throws IOException {
        GetIndexRequest request = new GetIndexRequest();
        request.indices(indexName);
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println("existsIndex: " + exists);
        return exists;
    }

    /**
     * 创建index
     *
     * @param indexName
     * @return
     * @throws IOException
     */
    public CreateIndexResponse createIndec(String indexName) throws IOException {
        if (!existsIndex(indexName)) {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            createIndexRequest.settings(Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 1));
            CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            return createIndexResponse;
        }
        return null;
    }

    /**
     * 创建mapping
     *
     * @param indexName
     * @param mappingType
     * @return
     * @throws IOException
     */
    public void createMapping(String indexName, String mappingType) throws IOException {

        XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject("properties")

        .startObject("uid").field("type", "keyword").field("index", "false").endObject()

        .startObject("name").field("type", "keyword").field("index", "true").endObject()

        .startObject("age").field("type", "byte").field("index", "true").endObject()

        .startObject("phone").field("type", "keyword").field("index", "true").endObject()

        .startObject("address").field("type", "text").field("index", "true").field("analyzer", "ik_max_word").field("search_analyzer", "ik_max_word").endObject()

        .startObject("birtydate").field("type", "date").field("index", "true").endObject()

        .endObject().endObject();

        PutMappingRequest request = new PutMappingRequest(indexName).type(mappingType).source(mapping);
        boolean flag = client.indices().putMapping(request, RequestOptions.DEFAULT).isAcknowledged();
        System.out.println("------------------->>>>>" + flag);
    }

    /**
     * 保存数据
     *
     * @param index
     * @param type
     * @param cus
     */
    public <T> IndexResponse add(String index, String type, T t, String uid) throws IOException {
        IndexRequest indexRequest = new IndexRequest(index, type, uid);
        indexRequest.source(JSON.toJSONString(t), XContentType.JSON);
        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        return indexResponse;
    }

    /**
     * 根据主键查询记录
     *
     * @param index
     * @param type
     * @param id
     * @throws IOException
     */
    public GetResponse get(String index, String type, String id) throws IOException {
        GetRequest getRequest = new GetRequest(index, type, id);
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        return getResponse;
    }

    /**
     * 删除
     *
     * @param index
     * @param type
     * @param id
     * @throws IOException
     */
    public DeleteResponse delete(String index, String type, String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(index, type, id);
        DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        return deleteResponse;
    }

    public DeleteResponse delete(String index) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(index);
        DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        return deleteResponse;
    }

    /**
     * 查询
     * 
     * @param index
     * @throws IOException
     * @return void
     * @throws
     */
    public void query(String index, QueryBuilder queryBuilder) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);

        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(searchSourceBuilder.query(queryBuilder));
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit hit : searchHits) {
            System.out.println("--------结果:--------->" + hit.getSourceAsString());
        }

    }
}
