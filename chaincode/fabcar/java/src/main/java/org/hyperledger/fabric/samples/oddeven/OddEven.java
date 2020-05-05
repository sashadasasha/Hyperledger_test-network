/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.oddeven;

import java.util.ArrayList;
import java.util.List;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import com.owlike.genson.Genson;

/**
 *
 */
@Contract(
        name = "OddEven",
        info = @Info(
                title = "OddEven contract",
                description = "The hyperlegendary oddEven contract",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "f.oddeven@example.com",
                        name = "Odd Even",
                        url = "https://hyperledger.example.com")))
@Default
public final class OddEven implements ContractInterface {

    private final Genson genson = new Genson();

    private enum Errors {
        NUM_NOT_FOUND,
        NUM_ALREADY_EXISTS
    }

    /**
     * Retrieves a number with the specified key from the ledger.
     *
     * @param ctx the transaction context
     * @param key the key
     * @return the Number found on the ledger if there was one
     */
    @Transaction()
    public Number queryNumber(final Context ctx, final String key) {
        ChaincodeStub stub = ctx.getStub();
        String numberState = stub.getStringState(key);

        if (numberState.isEmpty()) {
            String errorMessage = String.format("Number %s does not exist", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, Errors.NUM_NOT_FOUND.toString());
        }

        Number num = genson.deserialize(numberState, Number.class);

        return num;
    }

    /**
     *
     * @param ctx
     * @param key
     * Create status of number - odd or even
     */
    @Transaction()
    public Number checkNumber(final Context ctx, final String key) {
        Number num = queryNumber(Context ctx, String key);
        String statusNumber = "";
        if (num % 2 == 0) {
            statusNumber = "even";
        } else {
            statusNumber = "odd";
        }

        ChaincodeStub stub = ctx.getStub();
        String statusState  = genson.serialize(statusNumber);
        stub.putStringState(key, statusState);
    }

    /**
     * Creates some initial Numbers on the ledger.
     *
     * @param ctx the transaction context
     */
    @Transaction()
    public void initLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        String[] numberData = {
                "{ \"number\": 12}",
                "{ \"number\": 15}",
                "{ \"number\": 18}",
            
        };

        for (int i = 0; i < numberData.length; i++) {
            String key = String.format("NUM%d", i);

            Number num = genson.deserialize(numberData[i], Number.class);
            String numberState = genson.serialize(num);
            stub.putStringState(key, numberState);
        }
    }

    /**
     * Creates a new car on the ledger.
     *
     * @param ctx the transaction context
     * @param key the key for the number
     * @param number the number of the new car
     * @return the created Number
     */
    @Transaction()
    public Number createNumber(final Context ctx, final String key, final int number) {
        ChaincodeStub stub = ctx.getStub();

        String numberState = stub.getStringState(key);
        if (!numberState.isEmpty()) {
            String errorMessage = String.format("Number %s already exists", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, Errors.NUM_ALREADY_EXISTS.toString());
        }

        Number num = new Number(number);
        numberState = genson.serialize(num);
        stub.putStringState(key, numberState);

        return num;
    }

    /**
     * Retrieves every car between NUM0 and NUM999 from the ledger.
     *
     * @param ctx the transaction context
     * @return array of Numbers  found on the ledger
     */
    @Transaction()
    public NumberQueryResult[] queryAllNumbers(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        final String startKey = "NUM0";
        final String endKey = "NUM999";
        List<NumberQueryResult> queryResults = new ArrayList<NumberQueryResult>();

        QueryResultsIterator<KeyValue> results = stub.getStateByRange(startKey, endKey);

        for (KeyValue result: results) {
            Number num = genson.deserialize(result.getStringValue(), Number.class);
            queryResults.add(new NumberQueryResult(result.getKey(), num));
        }

        NumberQueryResult[] response = queryResults.toArray(new NumberQueryResult[queryResults.size()]);

        return response;
    }

}
