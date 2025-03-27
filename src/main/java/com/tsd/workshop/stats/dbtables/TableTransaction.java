package com.tsd.workshop.stats.dbtables;

public class TableTransaction {

    private final String tableName;
    private final Long lastTransactionId;

    public TableTransaction(String tableName, Long lastTransactionId) {
        this.tableName = tableName;
        this.lastTransactionId = lastTransactionId;
    }

    public String getTableName() {
        return tableName;
    }

    public Long getLastTransactionId() {
        return lastTransactionId;
    }
}
