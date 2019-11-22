package revol.home.task.controller;

import revol.home.task.dto.ErrorDto;
import revol.home.task.exception.WrongDataException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WrongDataExceptionHandler implements ExceptionMapper<WrongDataException> {
    @Override
    public Response toResponse(WrongDataException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                       .entity(new ErrorDto(exception.getMessage()))
                       .type(MediaType.APPLICATION_JSON)
                       .build();
    }
}
