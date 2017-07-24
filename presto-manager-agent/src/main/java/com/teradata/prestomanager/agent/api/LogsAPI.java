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
package com.teradata.prestomanager.agent.api;

import com.teradata.prestomanager.agent.LogsHandler;
import com.teradata.prestomanager.common.JaxrsParameter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.Level;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import java.time.Instant;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

@Path("/logs")
@Api(description = "the logs API")
@javax.annotation.Generated(
        value = "io.swagger.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2017-06-23T09:53:13.549-04:00")
public class LogsAPI
{
    private static final DateTimeFormatter DATE_FORMAT = new DateTimeFormatterBuilder()
            .parseCaseInsensitive().parseStrict()
            .appendPattern("yyyy-MM-dd['T'HH:mm[:ss[")
            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 3, true)
            .appendPattern("][XX][XXX]]]")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
            .parseDefaulting(ChronoField.OFFSET_SECONDS, 0)
            .toFormatter().withChronology(IsoChronology.INSTANCE);

    @GET
    @Path("/{file}")
    @Produces({"text/plain"})
    @ApiOperation(value = "Get Presto log file")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retrieved logs", response = String.class),
            @ApiResponse(code = 400, message = "Invalid parameters"),
            @ApiResponse(code = 404, message = "Resource not found")})
    public Response getLog(
            @PathParam("file") @ApiParam("The name of a file") String file,
            @QueryParam("from") @ApiParam("Ignore logs before this date") @DefaultValue("DEFAULT") DateParameter fromDate,
            @QueryParam("to") @ApiParam("Ignore logs after this date") @DefaultValue("DEFAULT") DateParameter toDate,
            @QueryParam("level") @ApiParam("Only get logs of this level") @DefaultValue("ALL") LevelParameter level,
            @QueryParam("n") @ApiParam("The maximum number of log entries to get") Integer maxEntries)
    {
        return LogsHandler.getLogs(file, fromDate, toDate, level, maxEntries);
    }

    @DELETE
    @Path("/{file}")
    @Produces({"text/plain"})
    @ApiOperation(value = "Delete Presto logs")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Deleted logs"),
            @ApiResponse(code = 400, message = "Invalid parameters"),
            @ApiResponse(code = 404, message = "Resource not found")})
    public Response deleteLog(
            @PathParam("file") @ApiParam("The name of a file") String file,
            @QueryParam("to") @ApiParam("Ignore logs after this date") @DefaultValue("DEFAULT") DateParameter toDate)
    {
        return LogsHandler.deleteLogs(file, toDate);
    }

    public static class DateParameter
            extends JaxrsParameter<Instant>
    {
        public DateParameter(String s)
        {
            super(s);
        }

        @Override
        protected Instant parseString(String s)
                throws ParseException
        {
            try {
                return (s == null || "DEFAULT".equalsIgnoreCase(s))
                        ? null
                        : DATE_FORMAT.parse(s, Instant::from);
            }
            catch (DateTimeParseException e) {
                throw new ParseException();
            }
        }
    }

    public static class LevelParameter
            extends JaxrsParameter<Level>
    {
        public LevelParameter(String s)
        {
            super(s);
        }

        @Override
        protected Level parseString(String s)
                throws ParseException
        {
            try {
                return (s == null) ? null : Level.valueOf(s);
            }
            catch (IllegalArgumentException e) {
                throw new ParseException();
            }
        }
    }
}