package org.insightcentre.saffron.web.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import io.swagger.annotations.*;
import org.bson.Document;
import org.glassfish.jersey.server.JSONP;
import org.insightcentre.nlp.saffron.data.Taxonomy;
import org.insightcentre.saffron.web.SaffronData;
import org.insightcentre.saffron.web.mongodb.MongoDBHandler;
import org.json.JSONArray;
import org.json.JSONObject;
@SwaggerDefinition(


        info = @Info(
                title = "User Profile Servlet",
                version = "1.0.0",
                description = "Servlet that handles basic CRUD operations to the user profile data source",
                contact = @Contact(name = "XYZ", email = "XYZ", url = "XYZ"),
                termsOfService = "XYZ",
                license = @License(name = "XYZ", url = "XYZ")
        ),
        basePath = "/",
        consumes = {"application/json"},
        produces = {"application/json"},
        schemes = {SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS},
        tags = {@Tag(name = "users", description = "CRUD operations on user datatype")}
)
@Path("/api/v1/run")
@Api(value = "/user", description = "performs CRUD operations on a user profile")
public class SaffronAPI{

    @GET
    @JSONP
    @Path("/{param}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(httpMethod = "GET", value = "Returns a list of the user profile datatype", notes = "", response = String.class, nickname = "getUser", tags = ("User"))
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succssful retrieval of user profiles", response = String.class),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param", value = "profile id", required = false, dataType = "String", paramType = "query"),
    })
    public Response getRun(@PathParam("param") String name) {
        MongoDBHandler mongo = new MongoDBHandler("localhost", 27017, "saffron", "saffron_runs");
        FindIterable<Document> runs;
        try {
            runs = mongo.getTaxonomy(name);
            for (Document doc : runs) {
                return Response.ok(doc.toJson()).build();
            }
            mongo.close();

        } catch (Exception x) {
            x.printStackTrace();
            System.err.println("Failed to load Saffron from the existing data, this may be because a previous run failed");
        }

        return Response.ok("OK").build();


    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRuns(InputStream incomingData) {
        List<BaseResponse> runsResponse = new ArrayList<>();

        MongoDBHandler mongo = new MongoDBHandler("localhost", 27017, "saffron", "saffron_runs");
        FindIterable<Document> runs;

        try {
            runs = mongo.getAllRuns();

            System.out.println("HERE");
            for (Document doc : runs) {
                BaseResponse entity = new BaseResponse();
                System.out.println(doc.getInteger("occurences"));
                entity.setId(doc.getString("id"));
                entity.setRunDate(doc.getDate("run_date"));
                runsResponse.add(entity);
            }
            mongo.close();
        } catch (Exception x) {
            x.printStackTrace();
            System.err.println("Failed to load Saffron from the existing data, this may be because a previous run failed");
        }


        String json = new Gson().toJson(runsResponse);
        return Response.ok(json).build();
    }

    @DELETE
    @Path("/{param}")
    public Response deleteRun(@PathParam("param") String name) {
        MongoDBHandler mongo = new MongoDBHandler("localhost", 27017, "saffron", "saffron_runs");
        mongo.deleteRun(name);
        return Response.ok("Run " + name + " Deleted").build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postRun(InputStream incomingData) {
        StringBuilder crunchifyBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
            String line = null;
            while ((line = in.readLine()) != null) {
                crunchifyBuilder.append(line);
                System.out.println(line);
            }
        } catch (Exception e) {
            System.out.println("Error Parsing: - ");
        }
        System.out.println("Data Received: " + crunchifyBuilder.toString());


        BaseResponse resp = new BaseResponse();
        resp.setId("1234");
        resp.setRunDate(new Date());
        return Response.ok(resp).build();
    }

    @GET
    @JSONP
    @Path("/{param}/topics")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRunTopics(@PathParam("param") String runId) {
        List<TopicResponse> topicsResponse = new ArrayList<>();
        TopicsResponse resp = new TopicsResponse();
        MongoDBHandler mongo = new MongoDBHandler("localhost", 27017, "saffron", "saffron_runs");
        FindIterable<Document> topics;

        try {
            topics = mongo.getTopics(runId);

            System.out.println("HERE");
            for (Document doc : topics) {
                TopicResponse entity = new TopicResponse();
                System.out.println(doc.getInteger("occurences"));
                entity.setId(doc.getString("_id"));
                entity.setMatches(doc.getInteger("matches"));
                entity.setOccurrences(doc.getInteger("occurences"));
                entity.setScore(doc.getDouble("score"));
                entity.setTopicString(doc.getString("topicString"));
                entity.setMvList((List<String>) doc.get("mvList"));
                topicsResponse.add(entity);
            }

            mongo.close();
        } catch (Exception x) {
            x.printStackTrace();
            System.err.println("Failed to load Saffron from the existing data, this may be because a previous run failed");
        }


        String json = new Gson().toJson(topicsResponse);
        return Response.ok(json).build();
    }


    @DELETE
    @JSONP
    @Path("/{param}/topics/{topic_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteTopic(@PathParam("param") String name,
                                @PathParam("topic_id") String topicId) {

        MongoDBHandler mongo = new MongoDBHandler("localhost", 27017, "saffron", "saffron_runs");
        List<TopicResponse> topicsResponse = new ArrayList<>();
        TopicsResponse resp = new TopicsResponse();
        FindIterable<Document> topics;

        topics = mongo.deleteTopic(name, topicId);

        return Response.ok("Topic " + name + " " + topicId + " Deleted").build();
    }


    @POST
    @Path("/{param}/topics")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response postDeleteManyTopics(@PathParam("param") String name, InputStream incomingData) {


        MongoDBHandler mongo = new MongoDBHandler("localhost", 27017, "saffron", "saffron_runs");
        StringBuilder crunchifyBuilder = getJsonData(incomingData);
        FindIterable<Document> topics;

        System.out.println("Data Received: " + crunchifyBuilder.toString());
        JSONObject jsonObj = new JSONObject(crunchifyBuilder.toString());

        Iterator<String> keys = jsonObj.keys();

            while(keys.hasNext()) {
                String key = keys.next();

                JSONArray obj = (JSONArray) jsonObj.get(key);
                System.out.print("Here:" + obj.toString());
                for (int i = 0; i < obj.length(); i++) {
                    JSONObject json = obj.getJSONObject(i);
                    System.out.println("Here1:" + json.get("id").toString());
                    topics = mongo.deleteTopic(name, json.get("id").toString());


                }
            }



        return Response.ok("Topics " + jsonObj + " Deleted").build();
    }


    @POST
    @Path("/{param}/topics/changeroot")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response postChangeTopicRoot(@PathParam("param") String name, InputStream incomingData) {

        MongoDBHandler mongo = new MongoDBHandler("localhost", 27017, "saffron", "saffron_runs");
        StringBuilder crunchifyBuilder = getJsonData(incomingData);

        System.out.println("Data Received: " + crunchifyBuilder.toString());
        JSONObject jsonRqObj = new JSONObject(crunchifyBuilder.toString());

        Taxonomy finalTaxon = new Taxonomy("", 0.0, 0.0, new ArrayList<>());
        Iterator<String> keys = jsonRqObj.keys();

        try {
            SaffronData data;
            data = SaffronData.fromMongo(name);
            Taxonomy originalTaxo = data.getTaxonomy();
            JSONObject returnJson = new JSONObject();

            JSONArray returnJsonArray = new JSONArray();
            while(keys.hasNext()) {
                String key = keys.next();

                JSONArray obj = (JSONArray) jsonRqObj.get(key);
                for (int i = 0; i < obj.length(); i++) {
                    JSONObject json = obj.getJSONObject(i);
                    String topicString = json.get("id").toString();
                    String newParentString = json.get("new_parent").toString();

                    Taxonomy topic = data.getTaxoDescendent(topicString);
                    Taxonomy newParent = data.getTaxoDescendent(newParentString);
                    newParent = newParent.addChild(topic, newParent);

                    finalTaxon = originalTaxo.deepCopyNewParent(topicString, newParentString, newParent);

                    returnJson.put("id", name);
                    returnJson.put("success", true);
                    returnJson.put("new_parent", newParentString);
                    returnJsonArray.put(returnJson);
                }
            }


            data.setTaxonomy(finalTaxon);
            mongo.updateTaxonomy(name, new Date(), finalTaxon);
            return Response.ok(returnJsonArray.toString()).build();

        } catch (Exception x) {
            x.printStackTrace();
            System.err.println("Failed to load Saffron from the existing data, this may be because a previous run failed");
        }

        return Response.ok(finalTaxon.toString()).build();

    }





    @PUT
    @Path("/{param}/topics/{topic_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response putNewTopic(InputStream incomingData) {
        StringBuilder crunchifyBuilder = getJsonData(incomingData);
        return Response.ok("Topics " + crunchifyBuilder.toString() + " Deleted").build();
    }


    private StringBuilder getJsonData(InputStream incomingData) {
        StringBuilder crunchifyBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
            String line = null;
            while ((line = in.readLine()) != null) {
                crunchifyBuilder.append(line);
            }
        } catch (Exception e) {
            System.out.println("Error Parsing: - ");
        }
        return crunchifyBuilder;
    }

}