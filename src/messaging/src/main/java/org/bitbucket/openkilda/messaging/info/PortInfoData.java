package org.bitbucket.openkilda.messaging.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Defines the data payload of a Message representing a port info.
 */
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "message_type",
        "switch_id",
        "port_no",
        "max_capacity",
        "state"})
public class PortInfoData extends InfoData {
    /**
     * Serialization version number constant.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Switch id.
     */
    @JsonProperty("switch_id")
    private String switchId;

    /**
     * Port number.
     */
    @JsonProperty("port_no")
    private int portNo;
    /**
     * Maximum capacity.
     */
    @JsonProperty("max_capacity")
    private int maxCapacity;

    /**
     * Port state.
     */
    @JsonProperty("state")
    private PortChangeType state;

    /**
     * Default constructor.
     */
    public PortInfoData() {
    }

    /**
     * Instance constructor.
     *
     * @param switchId    switch id
     * @param portNo      port number
     * @param maxCapacity maximum capacity
     * @param state       port state
     */
    @JsonCreator
    public PortInfoData(@JsonProperty("switch_id") final String switchId,
                        @JsonProperty("port_no") final int portNo,
                        @JsonProperty("max_capacity") final int maxCapacity,
                        @JsonProperty("state") final PortChangeType state) {
        this.switchId = switchId;
        this.portNo = portNo;
        this.maxCapacity = maxCapacity;
        this.state = state;
    }

    /**
     * Returns switch id.
     *
     * @return switch id
     */
    @JsonProperty("switch_id")
    public String getSwitchId() {
        return switchId;
    }

    /**
     * Sets switch id.
     *
     * @param switchId switch id to set
     */
    @JsonProperty("switch_id")
    public void setSwitchId(final String switchId) {
        this.switchId = switchId;
    }

    /**
     * Returns port number.
     *
     * @return port number
     */
    @JsonProperty("port_no")
    public int getPortNo() {
        return portNo;
    }

    /**
     * Sets port number.
     *
     * @param portNo port number to set
     */
    @JsonProperty("port_no")
    public void setPortNo(final int portNo) {
        this.portNo = portNo;
    }

    /**
     * Returns maximum capacity.
     *
     * @return maximum capacity
     */
    @JsonProperty("max_capacity")
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * Sets maximum capacity.
     *
     * @param maxCapacity maximum capacity to set
     */
    @JsonProperty("max_capacity")
    public void setMaxCapacity(final int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    /**
     * Returns port state.
     *
     * @return port state
     */
    @JsonProperty("state")
    public PortChangeType getState() {
        return state;
    }

    /**
     * Sets port state.
     *
     * @param state port state to set
     */
    @JsonProperty("state")
    public void setState(final PortChangeType state) {
        this.state = state;
    }
}