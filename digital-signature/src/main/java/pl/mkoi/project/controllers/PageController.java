package pl.mkoi.project.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import pl.mkoi.project.builders.ZipBuilder;

public class PageController {

  protected HttpHeaders prepareHeaders(long contentLength, String filename, MediaType contentType) {
    HttpHeaders header = new HttpHeaders();
    header.setContentType(contentType);
    header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=".concat(filename));
    header.setContentLength(contentLength);
    return header;

  }

  protected ZipBuilder getZipBuilder() {
    return new ZipBuilder();
  }

}
