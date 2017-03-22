package pl.mkoi.project.builders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.mkoi.project.controllers.PageController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipBuilder {
  private static final Logger LOGGER = LoggerFactory.getLogger(PageController.class);

  private ByteArrayOutputStream baos = new ByteArrayOutputStream();
  private ZipOutputStream zos = new ZipOutputStream(baos);

  /**
   * Adds file do zip archive.
   * 
   * @param filename name of file
   * @param filedata data of file
   * @return returns builder
   */
  public ZipBuilder addFile(String filename, byte[] filedata) {

    ZipEntry entry = new ZipEntry(filename);
    try {
      zos.putNextEntry(entry);
      zos.write(filedata);
      zos.closeEntry();
    } catch (IOException e) {
      LOGGER.error("Adding file to zip fails", e);
    }
    return this;

  }

  /**
   * Builds zip file.
   * 
   * @return zip file
   */
  public byte[] build() {
    try {
      zos.close();
    } catch (IOException e) {
      LOGGER.error("Error during close zip stream", e);
    }
    return baos.toByteArray();
  }

}
