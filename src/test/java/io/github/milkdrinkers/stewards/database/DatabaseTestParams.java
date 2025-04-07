package io.github.milkdrinkers.stewards.database;

import io.github.milkdrinkers.stewards.database.handler.DatabaseType;

record DatabaseTestParams(String jdbcPrefix, DatabaseType requiredDatabaseType, String tablePrefix) {
}
