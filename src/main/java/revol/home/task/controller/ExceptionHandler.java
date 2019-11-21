package revol.home.task.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {
    private final static Logger LOG = LoggerFactory.getLogger(ExceptionHandler.class);

    @Override
    public Response toResponse(Exception exception) {
        LOG.error("Unexpected exception is caught.", exception);

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity("Internal server error")
                       .type(MediaType.TEXT_PLAIN_TYPE)
                       .build();
    }
}