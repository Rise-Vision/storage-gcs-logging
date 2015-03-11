package com.risevision.gcslogs.load;

import com.risevision.gcslogs.auth.CredentialProvider;
import static com.risevision.gcslogs.BuildConfig.*;

import java.util.*;
import java.io.*;
import java.util.logging.Logger;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.services.bigquery.Bigquery;
import com.google.api.services.bigquery.model.*;
import static com.risevision.gcslogs.alert.AlertService.alert;

class BQLoadJobInserter implements LoadJobInserter {
  private Bigquery.Jobs.Insert req;
  private Bigquery bq;
  private Calendar cal = Calendar.getInstance();
  private static final Logger log = Logger.getLogger("gcslogs.BQLoadJobInserter");
  List<TableFieldSchema> usageFields = new ArrayList<>();
  List<TableFieldSchema> storageFields = new ArrayList<>();

  BQLoadJobInserter(CredentialProvider credentialProvider) {
    bq = new Bigquery.Builder(Utils.getDefaultTransport(),
    Utils.getDefaultJsonFactory(), credentialProvider.getCredential())
    .setApplicationName(STORAGE_APP_NAME.toString()).build();

    loadSchemaFiles();
  }

  private void loadSchemaFiles() {
    try {
      File usageSchemaFile = new File(USAGE_LOG_SCHEMA_PATH.toString());
      FileReader fileReader = new FileReader(usageSchemaFile);
      usageFields = new Gson().fromJson
      (fileReader, new TypeToken<List<TableFieldSchema>>(){}.getType());
      fileReader.close();

      File storageSchemaFile = new File(STORAGE_LOG_SCHEMA_PATH.toString());
      fileReader = new FileReader(storageSchemaFile);
      storageFields = new Gson().fromJson
      (fileReader, new TypeToken<List<TableFieldSchema>>(){}.getType());
      fileReader.close();
    } catch (Exception e) {
      alert("Could not load schemas for BQLoadJobInserter.");
      e.printStackTrace();
    }
  }

  public Job insertJob(List<String> fileUris, String type) {
    TableReference tr = new TableReference()
    .setDatasetId(DATASET_ID.toString())
    .setProjectId(PROJECT_ID.toString())
    .setTableId
    (type + "Logs" + cal.get(Calendar.YEAR) + "_" + (cal.get(Calendar.MONTH) + 1)); 

    TableSchema sc;
    if (type == "Storage") {
      sc = new TableSchema().setFields(storageFields);
    } else {
      sc = new TableSchema().setFields(usageFields);
    }

    JobConfigurationLoad jcl = new JobConfigurationLoad()
    .setCreateDisposition("CREATE_IF_NEEDED")
    .setDestinationTable(tr)
    .setSkipLeadingRows(1)
    .setSourceUris(fileUris)
    .setSchema(sc)
    .setWriteDisposition("WRITE_APPEND");

    JobConfiguration jc = new JobConfiguration().setLoad(jcl);

    Job resp;

    try {
      req = bq.jobs().insert(PROJECT_ID.toString(), new Job().setConfiguration(jc));
      resp = req.execute();
    } catch (IOException e) {
      log.warning
      ("Could not submit bq insert job. Will be reattempted on next load run");
      e.printStackTrace();
      return null;
    }

    return resp;
  }
}
