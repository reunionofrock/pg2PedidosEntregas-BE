package com.ApiResponse;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.conexion.Conexion;

public class ApiResponse {

    public ApiResponse() {
        // Inicializa el objeto res al crear una instancia de ApiResponse
        this.res = new JSONObject();
    }
    /**
     * @return the httpCode
     */
    public int getHttpCode() {
        return httpCode;
    }

    /**
     * @param httpCode the httpCode to set
     */
    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    private boolean Indicator;
    private String IndicatorCode = "SUC";
    private JSONObject res;
    private JSONArray resArray;
    private String Code;
    private String Description;
    private String FunctionName;
    private int httpCode;

    public void Error() {
        this.Indicator = true;
        this.IndicatorCode = "ERR";
    }

    public void Errort() {
        this.Indicator = true;
        this.IndicatorCode = "ERR";
        this.Code = "1";
    }

    public void Success() {
        this.Indicator = false;
        this.IndicatorCode = "SUC";
    }

    public void setStatus(boolean isSuccess) {
        if (isSuccess) {
            this.Indicator = true;
            this.IndicatorCode = "SUC";
        } else {
            this.Indicator = false;
            this.IndicatorCode = "ERR";
        }
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public void setFunctionName(String FuncName) {
        this.FunctionName = FuncName;
    }

    public String getIndicator() {
        if (this.Indicator) {
            return "ERROR";
        } else {
            return "SUCCESS";
        }
    }

    public boolean HasError() {
        return this.Indicator;
    }

    public String getDescription() {
        return this.Description;
    }

    public String ParseResponse() throws JSONException {
        JSONObject resf = new JSONObject();
        resf.put(this.getFunctionName() + "Response",
                this.res.put("ApiResponseIndicator", this.getIndicator())
                        .put("ApiResponseCode", this.getCode())
                        .put("ApiResponseDescription", this.getDescription()));
        return resf.toString();
    }

    public void setRes(String key, Object value) {
        this.res.put(key, value);
    }

    public JSONObject getRes() {
        return this.res;
    }

    /**
     * @return the Code
     */
    public String getCode() {
        return Code;
    }

    /**
     * @param Code the Code to set
     */
    public void setCode(String Code) {
        this.Code = Code;
    }

    /**
     * @return the FunctionName
     */
    public String getFunctionName() {
        return FunctionName;
    }

    public String generateLog(String request, String response, Conexion conn, long startTime,
            long endTime) {
        String log = " ExecutionDate: " + new Date().toString() + " Request:" + request.replaceAll("\n", "")
                + " Response:" + response + new JSONObject(" {DBConnection: {\n" + conn + "}}").toString()
                + "} ExecutionTime: " + (endTime - startTime) + " ms\n";
        return log;
    }

    public String getLastUpdate() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        } catch (Exception e) {
            return new Date().toString();
        }
    }

    public String getLastUpdateAmx() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date());
        } catch (Exception e) {
            return new Date().toString();
        }
    }

    /**
     * @param res the res to set
     */
    public void setRes(JSONObject res) {
        this.res = res;
    }

    public String generateLog(String request, String response, long startTime, long endTime) {
        return " ExecutionDate: " + new Date().toString() + " Request:" + request.replaceAll("\n", "")
                + " Response:" + response + "} ExecutionTime: " + (endTime - startTime) + " ms\n";
    }

    /**
     * @return the resArray
     */
    public JSONArray getResArray() {
        return resArray;
    }

    /**
     * @param resArray the resArray to set
     */
    public void setResArray(JSONArray resArray) {
        this.resArray = resArray;
    }

}
