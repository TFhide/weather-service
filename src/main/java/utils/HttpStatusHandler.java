package utils;

import org.springframework.http.HttpStatus;
import ru.task.weatherservice.model.ApiResponse;

import java.net.http.HttpResponse;

public final class HttpStatusHandler {

    private HttpStatusHandler() {
        throw new AssertionError("Utility class shouldn't be instantiated");
    }

    public static ApiResponse handleResponseStatus(String serviceName,HttpResponse<String> response) {
        HttpStatus status = HttpStatus.resolve(response.statusCode());

        if (status != null) {
            switch (status) {
                case BAD_REQUEST:
                    return ApiResponse.error(serviceName,
                            "The request was invalid or cannot be served. Check your request.");
                case UNAUTHORIZED, FORBIDDEN:
                    return ApiResponse.error(serviceName,"Access is denied. Please check your API key.");
                case NOT_FOUND:
                    return ApiResponse.error(serviceName,
                            "The requested resource could not be found but may be available again in the future.");
                case TOO_MANY_REQUESTS:
                    return ApiResponse.error(serviceName,
                            "Too many requests. Please wait and try again later.");
                case INTERNAL_SERVER_ERROR:
                    return ApiResponse.error(serviceName,
                            "Internal server error. Something went wrong on our end.");
                default: {
                    if (status.is2xxSuccessful()) return ApiResponse.ok(serviceName,null);
                    else return ApiResponse.error(serviceName,"Unexpected response status: " + status
                            + " Please contact support.");
                }
            }
        }
        return ApiResponse.error(serviceName,"Unknown response status: " + response.statusCode()
                + " Unable to process the request.");
    }
}
