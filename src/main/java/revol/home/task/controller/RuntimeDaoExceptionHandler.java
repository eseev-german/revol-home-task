package revol.home.task.controller;

import revol.home.task.dto.ErrorDto;
import revol.home.task.exception.RuntimeDaoException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RuntimeDaoExceptionHandler implements ExceptionMapper<RuntimeDaoException> {
    @Override
    public Response toResponse(RuntimeDaoException exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity(new ErrorDto(exception.getMessage()))
                       .type(MediaType.APPLICATION_JSON)
                       .build();
    }
}
