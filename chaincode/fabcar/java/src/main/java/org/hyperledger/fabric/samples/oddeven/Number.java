/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.oddeven;

import java.util.Objects;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonProperty;

@DataType()
public final class Number {

     @Property()
     private final String statusOfNumber;

    @Property()
    private final int number;

    public int getNumber() {
        return number;
    }

    public String getStatusOfNumber() {
        return statusOfNumber;
    }

    public Number(@JsonProperty("number") final int number) {
        this.number = number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(/*getMake(), getModel(), getColor(),*/ getNumber());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [number=" +  number+ "]";
    }
}
