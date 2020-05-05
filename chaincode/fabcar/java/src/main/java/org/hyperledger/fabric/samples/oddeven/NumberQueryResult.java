/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.oddeven;

import java.util.Objects;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonProperty;

/**
 * NumberQueryResult structure used for handling result of query
 *
 */
@DataType()
public final class NumberQueryResult {
    @Property()
    private final String key;

    @Property()
    private final Number record;

    public NumberQueryResult(@JsonProperty("Key") final String key, @JsonProperty("Record") final Number record) {
        this.key = key;
        this.record = record;
    }

    public String getKey() {
        return key;
    }

    public Number getRecord() {
        return record;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        NumberQueryResult other = (NumberQueryResult) obj;

        Boolean recordsAreEquals = this.getRecord().equals(other.getRecord());
        Boolean keysAreEquals = this.getKey().equals(other.getKey());

        return recordsAreEquals && keysAreEquals;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getKey(), this.getRecord());
    }

    @Override
    public String toString() {
        return "{\"Key\":\"" + key + "\"" + "\"Record\":{\"" + record + "}\"}";
    }

}
