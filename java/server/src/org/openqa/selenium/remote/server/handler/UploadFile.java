/*
Copyright 2011 Selenium committers
Copyright 2011 Software Freedom Conservancy

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.io.TemporaryFilesystem;
import org.openqa.selenium.io.Zip;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;

import java.io.File;
import java.util.Map;

public class UploadFile extends WebDriverHandler<String> implements JsonParametersAware {

  private String file;

  public UploadFile(Session session) {
    super(session);
  }

  @Override
  public String call() throws Exception {
    TemporaryFilesystem tempfs = getSession().getTemporaryFileSystem();
    File tempDir = tempfs.createTempDir("upload", "file");

    new Zip().unzip(file, tempDir);
    // Select the first file
    File[] allFiles = tempDir.listFiles();
    if (allFiles == null || allFiles.length != 1) {
      throw new WebDriverException("Expected there to be only 1 file. There were: " +
          allFiles.length);
    }

    return allFiles[0].getAbsolutePath();
  }

  public void setJsonParameters(Map<String, Object> allParameters) throws Exception {
    file = (String) allParameters.get("file");
  }
}
