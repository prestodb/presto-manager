/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.teradata.prestomanager.controller.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teradata.prestomanager.common.ApiRequester;
import com.teradata.prestomanager.controller.RequestDispatcher;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

import static org.eclipse.jetty.http.HttpMethod.DELETE;
import static org.eclipse.jetty.http.HttpMethod.GET;
import static org.eclipse.jetty.http.HttpMethod.POST;
import static org.eclipse.jetty.http.HttpMethod.PUT;

@Path("/connectors")
@Api(description = "API to read and write to Presto connector files in the specified scope")
@Singleton
public class ControllerConnectorAPI
        extends AbstractControllerAPI
{
    @Inject
    public ControllerConnectorAPI(
            Client forwardingClient,
            RequestDispatcher requestDispatcher)
    {
        super(forwardingClient, requestDispatcher);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Get avaiable connector file names")
    @ApiResponses(value = {
            @ApiResponse(code = 207, message = "Multiple responses available"),
            @ApiResponse(code = 400, message = "Request contains invalid parameters")})
    public Response getConnectors(@QueryParam("scope") String scope,
            @QueryParam("nodeId") List<String> nodeId)
    {
        ApiRequester apiRequester = requesterBuilder(ControllerConnectorAPI.class)
                .httpMethod(GET)
                .accept(MediaType.TEXT_PLAIN)
                .build();

        return forwardRequest(scope, apiRequester, nodeId);
    }

    @GET
    @Path("/{file}")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Get contents of a connector file")
    @ApiResponses(value = {
            @ApiResponse(code = 207, message = "Multiple responses available"),
            @ApiResponse(code = 400, message = "Request contains invalid parameters")})
    public Response getConnectorFile(
            @PathParam("file") String file,
            @QueryParam("scope") String scope,
            @QueryParam("nodeId") List<String> nodeId)
    {
        ApiRequester apiRequester = requesterBuilder(ControllerConnectorAPI.class)
                .pathMethod("getConnectorFile")
                .httpMethod(GET)
                .resolveTemplate("file", file)
                .accept(MediaType.TEXT_PLAIN)
                .build();

        return forwardRequest(scope, apiRequester, nodeId);
    }

    @GET
    @Path("/{file}/{property}")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Get connector property by file")
    @ApiResponses(value = {
            @ApiResponse(code = 207, message = "Multiple responses available"),
            @ApiResponse(code = 400, message = "Request contains invalid parameters")})
    public Response getConnectorProperty(
            @PathParam("file") String file,
            @PathParam("property") String property,
            @QueryParam("scope") String scope,
            @QueryParam("nodeId") List<String> nodeId)
    {
        ApiRequester apiRequester = requesterBuilder(ControllerConnectorAPI.class)
                .pathMethod("getConnectorProperty")
                .httpMethod(GET)
                .resolveTemplate("file", file)
                .resolveTemplate("property", property)
                .accept(MediaType.TEXT_PLAIN)
                .build();

        return forwardRequest(scope, apiRequester, nodeId);
    }

    @PUT
    @Path("/{file}/{property}")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Update or create a certain property of connector file")
    @ApiResponses(value = {
            @ApiResponse(code = 207, message = "Multiple responses available"),
            @ApiResponse(code = 400, message = "Request contains invalid parameters")})
    public Response updateConnectorProperty(
            @PathParam("file") String file,
            @PathParam("property") String property,
            String value,
            @QueryParam("scope") String scope,
            @QueryParam("nodeId") List<String> nodeId)
    {
        ApiRequester apiRequester = requesterBuilder(ControllerConnectorAPI.class)
                .pathMethod("updateConnectorProperty")
                .httpMethod(PUT)
                .resolveTemplate("file", file)
                .resolveTemplate("property", property)
                .accept(MediaType.TEXT_PLAIN)
                .entity(Entity.entity(value, MediaType.TEXT_PLAIN))
                .build();

        return forwardRequest(scope, apiRequester, nodeId);
    }

    @POST
    @Path("/{file}")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Replace this file with the file at the given URL")
    @ApiResponses(value = {
            @ApiResponse(code = 207, message = "Multiple responses available"),
            @ApiResponse(code = 400, message = "Request contains invalid parameters")})
    public Response setConnectorFileByURL(
            String url,
            @PathParam("file") String file,
            @QueryParam("scope") String scope,
            @QueryParam("nodeId") List<String> nodeId)
    {
        ApiRequester apiRequester = requesterBuilder(ControllerConnectorAPI.class)
                .pathMethod("setConnectorFileByURL")
                .httpMethod(POST)
                .resolveTemplate("file", file)
                .accept(MediaType.TEXT_PLAIN)
                .entity(Entity.entity(url, MediaType.TEXT_PLAIN))
                .build();

        return forwardRequest(scope, apiRequester, nodeId);
    }

    @DELETE
    @Path("/{file}")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Delete a connector file")
    @ApiResponses(value = {
            @ApiResponse(code = 207, message = "Multiple responses available"),
            @ApiResponse(code = 400, message = "Request contains invalid parameters")})
    public Response deleteConnectorFile(
            @PathParam("file") String file,
            @QueryParam("scope") String scope,
            @QueryParam("nodeId") List<String> nodeId)
    {
        ApiRequester apiRequester = requesterBuilder(ControllerConnectorAPI.class)
                .pathMethod("deleteConnectorFile")
                .httpMethod(DELETE)
                .resolveTemplate("file", file)
                .accept(MediaType.TEXT_PLAIN)
                .build();

        return forwardRequest(scope, apiRequester, nodeId);
    }

    @DELETE
    @Path("/{file}/{property}")
    @ApiOperation(value = "Delete a certain property of connector file")
    @ApiResponses(value = {
            @ApiResponse(code = 207, message = "Multiple responses available"),
            @ApiResponse(code = 400, message = "Request contains invalid parameters")})
    public Response deleteConnectorProperty(
            @PathParam("file") String file,
            @PathParam("property") String property,
            @QueryParam("scope") String scope,
            @QueryParam("nodeId") List<String> nodeId)
    {
        ApiRequester apiRequester = requesterBuilder(ControllerConnectorAPI.class)
                .pathMethod("deleteConnectorProperty")
                .httpMethod(DELETE)
                .resolveTemplate("file", file)
                .resolveTemplate("property", property)
                .accept(MediaType.TEXT_PLAIN)
                .build();

        return forwardRequest(scope, apiRequester, nodeId);
    }
}
