package uk.co.gresearch.siembol.response.common;

import uk.co.gresearch.siembol.response.engine.ResponseEngine;

import java.util.List;

public class RespondingResultAttributes {
    private ResponseEvaluationResult result;
    private ResponseAlert alert;
    private Evaluable respondingEvaluator;
    private String message;
    private String attributesSchema;
    private String rulesSchema;
    private String evaluatorType;
    private ResponseEngine responseEngine;
    private List<RespondingEvaluatorFactory> respondingEvaluatorFactories;
    private List<RespondingEvaluatorValidator> respondingEvaluatorValidators;

    public ResponseEvaluationResult getResult() {
        return result;
    }

    public void setResult(ResponseEvaluationResult result) {
        this.result = result;
    }

    public ResponseAlert getAlert() {
        return alert;
    }

    public void setAlert(ResponseAlert alert) {
        this.alert = alert;
    }

    public Evaluable getRespondingEvaluator() {
        return respondingEvaluator;
    }

    public void setRespondingEvaluator(Evaluable respondingEvaluator) {
        this.respondingEvaluator = respondingEvaluator;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAttributesSchema() {
        return attributesSchema;
    }

    public void setAttributesSchema(String attributesSchema) {
        this.attributesSchema = attributesSchema;
    }

    public List<RespondingEvaluatorFactory> getRespondingEvaluatorFactories() {
        return respondingEvaluatorFactories;
    }

    public void setRespondingEvaluatorFactories(List<RespondingEvaluatorFactory> respondingEvaluatorFactories) {
        this.respondingEvaluatorFactories = respondingEvaluatorFactories;
    }

    public String getRulesSchema() {
        return rulesSchema;
    }

    public void setRulesSchema(String rulesSchema) {
        this.rulesSchema = rulesSchema;
    }

    public String getEvaluatorType() {
        return evaluatorType;
    }

    public void setEvaluatorType(String evaluatorType) {
        this.evaluatorType = evaluatorType;
    }

    public ResponseEngine getResponseEngine() {
        return responseEngine;
    }

    public void setResponseEngine(ResponseEngine responseEngine) {
        this.responseEngine = responseEngine;
    }

    public List<RespondingEvaluatorValidator> getRespondingEvaluatorValidators() {
        return respondingEvaluatorValidators;
    }

    public void setRespondingEvaluatorValidators(List<RespondingEvaluatorValidator> respondingEvaluatorValidators) {
        this.respondingEvaluatorValidators = respondingEvaluatorValidators;
    }
}