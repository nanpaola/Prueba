package com.transport.api.exception;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import java.time.Instant;
import java.util.*;
@RestControllerAdvice public class GlobalExceptionHandler {
 private static final Logger log=LoggerFactory.getLogger(GlobalExceptionHandler.class);
 public record ErrorResponse(Instant timestamp,int status,String error,String message,String path){}
 private ResponseEntity<ErrorResponse> result(HttpStatus s,String msg,String path){return ResponseEntity.status(s).body(new ErrorResponse(Instant.now(),s.value(),s.getReasonPhrase(),msg,path));}
 @ExceptionHandler(ApiException.class) ResponseEntity<ErrorResponse> api(ApiException ex,HttpServletRequest r){return result(ex.status,ex.getMessage(),r.getRequestURI());}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<ErrorResponse> invalid(MethodArgumentNotValidException ex,HttpServletRequest r){String msg=ex.getBindingResult().getFieldErrors().stream().map(f->f.getField()+": "+f.getDefaultMessage()).distinct().reduce((a,b)->a+"; "+b).orElse("Invalid request");return result(HttpStatus.BAD_REQUEST,msg,r.getRequestURI());}
 @ExceptionHandler({HttpMessageNotReadableException.class,IllegalArgumentException.class}) ResponseEntity<ErrorResponse> malformed(Exception ex,HttpServletRequest r){return result(HttpStatus.BAD_REQUEST,"Malformed request or invalid value",r.getRequestURI());}
 @ExceptionHandler(MaxUploadSizeExceededException.class) ResponseEntity<ErrorResponse> large(Exception ex,HttpServletRequest r){return result(HttpStatus.PAYLOAD_TOO_LARGE,"File exceeds configured size",r.getRequestURI());}
 @ExceptionHandler(DataIntegrityViolationException.class) ResponseEntity<ErrorResponse> duplicate(Exception ex,HttpServletRequest r){return result(HttpStatus.CONFLICT,"Data violates a uniqueness or relational constraint",r.getRequestURI());}
 @ExceptionHandler(Exception.class) ResponseEntity<ErrorResponse> unexpected(Exception ex,HttpServletRequest r){log.error("Unhandled request failure",ex);return result(HttpStatus.INTERNAL_SERVER_ERROR,"Unexpected server error",r.getRequestURI());}
}
