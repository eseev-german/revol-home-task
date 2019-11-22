package revol.home.task.controller;

import revol.home.task.dto.ErrorDto;
import revol.home.task.exception.RuntimeSqlException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RuntimeSqlExceptionHandler implements ExceptionMapper<RuntimeSqlException> {
    @Override
    public Response toResponse(RuntimeSqlException exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity(new ErrorDto(exception.getMessage()))
                       .type(MediaType.APPLICATION_JSON)
                       .build();
    }
}
