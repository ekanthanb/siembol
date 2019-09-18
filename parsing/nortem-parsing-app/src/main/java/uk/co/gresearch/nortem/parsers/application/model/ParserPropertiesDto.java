package uk.co.gresearch.nortem.parsers.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.reinert.jjschema.Attributes;

@Attributes(title = "parser properties", description = "The settings of the parser")
public class ParserPropertiesDto {
    @JsonProperty("parser_name")
    @Attributes(description = "The name of the parser from parser configurations", required = true)
    private String parserName;

    @JsonProperty("output_topic")
    @Attributes(description = "The kafka topic for publishing parsed messages", required = true)
    private String outputTopic;

    public String getParserName() {
        return parserName;
    }

    public void setParserName(String parserName) {
        this.parserName = parserName;
    }

    public String getOutputTopic() {
        return outputTopic;
    }

    public void setOutputTopic(String outputTopic) {
        this.outputTopic = outputTopic;
    }
}
