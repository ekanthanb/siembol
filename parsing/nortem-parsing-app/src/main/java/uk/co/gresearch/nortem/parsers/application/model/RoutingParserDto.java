package uk.co.gresearch.nortem.parsers.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.reinert.jjschema.Attributes;

import java.util.List;

@Attributes(title = "routing parser", description = "The specification for the routing parser")
public class RoutingParserDto {
    @JsonProperty("router_parser_name")
    @Attributes(required = true, description = "The name of the parser that will be used for routing")
    private String routerParserName;

    @JsonProperty("routing_field")
    @Attributes(required = true,
            description = "The field of the message parsed by the router that will be used for selecting next parser")
    private String routingField;

    @JsonProperty("routing_message")
    @Attributes(description = "The field of the message parsed by the router that will be routed to the next parser " +
            "as a message for the further parsing", required = true)
    private String routingMessage;

    @JsonProperty("merged_fields")
    @Attributes(description = "The fields from the message parsed by the router that will be merged " +
            "to a message parsed by next the next parser", minItems = 1)
    private List<String> mergedFields;

    @JsonProperty("default_parser")
    @Attributes(description = "The parser that should be used if no other parsers will be selected", required = true)
    private ParserPropertiesDto defaultParser;

    @JsonProperty("parsers")
    @Attributes(description = "The list of parsers for the further parsing", required = true, minItems = 1)
    private List<RoutedParserPropertiesDto> parsers;

    public String getRouterParserName() {
        return routerParserName;
    }

    public void setRouterParserName(String routerParserName) {
        this.routerParserName = routerParserName;
    }

    public String getRoutingField() {
        return routingField;
    }

    public void setRoutingField(String routingField) {
        this.routingField = routingField;
    }

    public String getRoutingMessage() {
        return routingMessage;
    }

    public void setRoutingMessage(String routingMessage) {
        this.routingMessage = routingMessage;
    }

    public List<String> getMergedFields() {
        return mergedFields;
    }

    public void setMergedFields(List<String> mergedFields) {
        this.mergedFields = mergedFields;
    }

    public ParserPropertiesDto getDefaultParser() {
        return defaultParser;
    }

    public void setDefaultParser(ParserPropertiesDto defaultParser) {
        this.defaultParser = defaultParser;
    }

    public List<RoutedParserPropertiesDto> getParsers() {
        return parsers;
    }

    public void setParsers(List<RoutedParserPropertiesDto> parsers) {
        this.parsers = parsers;
    }
}
