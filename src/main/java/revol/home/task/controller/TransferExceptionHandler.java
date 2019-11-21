package revol.home.task.controller;

import revol.home.task.dto.ErrorDto;
import revol.home.task.exception.TransferException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class TransferExceptionHandler implements ExceptionMapper<TransferException> {
    @Override
    public Response toResponse(TransferException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                       .entity(new ErrorDto(exception.getMessage()))
                       .type(MediaType.APPLICATION_JSON)
                       .build();
    }
}
