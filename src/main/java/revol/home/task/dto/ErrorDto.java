package revol.home.task.dto;

public final class ErrorDto {
    private String error;

    public ErrorDto() {
    }

    public ErrorDto(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
